package trap.dsl.compiler

import org.antlr.runtime.tree.CommonTree
import scala.collection.JavaConversions._ 
import scala.xml.Node
import trap.xml.Util._


class XMLGenerator(tokens:Array[String]) {
  def getChildren(tree:CommonTree) = if (tree.getChildren != null) tree.getChildren.map(_.asInstanceOf[CommonTree]) else Seq()
  def getXML(tree:CommonTree):Node = {
    val node = tokens(tree.getType) match {
      case s if s.startsWith("'") => getNode(tree.getText)
      case any => addAttribute(getNode(any), "name", tree.getText)
    }    
    addChildren(addAttribute(node, "line", tree.getLine.toString), getChildren(tree) map(getXML))
  }
}
