
package test.XDSLLoader

import trap.xdsl.XMappingLoader

object TestXMappingLoader {
  def main(args: Array[String]): Unit = {
    println("Compiling mapping")
    args.size match {
      case 1 => 
        // load XML into internal objects
        val testPatterns = TestDSLPatterns.lib.map(test.XDSLLoader.TestXDSLPatternUtil.getLibPattern)
        val loader = new XMappingLoader(args(0), testPatterns)
        println("\n mappings:")
        loader.loadedMappings foreach println
      case _ => println ("Usage java -jar JARFile <input_xdsl_file>")
    }    
  }
}
