package trap.dsl.compiler

import org.antlr.runtime.tree.CommonTree
import scala.collection.JavaConversions._ 
import trap.xml.Util._

class XDSLGenerator(tokens:Array[String]) {
  val xmlGen = new XMLGenerator(tokens)
  def getXDSL(t:CommonTree, src:String, ver:String) = <dsl ver={ver} file={src}>{xmlGen.getXML(t)}</dsl>
}

