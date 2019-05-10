
package trap.repl

import java.awt.Dimension
import java.awt.Font
import java.awt.Toolkit
import java.io.BufferedInputStream
import javax.swing.JFrame
import util.StringUtil
import java.awt.Color
import bsh.util.JConsole

import scala.collection.JavaConversions._

object TestREPLConsole extends App {
  def consoleCode(s:String):Seq[String] = {
    Seq("you input: ", s+s"\nof size ${s.size} chars")
    
  }
  val console = new REPLConsole(consoleCode, 1000, Seq("exit", "q"), "DQL console")
  console.console.addHistory("foo")
  console.start
  console.console.getHistory foreach println
}

object REPLConsole {
  type Input = String
  type Output = Seq[String]
  type REPLCode = Input => Output
  val defaultExitKeywords = Seq("exit", "quit", "end", "bye")
  val defaultMaxInputSize = 1024
}
/**
 * GUI code template borrowed from http://stackoverflow.com/questions/1255373/create-a-command-console/1324525#1324525
 */
  
/**
 * JConsole is a command line input console that has support
 * for command history, cut/copy/paste, a blinking cursor,
 * command completion, Unicode character input, coloured text
 * output and comes wrapped in a scroll pane.
 *
 * The gui code is borrowed from http://www.beanshell.org/manual/jconsole.html
 *
 * 
 */
import REPLConsole._
class REPLConsole(replCode:REPLCode, maxInputSize:Int, exitKeywords:Seq[String], consoleTitle:String) {
  def this(replCode:REPLCode, consoleTitle:String) = this(replCode, defaultMaxInputSize, defaultExitKeywords, consoleTitle:String)
  def this(replCode:REPLCode, maxInputSize:Int, consoleTitle:String) = this(replCode, maxInputSize, defaultExitKeywords, consoleTitle:String)
  def this(replCode:REPLCode, exitKeywords:Seq[String], consoleTitle:String) = this(replCode, defaultMaxInputSize, exitKeywords, consoleTitle:String)
  //define a frame and add a console to it
  val frame = new JFrame(consoleTitle);
  val console = new JConsole();
//  console.set
  def getHistory:Seq[String] = console.getHistory.map(_.asInstanceOf[String]).toSeq
//  frame.add
  val bufInput = new BufferedInputStream(console.getInputStream())
  def addHistory(seq:Seq[String]) = seq foreach console.addHistory
  def readInput = {
    val bytes = new Array[Byte](maxInputSize)
    val length = bufInput.read(bytes)
    (new String(bytes)).trim
    //StringUtil.convertUnicodeEscape(new String(bytes).substring(0, length-6))
  }
  val newline = System.getProperty("line.separator");
  def prompt = if (console != null) {
//    console.setFont(new Font("Consolas", Font.BOLD, 20))
    console.print("\nDQL> ", Color.BLUE)
//    console.print(" ") 
//    val f = new Font("Monospaced", Font.BOLD, 20)
//    console.setFont(new Font("Monospaced", Font.BOLD, 20))
    console.setForeground(Color.BLACK)
  };
  // def prompt = if (console != null) console.print("\nDQL> ", Color.BLUE);
  // def prompt = if (console != null) console.print("\nDQL> ");
  // def print(s:String) = if (console != null) console.print(s + newline, Color.RED) else println(s);
   
  def print(s:String) = if (console != null) {console.print(s, Color.RED);console.print("\n")} //else println(s);
    
  def start = try  {

    //  console.setFont(java.awt.Font.MONOSPACED)
    //  val font = new Font("Consolas", Font.BOLD, 12);
    // console.setWheelScrollingEnabled(true)
    //http://www.programcreek.com/java-api-examples/java.awt.Dimension
    val screenSize = Toolkit.getDefaultToolkit().getScreenSize()
    val screenWidth = (screenSize.getWidth()*0.7f).toInt;
    val screenHeight = (screenSize.getHeight()*0.7f).toInt;
    val dim = new Dimension(screenWidth, screenHeight)
    
    // console.setSize(dim)
  
    frame.getContentPane().add(console);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(dim);
    frame.setVisible(true);
    
    val history = trap.file.Util.readTextFileToString(historyFile).split('\n')
    addHistory(history)
    var input = ""

    prompt
    while({input = readInput; !exitKeywords.contains(input.toLowerCase)}) {
      val result = replCode(input)
      result foreach print
      prompt
    }
    stop
  } catch {
    case e:Exception => e.printStackTrace();
  }
  val historyFile = System.getProperty("user.home")+s"/.bsh_repl_$consoleTitle.history"
  def stop = {
    bufInput.close();
    val history = getHistory
    val str = if (history.isEmpty) "" else history.reduceLeft(_+"\n"+_)
    trap.file.Util.writeToTextFile(historyFile, str)
    frame.setVisible(false)
    frame.dispose
  }
  def pushString(output:String) = 
    try print(output) 
    catch {
      case any:Any => print("exception "+any.getMessage)
    } finally prompt
}


