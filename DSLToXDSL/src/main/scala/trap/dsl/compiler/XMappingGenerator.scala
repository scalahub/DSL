
package trap.dsl.compiler

import org.antlr.runtime.tree.CommonTree
import scala.collection.JavaConversions._ 
import trap.xml.Util._

class XMappingGenerator (tokens:Array[String]) {
  val xmlGen = new XMLGenerator(tokens)
  def getXMapping(t:CommonTree, src:String, ver:String) = <mappings ver={ver} file={src}>{xmlGen.getXML(t)}</mappings>
}

