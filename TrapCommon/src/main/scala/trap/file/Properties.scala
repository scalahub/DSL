package trap.file

//import Util._
import java.io.FileInputStream
import trap.Util._

class PropertiesOld(path: String) {
  lazy val props: java.util.Properties = new java.util.Properties()
  val stream = new java.io.FileInputStream(new java.io.File(path))
  props.load(stream)
  def apply(key: String): Option[String] = Option(props.getProperty(key))
}
class Properties(path: String) {
  val props: java.util.Properties = new java.util.Properties()
  try {
    using(
      trycatch(
        getStreams(path)
      )
    ){is =>
      props.load(is)
      //println("loaded from: "+is+"["+path+"]")
    }
  } catch { case e : Throwable => 
      e.printStackTrace
      println ("File not found: "+path)
  }
  def apply(key: String): Option[String] = Option(props.getProperty(key))
}
 
object Properties {
  def apply(path: String) = new Properties(path)
}












                                                                                 
                                                                                 
                                                                                 
                                                                                 
                                                                                 
                                                                                 
                                                                                 
                                                                                 
                                                                                 
                                                                                 
                                                                                 
                                                                                 
                                                                                 
                                                                                 
                                                                                 
                                                                                 
                                                                                 
                                                                                 
                                                                                 
                                                                                 
                                                                                 
                                                                                 
                                                                                 
                                                                                 
                                                                                 
                                                                                 
                                                                                 