
package trap.dsl

object GenerateDSLParser {
  val grammarFile = "src/DSL.g"
  val outputDir = "src/trap/dsl/antlrparser"
  println ("grammar is in "+grammarFile)
  println ("output is in "+outputDir)
  def main(args: Array[String]):Unit = org.antlr.Tool.main(Array(grammarFile, "-o", outputDir))
}
