package trap.xdsl

import trap.Util._
import trap.dsl.FactDataStructures._
import trap.xdsl.XDSLDataStructures._
import trap.xdsl.datalog.RuleDataStructures._
import scala.util.Random._

class XDSLCompilerException(s:String) extends Exception(s)

class XDSLToDatalogUtil(basisUtil:XDSLBasisUtil) {
  private var factMap:Array[(Pattern,FactDef)] = basisUtil.factMap  
  def addFactToMap(p:Pattern, f:FactDef) = factMap = factMap :+ (p, f)
  def findFactDef(pattern:Pattern):Option[FactDef] = factMap.find(_._1 == pattern) match {
    case Some((_, fact)) => Some(fact)
    case None => None
  }

  var createdRules:Seq[DLRule] = Seq()
  private var tempFactCounter = 0
  def getTempSchemaName = {tempFactCounter += 1; "temp"+tempFactCounter}
  def addRule(rule:DLRule) = createdRules = createdRules :+ rule
  def addRules(rules:Seq[DLRule]) = createdRules = createdRules ++ rules  
  def getParaDef(fact:FactDef, attr:Attr):(ParaDef, Int) = fact.paramDefs.zipWithIndex.find(_._1.paraName == attr.name.tail) match {
    case Some(tuple) => tuple
    case _ => throw new XDSLCompilerException ("attr: "+attr.name+" not found in fact: "+fact.factName)
  }
  def joinHeadSchema(left:DLHeadSchema, join:JoinType, right:DLHeadSchema):DLHeadSchema = {
    val head = DLHeadSchema(getTempSchemaName, left.vars)
    val (lTail, rTail, varOpVals) = (left.toTailSchema, right.toTailSchema, Seq())
    val newRules = join match {
      case And => Seq(DLRule(head, Seq(lTail, rTail), varOpVals))
      case Or => Seq(DLRule(head, Seq(lTail), varOpVals), DLRule(head, Seq(rTail), varOpVals))
      case Not => Seq(DLRule(head, Seq(lTail, rTail.negate), varOpVals))
      case Xor => Seq(DLRule(head, Seq(lTail, rTail.negate), varOpVals), DLRule(head, Seq(lTail.negate, rTail), varOpVals))
    }
    addRules(newRules)
    head
  }
  def getDlVar(attrOwnerFact:FactDef, attrParam:ParaDef, attrIndex:Int, dslAttr:Attr) = 
    DLAttrVar(if (attrParam.patternKey.isDefined) (attrOwnerFact.getKeyVars(attrIndex).get) // valid! (attrOwnerFact is in basis. No pattern key is skipped)
              else dslAttr.name.tail+getHash(nextLong.toString)) // the .tail method converts "@foo" to "foo"
  def getTailSchemaVars(fact:FactDef):Seq[DLVar] = fact.getKeyVars.collect{
    /* valid! There are two situations:
     *    1. fact is from basis: 
     *       In this case, we need all keys, and hence need to use getKeyVars (instead of getHeadKeyVars)
     *    2. fact is not from basis (fact from user defined pattern, created via createFactDef)
     *       Fact created via this method uses Pattern.returnedKeys (which contain only unskipped keys)
     *       and all such key are returned. Therefore the output of getKeyVars and getHeadKeyVars is identical
     *       in such cases.
     * 
     */
    case None => WildCard
    case Some(s) => DLPrimaryKeyVar(s)
  }
  def getSetIDs(fact:FactDef) = fact.paramDefs.filter(_.primaryKey.isDefined).map(_.primaryKey.get).toSet
  def insertAt(seq:Seq[DLVar], i:Int, v:DLVar):Seq[DLVar] = seq.indices.collect({
    case `i` => v
    case i => seq(i)
  })
  def getChainInit(start:FactDef, end:FactDef):Seq[DLTailSchema] = if (start == end) Seq() else {
    val ((sEdge, eEdge), detailedPath) = basisUtil.getSetsToSetsPath(getSetIDs(start), getSetIDs(end))
    val shortPath = detailedPath.map(_._1)
    val startPath = if (shortPath.size == 0 || shortPath(0) != start) List(start) ++ shortPath else shortPath
    val path = if (startPath.last == end) startPath.init else startPath
    val pathSet = path.toSet
    pathSet.map(x => DLTailSchema(x.factName, getTailSchemaVars(x))).toSeq
    // path.removeDuplicates.map(x => DLTailSchema(x.factName, getTailSchemaVars(x))).toSeq
  }
  def getSchemaForPatternKey(fact:FactDef, key:String) = {
    val keyVar = DLPrimaryKeyVar(key+getHash(nextLong.toString))
    val vars = getTailSchemaVars(fact) map(
      _ match {
        case DLPrimaryKeyVar(v) if v == key => keyVar
        case any => WildCard
      }
    )
    (keyVar, Seq(DLTailSchema(fact.factName, vars)))
  }
}
