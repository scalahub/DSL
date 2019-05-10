package trap.file

import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.{Properties => jProp}
import Util._
import trap.Util._

trait TraitFilePropertyReader {
  val propertyFile : String
  val propertyDirectory = "properties"
  //  createDir(propertyDirectory)
  def fullFileName : String = propertyDirectory + "/" + propertyFile
  var isInitialized = false
  val props = new jProp

//  /////////////////////////////////////////////////
//  // following two copied from AmitCommon
//  /////////////////////////////////////////////////
//  type unitToT[T] = ()=>T
//  def trycatch[B](list:List[unitToT[B]]):B = list.size match {
//    case i if i > 1 => 
//      try {
//        list.head()
//      } catch {
//        case t:Any => trycatch(list.tail)
//      }
//    case 1 => 
//      println("trycatch with one element")
//      list(0)()
//    case _ => throw new Exception("call list must be non-empty")
//  }
//  def using[A <: {def close(): Unit}, B](param: A)(f: A => B): B =
//  try { f(param) } finally { param.close() }
//  /////////////////////////////////////////////////
//  /////////////////////////////////////////////////

//  def initializeOld = {
//    def stream2 = () => new FileInputStream(fullFileName)
//    def stream1 = () => new FileInputStream("props"+"/"+propertyFile)
//    def stream3 = () => this.getClass().getClassLoader().getResourceAsStream(fullFileName)
//    def stream4 = () => classOf[TraitFilePropertyReader].getResourceAsStream("/"+fullFileName)
//    def stream5 = () => Thread.currentThread.getContextClassLoader.getResourceAsStream(fullFileName)
//    def stream6 = () => Thread.currentThread.getContextClassLoader.getResourceAsStream("/"+fullFileName)
//    try {
//      using(trycatch(List(stream1, stream2, stream3, stream4, stream5, stream6))){is =>
//        props.load(is)
//      }
//    } catch { case e : Throwable => 
//        e.printStackTrace
//        println ("File not found: "+fullFileName)
//    }
//    //    try { props.load(new FileInputStream(fullFileName))}
//    //    catch { case th:Throwable => println ("exception: "+fullFileName+": "+th.getMessage)}
//    isInitialized = true
//  }
  def initialize = {
    try {
      using(trycatch(getStreams(fullFileName, "props"+"/"+propertyFile))){is =>
        props.load(is)
        //println("Loaded propertyfile ["+propertyFile+"] from: "+is)
      }
    } catch { case e : Throwable => 
//        e.printStackTrace
        println ("File not found: "+fullFileName)
    }
    isInitialized = true
  }
  def read[T](name:String, default:T, func:String=>T): T = {
    if (!isInitialized) initialize
    val p = props.getProperty(name)
    if (p == null) {
      println("property ["+fullFileName+":"+name+"] not found. Using default value of "+default)
      default
    } else func(p.trim)
  }
  def read(name:String, default:String):String = read[String](name, default, x => x)
  def read(name:String, default:Int):Int = read[Int](name, default, _.toInt)

  def read(name:String):String = read(name, "")
  def read(name:String, default:Boolean):Boolean = read[Boolean](name, default, _.toBoolean)
  def write(name:String, value:String, comment:String) = {
    props.setProperty(name, value)
    props.store(new FileOutputStream(fullFileName), comment)
  }
}





















