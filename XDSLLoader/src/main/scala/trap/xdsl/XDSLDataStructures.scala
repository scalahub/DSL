package trap.xdsl

import scala.language.existentials

class XDSLException(s:String) extends Exception(s)

object XDSLDataStructures {
  trait Op {val invert:Op}
  type OneOrMore[T] = Either[T, Joined[T]] 
  type ZeroOrMore[T] = Option[OneOrMore[T]]
  val (eql, neq, lt, gt, geq, leq, regex, glob) = ("=", "!=", "<", ">", ">=", "<=", "~~", "~")
  case object EQ extends Op {override def toString = eql ; val invert = EQ}
  case object NE extends Op {override def toString = neq ; val invert = NE}
  case object LT extends Op {override def toString = lt ; val invert = GT}
  case object GT extends Op {override def toString = gt ; val invert = LT}
  case object GE extends Op {override def toString = geq; val invert = LE}
  case object LE extends Op {override def toString = leq; val invert = GE}
  case object GLOB extends Op {override def toString = glob; val invert = GLOB}
  case object REGEX extends Op {override def toString = regex; val invert = REGEX}
  def getOpFromString(s:String) = s match {
    case `eql` => EQ; 
    case `neq` => NE; 
    case `leq` => LE; 
    case `geq` => GE; 
    case `lt` => LT ; 
    case `gt` => GT ; 
    case `glob` => GLOB; 
    case `regex` => REGEX
  }
  trait JoinType
  case object And extends JoinType
  case object Or extends JoinType; 
  case object Not extends JoinType
  case object Xor extends JoinType
  case class Joined[T](left:OneOrMore[T], joinType:JoinType, right:OneOrMore[T]) 
  //  {
  //    override def toString = left+" "+joinType.toString + " "+right
  //  }
  
  trait DSLType[T] { override def toString = this.getClass.getSimpleName}
  trait DSLPrimitiveType[T] extends DSLType[T]

  case class DSLPatternType(primaryKey:String) extends DSLPrimitiveType[Pattern] { 
    override def toString = "PatternType("+primaryKey+")" 
  }
  
  
  object DSLIntType extends DSLPrimitiveType[Int] {override def toString = "Int"}
  object DSLDecimalType extends DSLPrimitiveType[BigDecimal] {override def toString = "Decimal"}
  object DSLStringType extends DSLPrimitiveType[String] {override def toString = "String"}
  object DSLBooleanType extends DSLPrimitiveType[Boolean] {override def toString = "Boolean"}

  case class Attr(owner:Pattern, name:String, attrType:DSLPrimitiveType[_]) { 
    override def toString = owner+"."+name 
  }
    
  object DSLAttrType extends DSLType[Attr]  

  case class DSLValue[V](value:V, valueType:DSLType[V]) { override def toString = value.toString }
  case class DSLPrimitiveValue[V](value:V, valueType:DSLPrimitiveType[V]) // val int = DSLValue(1, IntType)

  case class Filter(attr:Attr, op:Op, value:DSLValue[_]) {
    (attr, value) match {
      case (Attr(owner, name, attrType), DSLValue(otherAttr:Attr, DSLAttrType)) if otherAttr.attrType == attr.attrType =>  
        if ((owner.name, name) == (otherAttr.owner.name, otherAttr.name)) throw new XDSLException ("lhs and rhs attrs cannot be identical") 
        // else value points to another attr of same type, do nothing.         
      case (Attr(owner, name, attrType), v:DSLValue[_]) if v.valueType == attr.attrType =>  // value is compatible with attr's type. Do nothing
      case (Attr(owner, name, DSLPatternType(l)), DSLValue(p, DSLPatternType(r))) if r.matches(l+".*\\d$") => 
        // the regex checks that r = l+some digits
        // rhs is pattern with pattern key rather than primary key. We allow this
      case _ => throw new XDSLException("invalid (attr-value) types: "+attr+"("+attr.attrType+"), "+value+"("+value.valueType+")")
      // case (Attr(_, _, DSLPatternType(x)), DSLValue(_, DSLPatternType(y))) if x.forall(y.contains(_)) => throw new XDSLException ("although the rhs attrs are a superset of lhs attrs, this feature is unsupported")
    }
    override def toString = attr.toString+op.toString+value.toString
      /*  // below test already conducted in lexer so skipping here
          op match {
            case GE | LE | LT | GT if value.valueType != IntType => throw new XDSLException("invalid op for attr-value pair: "+attr+", "+value+":"+op)
            case _ => 
          }
      */
  }
  case class Pattern(name:String, patternMatchers:ZeroOrMore[PatternMatcher], returnedCols:Seq[String], traversible:Boolean) {
    override def toString = name
    val defaultAttrs = Seq() // Seq(Attr(this, "@_count", DSLIntType))
    var availableAttrs: Seq[Attr] = defaultAttrs //, Attr(this, "@hash", DSLStringType))
    def addAttr(attr:Attr) = 
      if (availableAttrs.exists(_.name == attr.name)) throw new XDSLException("addattr: "+name+" already has "+attr.name) 
      else availableAttrs = availableAttrs :+ attr
    def isAllowed(attr:Attr) = availableAttrs.exists(_ == attr)
    def getAttr(name:String) = availableAttrs.find(_.name == name)
    def signature = name+(if (traversible) " [T]" else "")+" {"+returnedCols.reduceLeft(_+", "+_)+"} ("+
      (if (availableAttrs.size > 0) availableAttrs.map(k => k.name+": "+k.attrType.toString.replace("Type$", "")).reduceLeft(_+", "+_) else "")+")"
  }
  case class PatternMatcher(pattern:Pattern, filter:ZeroOrMore[Filter]) {    
    override def toString = "find "+pattern+(if (filter.isDefined) " where {"+getFilterString(filter.get)+"}" else "")
  } 
  def getFilterString(filter:OneOrMore[Filter]):String = filter match {
    case Left(f) => f.toString
    case Right(j) => "("+getFilterString(j.left)+" "+j.joinType+" "+getFilterString(j.right)+")"
  }
}
