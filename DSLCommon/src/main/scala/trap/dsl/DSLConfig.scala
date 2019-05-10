package trap.dsl

import FactDefUtil._
import trap.dsl.FactDataStructures.FactDef
import trap.file._

case class DSLConfigException(message:String) extends Exception(message)

object DSLConfig extends TraitFilePropertyReader {
  //~ lazy val propertyFile = "dsl.properties"
  //~ lazy val optimizeRules = read("optimizeRules", false)
  //~ lazy val validateRules = read("validateRules", false)
  //~ lazy val useRegex = read("useRegex", true)
  lazy val propertyFile = "dsl.properties"
  lazy val optimizeRules = read("optimizeRules", false)
  lazy val validateRules = read("validateRules", true)
  lazy val useRegex = true //read("useRegex", true)
  def parseFacts(factStr:String) = factStr.replace(" ", "").replace("\t", "").split(";").filter(_ != "")  

}
abstract class AbstractDSLConfig {
  val reducedBasis:Array[FactDef]
  val extendedBasis:Array[FactDef]
}
class DSLConfig(bootstrapFile:String) extends AbstractDSLConfig{  
  import DSLConfig._
  // println ("bootstrapping from: "+bootstrapFile)
  
//  if (!Util.fileExists(bootstrapFile)) throw new DSLConfigException("bootstrap file does not exist: "+bootstrapFile)  
  lazy val bootStrap = Properties(bootstrapFile)
  
  lazy val resolveResults = bootStrap("resolveResults") match {
    case Some("true") => true
    case _ => false
  }
  lazy val resultMap = bootStrap("resultMap") match {
    case Some(s) => s.replace(" ", "").replace("\t","").split(";").map(_.split("=>")).map(x => (x(0), x(1))).toMap
    case _ => Map[String, String]()
  }
  lazy val reducedBasis = bootStrap("reducedBasis") match {
    case Some(s) => parseFacts(s) map(createFactDef)
    case _ => throw new DSLConfigException("reduced basis must be defined ")
  }
  lazy val extendedBasis = bootStrap("extendedBasis") match {
    case Some(s) => parseFacts(s) map(createFactDef)
    case _ => throw new DSLConfigException("extended basis must be defined ")
  }
  lazy val initialRulesFile = bootStrap("initialRulesFile") match {
    case Some(s) => s
    case _ => throw new DSLConfigException("initial rule file must be defined ")
  }  
}

