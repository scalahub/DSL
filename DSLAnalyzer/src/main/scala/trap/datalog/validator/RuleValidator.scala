package trap.datalog.validator

import scala.collection.JavaConversions._ 
import trap.dsl.FactDataStructures._
import trap.dsl.AbstractDSLConfig
import trap.dsl.DSLConfig
import RuleUtil._

class RuleValidator(dslConfig:AbstractDSLConfig, ruleString:String, printer:Any => Unit) {
  /*
   * Rule validation -- following rules are/must be validated
   * 1. For every pattern in the extended basis, there is at least one rule with that pattern as head relation
   * 2. There is no rule with a head relation that corresponds to facts in the reduced basis
   * 3. Every tail rule must either refer to a pattern in reduced basis or another head rule
   * 4. Every relation must have at least one rule where it is a head relation and it is not the tail relation in that rule (simple cycle check; does not check complex cycles)
   * 5. Every head relation must either be in extended basis or be used as a tail relation in another rule (i.e. no "unused rules")
   * 6. Relations with same names are consistent in number and type of parameters
   * 7. Incorrect wildcard usage. Variables starting with ?ANY should not be repeated in a rule
   */
  import dslConfig._
  import DSLConfig._ 
  
  def getTypedRuleMap(strs:Array[String], ruleMap:Array[IRISRule], basis:Seq[FactDef]) = {
    
    def inferredType(name:String, tail:Seq[IRISRel], remaining:Array[IRISRule]):IRISType = {
      val tailRels = tail.filter(rel => rel.vars.collect{case IRISAny(any) => any}.contains(name))
      if (tailRels.isEmpty) throw new UnrestrictedVariableException("unrestricted variable: "+name, name)
      tailRels map(tailRel => try {
          basis find(_.factName == tailRel.name) match {
            case Some(fact) => 
              (fact.paramDefs zip tailRel.vars) filter(_._2 == IRISAny(name)) map(_._1.paraType match {
                  case ParaIntType => IRISInt
                  case ParaStringType => IRISString
                  case ParaBooleanType => IRISBoolean
                  case ParaDecimalType => IRISDecimal
                }
              ) reduceLeft(checkVars(_, _))
            case _ => // tail not in basis
//              println(" -------------> ")
//              tail foreach println
//              println(" -------------> ")
              val rules = remaining filter(_.head.name == tailRel.name)
              
              rules map(rule => 
                (rule.head.vars zip tailRel.vars) filter(_._2 == IRISAny(name)) map(_._1 match {
                    case IRISAny(any) => 
                      inferredType(any, rule.tail, remaining.filter(_ != rule))
                    case any => any
                  }
                ) reduceLeft(checkVars(_, _))
              ) reduceLeft(checkVars(_, _))
          }
        } catch {
          case ex:Exception => 
            ex.printStackTrace
            throw new RelationException("error validating tail relation: "+tailRel, tail) 
        }
      ) reduceLeft(checkVars(_, _))
    }
    (strs zip ruleMap) map (strRule => {
        val (str, rule) = (strRule._1, strRule._2)
        val headVars = rule.head.vars collect {
          case IRISAny(any) => try {
              inferredType(any, rule.tail, ruleMap filter (_ != rule))
          } catch {
            case UnrestrictedVariableException(m, v) =>
              throw new RuleValidationException("unsafe rule: "+str+" due to "+m, rule, rule.head)
          }
          case any => any
        }
        IRISRule(IRISRel(rule.head.name, headVars), rule.tail)
      }
    )
  }
  def validate = if (validateRules) {
    val (red, ext) = (reducedBasis, extendedBasis)
    val (strs, ruleMap) = RuleUtil.getRuleMap(ruleString)
    val typedRuleMap = getTypedRuleMap(strs, ruleMap, red ++ ext)
    printer("== Following initial-rules found ==")
    typedRuleMap.map(_.head).toSet foreach printer
    printer("========")
    // check 6: (see above)
    val relGroup = typedRuleMap flatMap (r => (r.tail :+ r.head)) groupBy(_.name) map (_._2)     
    relGroup foreach (rels => try { checkConsistent(rels) } catch {
      case e:Exception => 
        println(e.getMessage)
        throw new RelationException("Error validating relations: [\n"+(rels.map(_.toString).reduceLeft(_ +"\n"+_))+"]", rels)
    })
    // check 1: each fact in extended basis has at least one rule in the ruleMap as head
    ext foreach (f => if (!typedRuleMap.exists(r => factMatch(f, r.head))) 
        throw new BasisValidationException("No rules found for extended pattern: "+f, f))
    // check 2: no rule defines a reduced basis pattern (a sanity restriction)
    red foreach (f => if (typedRuleMap.exists(r => factMatch(f, r.head)))
        throw new BasisValidationException("Reduced basis pattern cannot be defined via rules: "+f,f))
    // check 3: (see above)
    typedRuleMap foreach (rule => 
      rule.tail.foreach(t => 
        if (!(typedRuleMap.exists(otherRule => relMatch(otherRule.head, t) && otherRule != rule) || // should we really check otherRule != rule ??
              red.exists(f => factMatch(f, t))))
          throw new RuleValidationException("No rule or pattern found: "+t+" in rule: "+rule, rule, t)))
    // check 4: (see above)
    typedRuleMap foreach (rule => 
      if (!typedRuleMap.exists(otherRule => otherRule.head == rule.head && !otherRule.tail.contains(rule.head)))
        throw new RuleValidationException("No independent definition: "+rule.head+" in rule: "+rule, rule, rule.head)
      )
    // check 5: (see above)
    typedRuleMap foreach (rule => 
      if (!(
          typedRuleMap.exists(otherRule => otherRule != rule && otherRule.tail.exists(tail => relMatch(rule.head, tail))) ||
          ext.exists(f => factMatch(f, rule.head))          
        )
      ) throw new RuleValidationException("Unused head relation: "+rule.head+" in rule: "+rule, rule, rule.head)
    )
    // check 7: (see above)
    (strs zip typedRuleMap) foreach (x =>      
      { 
        val (str, rule) = (x._1, x._2)
        val vars = (getVars(rule.head.vars) ++ rule.tail.flatMap(t => getVars(t.vars))) filter (_.startsWith("?ANY"))
        if (vars.toSet.size != vars.size)
          throw new RuleValidationException("Wildcards are repeated in rule:   "+str, rule, rule.head)
      }
    )
  } //else println("Rule validation is not enabled. Skipping validation.")
}
