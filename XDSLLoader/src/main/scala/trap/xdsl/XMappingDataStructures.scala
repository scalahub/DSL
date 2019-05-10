
package trap.xdsl

import trap.xdsl.XDSLDataStructures._

object XMappingDataStructures {
  
  
  sealed abstract class AggrType(val name:String) {
    override def toString = name
  }
  object Max extends AggrType("max")
  object Min extends AggrType("min")
  object Avg extends AggrType("avg")
  object Sum extends AggrType("sum")
  object Count extends AggrType("count")
  object Ignore extends AggrType("") // if this is used, that key will be dropped (ignored). Example map :a as $x => ;
  
  object AggrType {
    def getAggrType(s:String) = Seq(Max, Min, Avg, Sum, Count, Ignore).find(_.name == s) match {
      case Some(aggrType) => aggrType
      case _ => throw new Exception(s"Invalid aggregate $s")
    }
  }
  
  case class Aggr(aggrType:AggrType, attr:Attr) { // aggregate 
    def getCode = attr.name+"."+aggrType
    // check valid attrs
    (aggrType, attr.attrType) match {
      case (Max | Min| Avg | Sum, DSLIntType | DSLDecimalType) => // ok
      case (Count, DSLIntType | DSLStringType | DSLBooleanType | DSLDecimalType) => // ok
      case (someAggr, someType) => throw new Exception(s"Incompatible aggregate: $someAggr for attribute $attr of type $someType")
    }
  }
  
  case class AttrAggr(attr:Attr, optAggr:Option[Aggr]) {    
    def getCode = if (optAggr.isDefined) optAggr.get.getCode else s"${attr.name}"
    def isAggr = optAggr.isDefined
  }
  
  case class PatternAttrAggr(pattern:Pattern, optionAttrAggr:Option[Either[AggrType, Seq[AttrAggr]]]) {    
    def getCode = pattern+{
      if (optionAttrAggr.isDefined) {
        // map :a as $user -> #user.@age,@name, $bar -> #foo
        "."+{
          optionAttrAggr.get match {
            case Left(Count) => Count.toString
            case Right(a) => a.map(_.getCode).reduceLeft(_+","+_)
            case Left(someAggr) => throw new Exception(s"Incompatible aggregate: $someAggr for pattern $pattern")
          }
        }
      } else ""
    }
    
    def isAggr = optionAttrAggr.isDefined && (optionAttrAggr.get.isLeft || {
        val seq = optionAttrAggr.get.right.get
        seq.foldLeft(seq.head)((left, right) => {
            if (left.isAggr != right.isAggr) throw new Exception(s"cannot mix aggregates and non-aggregates in ${left.getCode}, ${right.getCode}")
            right
          }
        ).isAggr
      }
    )
  }
  
  
  //Attr
  case class KeyMap(keyID:String, value:Either[AggrType, PatternAttrAggr]) {
    val valueStr = value match {
      case Left(Count) => Count.toString
      case Left(Ignore) => Ignore.toString
      case Right(p) => p.getCode
      case Left(someAggr) => throw new Exception(s"Incompatible aggregate: $someAggr for keyID $keyID")
    }
    def getCode = s"$$$keyID => $valueStr"
    
    def isAggr = value match {
      case Left(Count) => true
      case Right(right) if right.isAggr => true
      case _ => false
    }
    // added above to ignore Ignore type
    def isAggrOld = value.isLeft || value.right.get.isAggr
  }
  
  case class Mapping(name:String, keyMaps:Seq[KeyMap]) {
    def getCode:String = s"map $name as "+(keyMaps.map(_.getCode).reduceLeft(_+", "+_))+";"
    keyMaps.foldLeft(keyMaps.head)((left, right) => {
        if (left.isAggr != right.isAggr) throw new Exception(s"cannot mix aggregates and non-aggregates: ${left.getCode}, ${right.getCode} in $getCode")
        right
      }
    )
    def isAggr = keyMaps.head.isAggr // all must be aggr, so we only check head. The others are checked above
  }
}
