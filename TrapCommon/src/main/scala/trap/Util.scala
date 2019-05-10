package trap

import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.PrintStream
import java.io.PrintWriter


object Util {
  val encoder = new sun.misc.BASE64Encoder
  val d = java.security.MessageDigest.getInstance("SHA-256");
  
  def getHash(words:String*) = 
    encoder.encode(d.digest(words.foldLeft("")((x, y)=>x+"|"+y).getBytes)).replaceAll("[^\\p{L}\\p{Nd}]", "").substring(0, 10)

  object NullOutputStream extends OutputStream {@Override def write(b:Int) {} }
  class StringOutputStream(s:StringBuffer) extends OutputStream {    
    @Override def write(b:Int) {
      s.append(new String(Array(b.asInstanceOf[Byte])));
    } 
  }
  def runCommand(cmd: Seq[String]): (Int, String, String) = {
    import sys.process._
    val stdoutStream = new ByteArrayOutputStream
    val stderrStream = new ByteArrayOutputStream
    val stdoutWriter = new PrintWriter(stdoutStream)
    val stderrWriter = new PrintWriter(stderrStream)
    val exitValue = cmd.!(ProcessLogger(stdoutWriter.println, stderrWriter.println))
    stdoutWriter.close
    stderrWriter.close
    (exitValue, stdoutStream.toString, stderrStream.toString)
  }

  def time(f: => Unit)={
    val s = System.currentTimeMillis
    f
    System.currentTimeMillis - s
  }  
  val nullOut = new PrintStream(NullOutputStream)
  var origOut = System.out
  def setPrintOff = System.setOut(nullOut);
  def setPrintOff(s:StringBuffer) = System.setOut(new PrintStream(new StringOutputStream(s)));
  def setPrintOn = System.setOut(origOut)


  var debug = false
  def printdbg(s:String) = if (debug) println(s)

  def stopAll(param: {def stop: Unit}*) = param.foreach(x => x.stop)
  def extractStrings(s:String):Array[String] = if (s != null) s.split(':').map(_.trim) else Array()
  def getIntList (t:Traversable[_]) = List.range(0, t.size)
  //def getIntList (a:Array[_]) = List.range(0, a.size)

  
    /** Used for generating random bytes */
  def randomBytes(numBytes:Int)= {
    var bytes = new Array[Byte](numBytes)
    scala.util.Random.nextBytes(bytes) // not secure
    bytes
  }
  def compareBytes(a:Array[Byte], b:Array[Byte]) =
    if (a.length == b.length) { // length must be equal
      var res = true
      Util.getIntList(a).foreach (i => res = res && a.apply(i)== b.apply(i))
      // above line computes the AND of the comparison of individual bytes in this and a
      res
    } else false

  def readUserInput(prompt:String) = {
    val br = new BufferedReader(new InputStreamReader(System.in))
    print (prompt)
    br.readLine()
  }
  /**
   * Generic function
   * it invokes func and if an exception, returns the result of func2, otherwise it returns the result of func. Both methods output generic type T 
   */
  def getOrElse[T](func : => T, func2: => T) =
    getOrNone(func).getOrElse(func2)

  // folllowing method not needed so commented out.
  //  def getOrElse[T](o:Option[Any], default: T): T = {
  //    o.getOrElse(None) match {
  //      case t:T => t
  //      case _ => default
  //    }
  //  }

  /**
   * Generic function
   * Takes as input func and func1, which are two methods. Both methods output generic type T and take no input.
   * This method invokes func and if an exception occurs, it returns None, otherwise it returns the result of func enclosed in a "Some" object. Therefore this method returns Option[T]
   */
  def getOrNone[T](func : => T) = {
    try{
      Some(func)
    } catch {
      case t:Throwable => None
    }
  }

  def getOrElseExit[T](func:()=>T) = try { func.apply } catch { case e:Exception =>
      println (e.getMessage+"\nProgram will exit")
      e.printStackTrace
      System.exit(1)
  }

  /**
   * Used for closing DB connections implicitly.
   * Also used for writing / reading to files
   * Code is borrowed - need to check correctness.
   * @author From the book "Beginning Scala"
   */
  def using[A <: {def close(): Unit}, B](param: A)(f: A => B): B =
  try { 
    f(param) 
  } finally { 
    if (param != null) param.close() 
  }
  /////////////////////////////////////////////////
  // following two copied from AmitCommon
  /////////////////////////////////////////////////
  type unitToT[T] = ()=>T
  def trycatch[B](list:List[unitToT[B]]):B = list.size match {
    case i if i > 1 => 
      try {
        list.head()
      } catch {
        case t:Any => trycatch(list.tail)
      }
    case 1 => 
      println("trycatch with one element")
      list(0)()
    case _ => throw new Exception("call list must be non-empty")
  }
  /////////////////////////////////////////////////
  /////////////////////////////////////////////////
  def getStreams(path:String, altPaths:String *) = {
    // println("getStreams: path = "+path)
    val stream0 = () => new FileInputStream(path)
    val stream1 = altPaths.map(p => () => new FileInputStream(p))
    val stream2 = () => this.getClass().getClassLoader().getResourceAsStream(path)
    //def stream3 = () => classOf[TraitFilePropertyReader].getResourceAsStream("/"+path)
    val stream4 = () => Thread.currentThread.getContextClassLoader.getResourceAsStream(path)
    val stream5 = () => Thread.currentThread.getContextClassLoader.getResourceAsStream("/"+path)
    List(stream0) ++ stream1 ++ List(stream2, stream4, stream5)
  }

  import scala.collection.mutable.ListBuffer
  def bmap[T](test: => Boolean)(block: => T): List[T] = {
    val ret = new ListBuffer[T]
    while(test) ret += block
    ret.toList
  }
}


