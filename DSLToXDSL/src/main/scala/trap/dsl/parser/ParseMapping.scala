
package trap.dsl.parser
import trap.Util._
import trap.file.Util._
import org.antlr.runtime.ANTLRStringStream
import org.antlr.runtime.CommonTokenStream
import trap.dsl.antlrparser.src._
import org.antlr.runtime.tree.CommonTree
import scala.collection.JavaConversions._ 

object ParseMapping {
  val extension="mapping"
  def getAST(inFile:String) = {    
    getASTFromCode(readTextFileToString(inFile)); //lexer splits input into tokens    
  }
  def getASTFromCode(code:String) = {    
    val input = new ANTLRStringStream(code); //lexer splits input into tokens    
    val tokenInputStream = new CommonTokenStream(new MappingLexer(input));
    val parser = new MappingParser(tokenInputStream)
    //val tree = parser.mapping.getTree.asInstanceOf[CommonTree]
    val tree = parser.mappings.getTree.asInstanceOf[CommonTree]
    val tokens = parser.getTokenNames
    //   printTree(tree, 0, tokens)    
    AST(tree, tokens)
  }
//  printAST("test.mapping")
  def main(a:Array[String]) = {
    printAST(a(0))
  }
  def printAST(inFile:String) = {
    val ast = getAST(inFile)
    printTree(ast.tree, 0, ast.tokens)
  }
  def printTree(tree:CommonTree, level:Int, tokens:Array[String]):Unit = {
    (1 to level) foreach (x => print("-")) //indent level    
    println(" " + tokens(tree.getType) + ":"+ tree.getText()); //print node description: type code followed by token text
    if (tree.getChildren() != null) tree.getChildren foreach (x => printTree(x.asInstanceOf[CommonTree], level+1, tokens))     //print all children
  }
}
