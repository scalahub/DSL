package test.DSLToXDSL
import trap.dsl._

object Main {
  def main(args: Array[String]): Unit = {
    //    println("Compiling dsl")
    args.size match {
      case 2 => 
        // generate XML from DSL
        DSLToXDSL.compile(args(0), args(1))
      case _ => println ("Usage test <input_file> <output_file>")
                println ("This will output XML file generated using the input DSL file")
    }    
  }
}
