
package trap.datalog

import trap.file.Util._
import trap.Util._
//import org.apache.commons.io.IOUtils

class RuleReader(initialRulesFile:String) {
  println("Initial rules file: "+initialRulesFile)
//  def streams = getStreams(initialRulesFile)
//  def streamsIndex = streams.zipWithIndex
//  def xdslRulesString = using(trycatch(streams)){is =>
    //println("[Loading rules] from: "+is)
//    IOUtils.toString(is, "UTF-8")      
//  }
  def xdslRulesString = trap.file.Util.readTextFileToString(initialRulesFile)
}