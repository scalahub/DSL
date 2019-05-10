
package trap.dsl

object GenerateMappingParser {
  val grammarFile = "src/Mapping.g"
  val outputDir = "src/trap/dsl/antlrparser"
  println ("grammar is in "+grammarFile)
  println ("output is in "+outputDir)
  def main(args: Array[String]):Unit = org.antlr.Tool.main(Array(grammarFile, "-o", outputDir))
}
