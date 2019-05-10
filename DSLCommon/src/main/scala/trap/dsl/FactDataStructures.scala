package trap.dsl


object FactDataStructures {  
  sealed trait ParaType[T] { 
    val text:String 
    def matches(data:Any):Boolean 
    def fromString(data:String):T
  }  
  case class ParaValue[T](value:T, valueType:ParaType[T])
  /*
   * This is for primary (initial program facts) extracted from Java code.
   * These support only Int and String types. The additional Pattern type is available only in extended facts
   * (those generated from primary facts), auch as dataflow, flow, etc
   */
  case object ParaIntType extends ParaType[Long] { 
    val text = "Int" 
    def matches(data:Any) = data match{
      case i:Int => true
      case i:Long => true
      case _ => false
    }
    def fromString(data:String) = data.toLong
  } 
  case object ParaStringType extends ParaType[String] {
    val text = "String" 
    def matches(data:Any) = data match{
      case s:String => true
      case _ => false
    }
    def fromString(data:String) = data
  }
  
  case object ParaBooleanType extends ParaType[Boolean] {
    val text = "Boolean" 
    def matches(data:Any) = data match{
      case s:Boolean => true
      case _ => false
    }
    def fromString(data:String) = data.toBoolean
  }

  case object ParaDecimalType extends ParaType[BigDecimal] {
    val text = "Decimal" 
    def matches(data:Any) = data match{
      case s:BigDecimal => true
      case _ => false
    }
    def fromString(data:String) = BigDecimal(data)
  }
  val types = Array[ParaType[_]](ParaIntType, ParaStringType, ParaBooleanType, ParaDecimalType)
  case class ParaDef(paraName:String, 
                     paraType:ParaType[_], 
                     primaryKey:Option[String]=None, 
                     patternKey:Option[String]=None, 
                     visible:Boolean=true, 
                     returned:Boolean=true) {
    // sanity checks start
    (primaryKey.isDefined, patternKey.isDefined, visible, returned) match {
      case (true, true, _, _) => throw DSLConfigException("primary key cannot be pattern key: "+primaryKey.get) // should never happen
      case (true, _, true, _) => throw DSLConfigException("primary key cannot be visible: "+primaryKey.get) // maybe remove this restriction later. For now it makes things easier to handle
      case (_, true, false,_) => throw DSLConfigException("pattern key must be visible: "+patternKey.get) // should never happen
      case (true, _, _,false) => throw DSLConfigException("primary key must be returned: "+primaryKey.get)
      case _ => // all good do nothing. 
    }
    // if (primaryKey.isDefined && visible) throw new DSLConfigException("primary key cannot be visible: "+primaryKey.get) // maybe remove this restriction later. For now it makes things easier to handle
    // if (primaryKey.isDefined && patternKey.isDefined) throw new DSLConfigException("primary key cannot be pattern key: "+primaryKey.get) // should never happen
    // if (patternKey.isDefined && ! visible) throw new DSLConfigException("pattern key must be visible: "+patternKey.get) // should never happen
    // if (primaryKey.isDefined && ! returned) throw new DSLConfigException("primary key must be returned: "+primaryKey.get)
    // sanity checks end    
    def matches(data:Any) = paraType.matches(data)
    override def toString = paraName
    // override def toString = "@"+paraName
  }  
  
  
  case class FactDef(factName:String, paramDefs:Seq[ParaDef], visible:Boolean=false) { 
    // returns mapping from paramDefs to Some(keyID) or None; Some(keyID) in case of pattern / primary key else none
    lazy val getKeyVars = {
      var patternKeyCtr = 0
      def getPatternKeyCtr = {patternKeyCtr += 1; patternKeyCtr}
      paramDefs.collect{
        case p if p.patternKey.isDefined => Some(p.patternKey.get+getPatternKeyCtr)
        case p if p.primaryKey.isDefined => Some(p.primaryKey.get)
        case _ => None
      }
    }
    lazy val getHeadKeyVars = getKeyVars zip paramDefs map (x => if (x._2.returned) x._1 else None)
    /******************* sanity checks start ******************/
    // check 1
      if (paramDefs.exists(_.patternKey.isDefined) && paramDefs.exists(_.primaryKey.isDefined)) 
        throw DSLConfigException("fact cannot have both pattern key(s) and primary key(s): "+factName)
    // check 2. We only allow stripping pattern keys from the end, not the middle
      val (headKeys, allKeys) = (getHeadKeyVars filter(_.isDefined) map(_.get), getKeyVars  filter(_.isDefined) map(_.get))
      if (! allKeys.startsWith(headKeys)) throw DSLConfigException("pattern keys in middle cannot be skipped: "+factName)
    /******************** sanity checks end *******************/
    def getSig = factName+paramDefs.foldLeft("(")((x, y) => x+", "+{
        y.paraName+":"+y.paraType.text+{
          // new stuff below 2 lines
            if (y.patternKey.isDefined) s"{${y.patternKey.get}}" else ""+
            (if (y.primaryKey.isDefined) s"[${y.primaryKey.get}]" else "")
          }
      }
    ).replace("(, ", "(")+")"    
    def matches(s:String, paramsData:Seq[Any]) = 
      if (paramsData.size != paramDefs.size || s != factName) false 
      else paramDefs zip(paramsData) forall (x => x._1.matches(x._2))
    override def toString = "#"+factName
    // override def toString = getSig
  }
  case class Fact(factDef:FactDef, params:Array[ParaValue[_]]) // case class Fact(f:FactDef, params:Array[Either[String, Int]])  
}
