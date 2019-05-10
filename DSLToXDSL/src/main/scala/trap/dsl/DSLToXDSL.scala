package trap.dsl
import trap.dsl.antlrparser.src.DSLParser
import trap.dsl.compiler._
import trap.dsl.parser._
import scala.xml.Elem
import trap.dsl.antlrparser._
import trap.file.Util._
import trap.xml.Util._


object DSLToXDSL {
  
  def compileCode(code:String, dslSrcFileName:String):Elem = {
    val ast = ParseDSL.getASTFromCode(code)
    val gen = new XDSLGenerator(ast.tokens)
    val version = DSLParser.version
    gen.getXDSL(ast.tree, dslSrcFileName, DSLParser.version)    
  }
  def compile(dslSrcFile:String):Elem = {
    // generate XML from DSL
    if (! fileExists(dslSrcFile)) throw new java.io.FileNotFoundException(dslSrcFile)
    val ast = ParseDSL.getAST(dslSrcFile)
    compileCode(readTextFileToString(dslSrcFile), dslSrcFile)
  }
  def compile(src:String, dest:String):Unit = {
    writeToTextFile(dest, formatNicely(compile(src))+"\n")  
  }
}
