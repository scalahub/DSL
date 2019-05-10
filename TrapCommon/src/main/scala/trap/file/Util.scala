
package trap.file
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
//import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.InputStream
//import java.io.InputStreamReader
import java.io.PrintWriter
import org.apache.commons.io.FileUtils
import trap.Util._

object Util {

  /**
   * File related methods
   */
  def isDir(file:String) = new File(file).isDirectory
  def isFile(file:String) = new File(file).isFile
  def deleteFile(fileName:String) = {
    val file = new File(fileName)
    if (file.isFile) file.delete
    else false
  }
  def getFileNameFromFilePath(s:String)= s.split(java.io.File.separatorChar).last
  
  def createDir(dirName:String) = (new File(dirName)).mkdir
  def fileExists(fileName:String) = new File(fileName).exists
  def writeToTextFile(fileName:String, data:String) = using (new FileWriter(fileName)) {
    fileWriter => {
      fileWriter.write(data)
    }
  }
  def writeToBinaryFile(fileName:String, bytes:Array[Byte]) = 
    using (new BufferedOutputStream(new FileOutputStream(fileName))) {
      bos => bos.write(bytes)
    }

  /*  appendToFile implements following code. Code idea from
      http://www.coderanch.com/t/277950/Streams/java/insert-new-line-text-file

   */
  def appendToTextFile(fileName:String, textData:String) = using (new FileWriter(fileName, true)){
    fileWriter => using (new PrintWriter(fileWriter)) {
      printWriter => printWriter.println(textData)
    }
  }

  def readNthLine(srcFile:String, lineNo:Int) = try {
    using (scala.io.Source.fromFile(srcFile)){
      f => f.getLines.toArray.apply(lineNo-1)
//      f => f.getLine(lineNo)
    }
  } catch {
    case e:Throwable => ""
  }
  def readTextFileToString(fileName:String) = try {
    using (scala.io.Source.fromFile(fileName)) {
      x => x.mkString
    }
  } catch {
    case e:Throwable => ""
  }
  def readInputStreamToBytes(is:InputStream) =
    Stream.continually(is.read).takeWhile(-1 !=).map(_.toByte).toArray
  def readBinaryFileToBytes(fileName:String) = 
    using (new BufferedInputStream(new FileInputStream(fileName))) {
      bis => readInputStreamToBytes(bis) //Stream.continually(bis.read).takeWhile(-1 !=).map(_.toByte).toArray
    }
  
  def getAllFiles(dir:String, extensions:Array[String], recursive:Boolean) = try {
    FileUtils.listFiles(new File(dir), extensions, recursive).toArray.map(_.toString)
  } catch {
    case ex:Throwable => 
		ex.printStackTrace
		Array[String]()
  }
  def readBinaryFileToString(fileName:String) = new String(readBinaryFileToBytes(fileName))

}


