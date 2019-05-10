package test.XDSLLoader

import trap.xdsl.XDSLLoader
import trap.xdsl.XMappingLoader

object TestXDSLLoader {
  def main(args: Array[String]): Unit = {
    println("Compiling dsl")
    args.size match {
      case 1|2 => 
        // load XML into internal objects
        val testPatterns = TestDSLPatterns.lib.map(test.XDSLLoader.TestXDSLPatternUtil.getLibPattern)
        val mappings = if (args.size == 2) {
          val xmappingLoader = new XMappingLoader(args(1), testPatterns)
          xmappingLoader.loadedMappings
        } else Nil
        val xdslLoader = new XDSLLoader(args(0), testPatterns, mappings)
//        println("\n visible library patterns:")
//        xdslLoader.visiblePatterns foreach  (println)
//        println("\n traversible library patterns:")
//        xdslLoader.visiblePatterns filter(_.traversible) foreach  (println)
        xdslLoader.loadDefinedPatterns foreach (println) //p => println("\n loaded pattern:\n"+p))        
//        xdslLoader.getFindPatternMatchers foreach (p => println("\n Find:\n"+p))
        xdslLoader.getFindPatterns foreach (println) //p => println("\n Find:\n"+p))
//        loader.getFindPatternMappers foreach (p => println("\n Map:\n"+p.getOrElse("none  ")))
//        loader.getFindPatternMappers foreach (p => println(s"\n Map: $p\n"+p.foldLeft("")((a, b) => a+"\n"+b)))
        //xdslLoader.getFindPatternMappers foreach (p => println(s"\n Map: $p"))
      case _ => println ("Usage java -jar JARFile <input_xdsl_file> [<input_xmapping_file>]")
    }    
  }
}
