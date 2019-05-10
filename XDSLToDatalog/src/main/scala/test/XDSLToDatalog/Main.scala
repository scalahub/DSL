package test.XDSLToDatalog

import trap.dsl.DSLToXDSL
import trap.xdsl.XDSLBasisUtil
import trap.dsl.DSLConfig
import trap.xdsl.XDSLLoader
import trap.xdsl.XDSLToDatalog
import scala.xml.Node

object Main {
  /*
   *  load definitions of initial facts ("atoms" of the system)
   */
  val dslConfig = new DSLConfig("java.properties")
  import dslConfig._
  println ("Total atomic fact defs: "+reducedBasis.size); 
    
  /*
   *  load definitions of ne facts generated from atomic facts ("extended" facts)
   */
  println ("Total extended fact defs: "+extendedBasis.size); 

  /*
   *  combine the two factDefs obtained above. 
   *  The "basis" of the system (the set of fact that queries will map to) is combination of 
   *  atomic and extended facts
   */
  val basis = Array(extendedBasis, reducedBasis).flatten  
  val basisUtil = new XDSLBasisUtil(basis)
  val compiler = new XDSLToDatalog(dslConfig, basisUtil)  

  def getFindQueries(xdsl:Node) = {
    val loader = new XDSLLoader(xdsl, basisUtil.patterns) // Load XML using some patterns
    loader.loadDefinedPatterns 
    loader.getFindPatternMatchers
  }

  def main(args: Array[String]): Unit = {
    args.size match {
      case i if i > 0 => 
        val xdsl = DSLToXDSL.compile(args(0)) // convert DSL to XDSL (XML)
        // println (" ====\n"+xdsl+"\n ====")
        val findQueries = getFindQueries(xdsl) // get find queries in XDSL
        // println ("// ---find---")
        // findQueries foreach println
        val datalogCode = findQueries map (compiler.getFindQuery(_, false))
        println ("// ---rules--- ")
        compiler.getCreatedRules foreach println
        println ("// ---query--- ")
        (findQueries zip datalogCode) foreach (x => println("// "+x._1+"\n"+x._2))
          // following is for testing 
          if (i >= 3) {
            println("Path [Fact("+args(1)+")=>Fact("+args(2)+")]")
            println(basisUtil.getPath(args(1), args(2)))
          }
          if (i >= 5) {
            println("Path [Set("+args(3)+")=>Fact("+args(4)+")]")
            println(basisUtil.getSetsToFactPath(Set(args(3)), args(4)))
          }
      case _ => println ("Usage java -jar JARFile <input_dsl_file> [<start> <end>]")
    }    
  }
}
