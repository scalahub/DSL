package trap.datalog.rewriter

import trap.xdsl.datalog.RuleDataStructures._

object RuleRewriter {
  def getOptimizedRules(rules:Seq[DLRule], find:DLHeadSchema):Seq[DLRule] = {
    rules // returning rules for now
  }
  /*
   *  Flow(?x, ?y) :- ....
   *  Foo(?x, ?y) :- Flow(?x, ?y), Web(?x), DB(?y)
   *  ==================================
   *  From https://www.researchgate.net/publication/311489506_JDQL_A_framework_for_Java_Static_Analysis
   *  Section: 4.2.2, Example 6
   *  
   *  Example 6 (Rule Rewriting).
   *  Consider the code:
   *    find #Flow where {@src = #foo and @dest = #bar}.
   *  Normally, this would map to rules computing #Flow first,
   *  and then applying the filters given by @src and @dest. 
   *  Additionally, #Flow could contain a large number of rows, 
   *  many of which might be filtered out by this. 
   *  Thus, it is desirable to avoid computation of the extra rows. 
   *  Rule-rewriting does exactly this - the filters are first evaluated and the original rule 
   *    is populated using the filter. 
   *  In our example, this would cause #Flow to be rewritten as (refer to Section 4.1):
   *    
   *    Flow(src, dest) :- Pred(src, dest), foo(src), bar(dest).
   *    Flow(src, dest) :- Flow(src, mid), Flow(mid, dest), foo(src), bar(dest).
   *  
   *  Note original #Flow below:
   *  
   *    Flow(src, dest) :- Pred(src, dest).
   *    Flow(src, dest) :- Flow(src, mid), Flow(mid, dest).
   */
  // case class DLRule(head:DLHeadSchema, tails:Seq[DLTailSchema], varOpVal:Seq[DLVarOpVal])
  // case class DLHeadSchema(name:String, vars:Seq[DLPrimaryKeyVar])
  // case class DLPrimaryKeyVar(name:String) extends DLValue(name) with DLVar
  // trait DLVar
  // abstract class DLValue[T](t:T)
  // case class DLTailSchema(name:String, vars:Seq[DLVar], isNegated:Boolean = false)
  // case class DLVarOpVal(dlVar:DLAttrVar, op:Op, value:DLValue[_])
  // case class DLAttrVar(name:String) extends DLValue(name) with DLVar
}
