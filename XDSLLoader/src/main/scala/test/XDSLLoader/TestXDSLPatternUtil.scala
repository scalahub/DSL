package test.XDSLLoader

import trap.xdsl.XDSLDataStructures._

class XDSLLoaderException(s:String) extends Exception(s)

object TestXDSLPatternUtil {
  private def getLibKey(owner:Pattern, keyStr:String):Attr = {   // keyStr = "@name:String" or @class:Pattern{2}
    val s = keyStr.split(":")  // Seq("@name", "String")
    val keyName = s(0) //  "@name"
    val keyType = s(1) match {  // "String"
      case "String" => DSLStringType
      case "Int" => DSLIntType
      case "Decimal" => DSLDecimalType
      case "Boolean" => DSLBooleanType
      case any if any.startsWith("Pattern") => DSLPatternType(any.split("\\{")(1).init)
      case any => throw new XDSLLoaderException("no such type: "+any)
    }
    Attr(owner, keyName, keyType)
  }
  def getLibPattern(str:String):Pattern = { // example val s = "#invoke[1,2](@name:String,@foo:Pattern{3},@interface:String,@static:String,@count:Int)"
    val s = str.replace(" ", "").replace("\t","")
    println ("library Pattern => "+s)
    val sp = s.split("\\(")       // Array("#invoke[1,2]", "@name:String,@class:String{2},@interface:String,@static:String,@count:Int)")
    val nameCols = sp(0).split("\\[") // Array("#invoke", "1,2]") or Array("#invoke")
    val name = nameCols(0)        // "#invoke"
    if(nameCols.size < 2) throw new XDSLLoaderException("primary keys not defined for pattern: "+name)
    val returnedKeys = nameCols(1).init.split(",")
    val p = Pattern(name, None, returnedKeys, true)   // None because lib pattern does not have any PatternMatchers within
    val libKeyStr = sp(1).init
    val libKeys = if (libKeyStr.size > 0) libKeyStr.split(",").map(getLibKey(p,_)) else Seq()
    if (libKeyStr.size > 0) libKeyStr.split(",").map(getLibKey(p,_)) foreach p.addAttr // Note: if libKeyStr.size == 0, then no lib keys (keys in other referred patterns only)
    p
  }  
}
