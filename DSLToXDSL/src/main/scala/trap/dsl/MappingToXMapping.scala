
package trap.dsl

import trap.dsl.antlrparser.src._
import trap.dsl.compiler._
import trap.dsl.parser._
import scala.xml.Elem
import trap.dsl.antlrparser._
import trap.file.Util._
import trap.xml.Util._

object MappingToXMapping {
  def compileCode(code:String, mappingSrcFileName:String):Elem = {
    // generate XML from DSL
    val ast = ParseMapping.getASTFromCode(code)
    val gen = new XMappingGenerator(ast.tokens)
    val version = MappingParser.version
    // writer.writeTree(dest, ast.tree, src, version)
    // println("Mapping version: "+version)
    gen.getXMapping(ast.tree, mappingSrcFileName, version)    
  }
  def compile(mappingSrcFile:String):Elem = {
    // generate XML from DSL
    if (! fileExists(mappingSrcFile)) throw new java.io.FileNotFoundException(mappingSrcFile)
    compileCode(readTextFileToString(mappingSrcFile), mappingSrcFile)
    //    val ast = ParseMapping.getAST(mappingSrcFile)
    //    println ("compiling "+mappingSrcFile)
    //    val gen = new XMappingGenerator(ast.tokens)
    //    val version = MappingParser.version
    //    // writer.writeTree(dest, ast.tree, src, version)
    //    println("Mapping version: "+version)
    //    gen.getXMapping(ast.tree, mappingSrcFile, version)    
  }
  def compile(src:String, dest:String):Unit = {
    writeToTextFile(dest, formatNicely(compile(src))+"\n")  
  }
  def main(args:Array[String]) = {
    compile(args(0), args(1))
  }
}
