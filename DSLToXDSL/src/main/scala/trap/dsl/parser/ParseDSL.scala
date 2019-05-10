package trap.dsl.parser

import org.antlr.runtime.tree.CommonTree
import trap.Util._
import trap.file.Util._
import java.nio.file.Files
import java.nio.file.Path
import org.antlr.runtime.ANTLRStringStream
import org.antlr.runtime.CommonTokenStream
import trap.dsl.antlrparser.src._
import scala.collection.JavaConversions._ 

case class AST(tree:CommonTree, tokens:Array[String])
object ParseDSL {
  val extension="dsl"
  
  def getASTFromCode(text:String) = {
    val input = new ANTLRStringStream(text) //lexer splits input into tokens    
    val tokenInputStream = new CommonTokenStream(new DSLLexer(input));
    val parser = new DSLParser(tokenInputStream)
    val tree = parser.patterns.getTree.asInstanceOf[CommonTree]
    val tokens = parser.getTokenNames
    //   printTree(tree, 0, tokens)    
    AST(tree, tokens)
  }
  
  def getAST(inPath:Path) = {
    getASTFromCode(Files.readAllLines(inPath).reduceLeft(_+"\n"+_))
    
  }
  def getAST(inFile:String) = {    
    getASTFromCode(readTextFileToString(inFile)); //lexer splits input into tokens    
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
