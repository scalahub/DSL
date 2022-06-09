package trap.datalog.validator

import trap.dsl.FactDataStructures._

abstract class IRISType
case class IRISAny(name: String) extends IRISType { override def toString = "Any" }
object IRISString extends IRISType { override def toString = "String" }
object IRISDecimal extends IRISType { override def toString = "Decimal" }
object IRISBoolean extends IRISType { override def toString = "Boolean" }
object IRISInt extends IRISType { override def toString = "Int" }
case class IRISRel(name: String, vars: Seq[IRISType]) {
  override def toString = name + "(" + vars.map(_.toString).reduceLeft(_ + "," + _) + ")"
}
case class IRISRule(head: IRISRel, tail: Seq[IRISRel]) {
  override def toString = head + ":-" + tail.map(_.toString).reduceLeft(_ + "," + _) + "."
}

case class RuleParsingException(message: String, detail: String) extends Exception(message)
case class RuleValidationException(message: String, rule: IRISRule, rel: IRISRel) extends Exception(message)
case class BasisValidationException(message: String, pattern: FactDef) extends Exception(message)
case class RelationException(message: String, rels: Seq[IRISRel]) extends Exception(message)
case class UnrestrictedVariableException(message: String, variable: String) extends Exception(message)
case class TypeException(message: String, types: Seq[IRISType]) extends Exception(message)

object RuleUtil {
  val splChar = '$'
  def paraMatch(p: ParaDef, v: IRISType) =
    (p.paraType, v) match {
      //case (ParaIntType, IRISInt) if v == IRISInt || v == IRISAny => true
      case (_, IRISAny(_))                => true
      case (ParaIntType, IRISInt)         => true
      case (ParaStringType, IRISString)   => true
      case (ParaBooleanType, IRISBoolean) => true
      case (ParaDecimalType, IRISDecimal) => true
//    case (ParaStringType, IRISAny(_)) => true
      case (anyP, anyV) =>
        println(s"==============> anyP: $anyP, anyV: $anyV")
        false
    }
  def typeMatch(left: IRISType, right: IRISType) =
    (left, right) match {
      case (IRISAny(_), _)  => true
      case (_, IRISAny(_))  => true
      case (l, r) if l == r => true
      case (l, r) =>
        false
      /* a = Any, s = String, i = Int, t = true, f = false
            a a  t
            a s  t
            a i  t
            s a  t
            s s  t
            s i  f
            i a  t
            i s  f
            i i  t
       */

    }
  def relMatch(left: IRISRel, right: IRISRel) = {
    left.name == right.name &&
    left.vars.size == right.vars.size &&
    (right.vars zip left.vars).forall(x => typeMatch(x._1, x._2))
  }
  def factMatch(fact: FactDef, rel: IRISRel) = {
    val vars = rel.vars
    val paras = fact.paramDefs
    fact.factName == rel.name &&
    paras.size == vars.size &&
    (paras zip vars).forall(x => paraMatch(x._1, x._2))
  }
  def subst(vars: String) = {
    var (quote, escape) = (false, false)
    for (i <- vars) yield {
      i match {
        case any if escape            => escape = false; splChar
        case ',' | '(' | ')' if quote => splChar
        case '\'' if quote            => quote = false; splChar
        case '\\' if quote            => escape = true; splChar
        case any if quote             => splChar
        case '\''                     => quote = true; splChar
        case any                      => any
      }
    }
  }
  def getName(s: String) = if (s.startsWith("!")) s.tail else s
  def getRel(s: String) = {
    // foo(?x,?y) or foo(?x, 'bar')
    val ar = s.split("\\(", 2)
    val name = getName(if (ar(0).startsWith(",")) ar(0).tail else ar(0))
    IRISRel(
      name,
      ar(1).split(',') map (_ match {
        case any if any(0) == '?'                         => IRISAny(any)
        case any if any(0) == splChar                     => IRISString
        case any if any forall ("0123456789".contains(_)) => IRISInt
        case any                                          => throw new RuleParsingException("Unknown parameter type: " + any + " in relation: " + name + "(" + ar(1) + ")", name)
      })
    )
  }
  def getRule(s: String) = {
    /*
       s is foo(?x, ?y) :- foo(?y, ?z), bar(?z, ?x), ?x = 'foo'.
         or foo(?x, ?y) :- foo(?y, ?z), bar(?z, ?x).
         or foo(?x, ?y) :- foo(?y, ?z).
     */
    val ar = subst(s).split(":-")
    val (left, right) = (ar(0), ar(1))
    val head = getRel(left.init)
    /*
       right is  foo(?y, ?z), bar(?z, ?x), ?x = 'foo'.
             or  foo(?y, ?z), bar(?z, ?x).
             or  foo(?y, ?z).
     */
    val tmp = right.split(')')
    val tails = tmp filter (_.contains('(')) map (getRel)
    // val varOpVals = tmp filter(!_.contains('(')) // ignore varOpVals for now. Later use it to further narrow down the type
    IRISRule(head, tails)
    /*
       builtin 	=
	{binary} 	[first]: term (!= | < | <= | > | >=) [second]: term
| 	{equals} 	[first]: term (=) [second]: term
| 	{ternary} 	[first]: term (+,*,-./) [second]: term (=) [third]: term
     */
  }
  def getRuleMap(ruleString: String) = {
    /* input ruleString is any IRIS string representing rules (including comments). Example:
        foo(?x, ?y) :- foo(?y, ?z), bar(?z, ?x), ?x = 'foo'. // some dummy rule

       returns Array of each rule string along with a zipped version of string and corresponding parsed rule
     */
    // first clean ruleString (remove spaces, tabs, blank lines)
    val cleanedString = ruleString.replace(" ", "").replace("\t", "").split("\n").filter(_ != "")
    // now filter comments out and get rules as each list[String]
    val rules = cleanedString.filter(!_.startsWith("//")).toList
    rules foreach (rule => if (rule.contains("//")) throw RuleParsingException("Comment must be on its own line: " + rule, rule))
    val strs = rules.foldLeft("")((x, y) => x + y).split('.').filterNot(_.isEmpty) // rules reduceLeft(_+_) split('.')
    (strs, strs map getRule)
  }
  def checkConsistent(s: Array[IRISRel]) = {
    if (s.size > 1) s.tail.foldLeft(s.head)(checkRels(_, _))
  }
  def checkRels(l: IRISRel, r: IRISRel): IRISRel = {
    if (l.vars.size != r.vars.size)
      throw RelationException("Inconsistent number of parameters: (" + l + ", " + r + ")", Seq(l, r))
    IRISRel(l.name, l.vars zip r.vars map (x => checkVars(x._1, x._2)))
  }
  def checkVars(l: IRISType, r: IRISType): IRISType = {
    (l, r) match {
      case (IRISAny(_), any)              => any
      case (any, IRISAny(_))              => any
      case (left, right) if left == right => left
      case _                              => throw TypeException("Inconsistent type of parameters: (" + l + ", " + r + ")", Seq(l, r))
    }
  }
  def getVars(s: Seq[IRISType]) =
    s collect {
      case IRISAny(any) => any
    }
}
