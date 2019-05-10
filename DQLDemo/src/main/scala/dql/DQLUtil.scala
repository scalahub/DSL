
package dql

import com.opencsv.CSVReader
import java.io._
//import java.nio.file._
import trap.Util
import _root_.trap.Util._
import trap.datalog.RuleReader
import trap.datalog.optimizer.RuleFilter
import trap.datalog.validator.RuleValidator
import trap.dsl._
import trap.dsl.FactDataStructures._
import trap.xdsl.XDSLDataStructures.PatternMatcher
import trap.xdsl.XMappingDataStructures.Mapping
import trap.xdsl._
import trap.dsl.analyzer.DSLAnalysis
import trap.file.Util._
import scala.util.Random
import scala.collection.JavaConversions._
import trap.xdsl.datalog.RuleDataStructures.DLHeadSchema
import trap.xdsl.datalog.RuleDataStructures.DLRule
import trap.datalog._
import org.apache.commons.codec.binary.{Base64 => B64}

// copied directly from common.Util (start)
object Base64 {
  def encodeBytes(b:Array[Byte]) = new String(B64.encodeBase64(b))
  def decode(s:String) = B64.decodeBase64(s)
}
// copied directly from common.Util (end)

object DQLUtil {
  lazy val dqlDir = {
    //    val dir = System.getProperty("user.home")+"/.dql"
    //    createDir(dir)
    val dir = "/tmp/dql"
    createDir(dir)
    if (!isDir(dir)) {
      val altDir = "tmp"
      createDir(altDir)
      altDir
    } else dir
  }
  println (" [DQL dir] "+dqlDir)
  var optConfig:Option[AbstractDSLConfig] = None 
  var optBasisUtil:Option[XDSLBasisUtil] = None 
  var optRuleFilter:Option[RuleFilter] = None
  var optMappings:Option[XMappingLoader] = None
  var defs:Seq[String] = Nil
  var verbose = false
  // copied directly from common.Util (start)
  //  def using[A <: {def close(): Unit}, B](param: A)(f: A => B): B =
  //  try { f(param) } finally { param.close() }
  def sha256Bytes(b:Array[Byte]):String = {
    val d = java.security.MessageDigest.getInstance("SHA-256");
    Base64.encodeBytes(d.digest(b))
  }
  def sha256Bytes2Bytes(b:Array[Byte]):Array[Byte] = {
    val d = java.security.MessageDigest.getInstance("SHA-256");
    d.digest(b)
  }
  def sha256(s:String):String = sha256Bytes(s.getBytes)
  def sha256Small(s:String):String = sha256(s).substring(0, 16)
  final val englishAlphaBet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
  def shaSmall(s:String):String = (cleanString(sha256(s))  match { // removes non-alphanumeric chars such as / and = 
    case x if "0123456789".contains(x(0)) => englishAlphaBet(x(0).toString.toInt)+x.substring(1)
    case x => x
  }).substring(0, 16)
  def cleanString(s:String) = if (s == null) "" else s.replaceAll("[^\\p{L}\\p{Nd}]", "") // removes non-alphanumeric chars
  // copied directly from common.Util (end)

  
  private def checkBasisExists = if (!optConfig.isDefined) throw new Exception("no basis defined. Load basis first")
  private def checkRulesExists = if (!optRuleFilter.isDefined) throw new Exception("no rules defined. Load rules first")
  private def checkMappingsExists = if (!optMappings.isDefined) throw new Exception("no mappings defined. Load mappings first")
  def loadBasis(basisFile:String) = {
    val config = new DSLConfig(basisFile)        

    val basisUtil = new XDSLBasisUtil(config)
    val compiler = new XDSLToDatalogNew(basisUtil)    
    optBasisUtil = Some(basisUtil)
    optConfig = Some(config)
    println("Loaded basis from: "+basisFile)
    "Loaded basis from: "+basisFile
  }
  def loadRules(rulesFile:String) = {
    checkBasisExists
    val xdslRulesString = new RuleReader(rulesFile).xdslRulesString
    
    new RuleValidator(optConfig.get, xdslRulesString, _ => ()).validate // validate rules first
    optRuleFilter = Some(new RuleFilter(xdslRulesString))
    "Loaded rules from: "+rulesFile+(if (verbose) xdslRulesString else "")
  }
  def loadMappings(mappingsFile:String) = {
    checkBasisExists    
    val mapping = new XMappingLoader(MappingToXMapping.compile(mappingsFile), optBasisUtil.get.patterns)
    val mappings = mapping.loadedMappings
    optMappings = Some(mapping)
    "Loaded mappings from: "+mappingsFile+(if (verbose) mappings.foldLeft("")((x, y) => x + "\n" + y.toString) else "")
  }
  var loadedFactFiles:Seq[(String, String)] = Nil // left is origName, right is internally storedName
  def loadFactFile(factsFile:String):String = {
    checkBasisExists    
    val fileName = getFileNameFromFilePath(factsFile).replace(".", "_")
    val rand = Random.nextInt.abs
    val outputFile = dqlDir+"/$"+fileName+s"_$rand.facts"    
    if (fileExists(outputFile) && readBinaryFileToBytes(outputFile).size > 0) throw new Exception(s"non-empty output file exists: $outputFile")
    lazy val inBytes = readBinaryFileToBytes(factsFile)
    if (fileExists(factsFile) && inBytes.size > 0) {
      val solver = new trap.datalog.DatalogSolver
      solver.loadFacts(factsFile) // for testing if facts are correctly formed     
      writeToBinaryFile(outputFile, inBytes)
      loadedFactFiles +:= (fileName, new File(outputFile).getName)
      s"file $factsFile added to database"
    } else {
      throw new Exception(s"input file not found or is empty")
    }
  }
  def getLoadedFactFiles = {
    
  }
  def unloadBasis = {
    optBasisUtil = None
    optConfig = None
  }
  def unloadMappings = {
    optMappings = None
  }
  def unloadRules = {
    optRuleFilter = None
  }
  def unloadFactFiles = {
    loadedFactFiles = Nil
  }
  def getFactFiles = {
    loadedFactFiles.map{_._1}.toArray
  }
  def getMappings = {
    checkMappingsExists    
    optMappings.get.loadedMappings.map(_.getCode).reduceLeft(_ +"\n" +_)    
  }
  def reset = {
    optBasisUtil = None
    optConfig = None
    optMappings = None
    optRuleFilter = None
    "Ok"
  }
  def getDefs = {
    if (defs.size > 0) defs.reduceLeft(_+"\n"+_) else "No defs"
  }
  
  def getBasis = {
    checkBasisExists
    val config = optConfig.get
    Array(config.extendedBasis, config.reducedBasis).flatten.map(_.getSig).reduceLeft(_ + "\n" + _)
  }
  
  def getRules = {
    checkRulesExists
    optRuleFilter.get.ruleString
  }
  def getParsedRules = {
    checkRulesExists
    val initialRules:Seq[DLRule] = new rewriter.RuleUtil(optRuleFilter.get.ruleString).getRules // load initial rules into rewriter.RuleUtil
    initialRules.map(_.toString).reduceLeft(_+"\n"+_)
  }
  def addReducedBasis(basisStr:String) = {
    val config = new AbstractDSLConfig {
      val reducedBasis = optConfig.map(_.reducedBasis).getOrElse(Array()) ++ DSLConfig.parseFacts(basisStr).map(FactDefUtil.createFactDef)
      val extendedBasis = optConfig.map(_.extendedBasis).getOrElse(Array())
    }
    
    val basisUtil = new XDSLBasisUtil(config)
    val compiler = new XDSLToDatalogNew(basisUtil)    
    optBasisUtil = Some(basisUtil)
    optConfig = Some(config)
    "Ok"
  }
  def addExtendedBasis(basisStr:String) = {
    val config = new AbstractDSLConfig {
      val reducedBasis = optConfig.map(_.reducedBasis).getOrElse(Array())
      val extendedBasis = optConfig.map(_.extendedBasis).getOrElse(Array()) ++ DSLConfig.parseFacts(basisStr).map(FactDefUtil.createFactDef)
    }
    val basisUtil = new XDSLBasisUtil(config)
    val compiler = new XDSLToDatalogNew(basisUtil)    
    optBasisUtil = Some(basisUtil)
    optConfig = Some(config)
    "Ok"
  }
  def addMapping(mappingStr:String) = {    
    val newMappingStr = if (mappingStr.trim.endsWith(";")) mappingStr else mappingStr+";"
    val mappingDSL = optMappings match{
      case Some(mapping) => 
        val mappingsStr = mapping.loadedMappings.map(_.getCode).reduceLeft(_+_)
        s"""
mappings ${mapping.mappingsID}
$mappingsStr
map $newMappingStr        
"""
      case _ => s"""
mappings tmpMappings
map $newMappingStr        
"""     
    }

    val mapping = new XMappingLoader(MappingToXMapping.compileCode(mappingDSL, "tmpFile"), optBasisUtil.get.patterns)
    val mappings = mapping.loadedMappings
    optMappings = Some(mapping)
    "Ok"
  }
  def addRule(ruleStr:String) = {
    checkBasisExists
    val ruleDSL = optRuleFilter match{
      case Some(ruleFilter) => 
        val rulesStr = ruleFilter.ruleString
        s"""
$rulesStr
$ruleStr
"""
      case _ => 
        s"""
$ruleStr
"""                
    }
    val randomNumber = Random.nextLong.abs
    val tmpFile = dqlDir+"/"+randomNumber+".rules"  
    writeToTextFile(tmpFile, ruleDSL)
    val xdslRulesString = new RuleReader(tmpFile).xdslRulesString
    new RuleValidator(optConfig.get, xdslRulesString, _ => ()).validate // validate rules first
    optRuleFilter = Some(new RuleFilter(xdslRulesString))
    "Ok"
  }
  def addDef(defString:String) = {
    checkBasisExists
    val olderDefs = defs.foldLeft("")((x, y) => x + "\n"+y)
    val newDef = "def "+defString
    val defCode = s"patterns console\n$olderDefs\n$newDef"    
    val xdsl = DSLToXDSL.compileCode(defCode, "tmpFile")
    val compiler = new XDSLToDatalogNew(optBasisUtil.get) 
    val loader = new XDSLLoader(xdsl, optBasisUtil.get.patterns) // Load XML using some patterns
    loader.loadDefinedPatterns 
    loader.getFindPatterns // why were we calling getFindPatterns? maybe to check def code?
    val rules = compiler.getCreatedRules
    defs :+= newDef
    "Ok"    
  }

  def xmlHash(x: xml.Node):String = { // strips line etc (for checking if two DQL codes are identical in semantics
    val a = x.attributes.filterNot{x =>
      x.key == "line" || x.key == "ver" || x.key == "file"
    }.foldLeft("")((u, attr) => shaSmall(u + shaSmall(attr.key + "|" + attr.value)))
    (x \ "_").foldLeft(a+shaSmall(x.text.trim))((u, v) => shaSmall(u + xmlHash(v)))
  }

  def compileDQL(dqlString:String) = {
    val xdsl = DSLToXDSL.compileCode(dqlString, "tmpFile")
    val compiler = new XDSLToDatalogNew(optBasisUtil.get) 
    val mappings = optMappings.map(_.loadedMappings).getOrElse(Nil)
    val loader = new XDSLLoader(xdsl, optBasisUtil.get.patterns, mappings) // Load XML using some patterns
    loader.loadDefinedPatterns 
    loader.getFindPatterns // why were we calling getFindPatterns? maybe to check def code?
    compiler.getCreatedRules
  }
  type Count = Int
  type Row = String
  type Rows = Seq[Row]
  type Comment = String
  
  def convertResultsToStrings(countsRowsComments:Seq[Seq[((Count, Rows), Comment)]]) = {
    countsRowsComments.flatMap{
      case countRowComment:Seq[((Int, Seq[String]), String)] =>
        countRowComment.flatMap{
          case ((count:Int, rows:Seq[String]), comment:String) =>
            val result = if (count > 10) {
              rows.take(10) ++ Seq("...", s"[showing top 10 of $count rows]", "") 
            } else rows ++ Seq(s"[$count rows]", "")
            
            Seq(comment)++result 
        }
    }
  }
  def find(findString:String) = {
    checkBasisExists
    val defsString = defs.foldLeft("")((x, y) => x + "\n"+y)
    val defCode = s"patterns console\n$defsString"    
    val dslCode = s"$defCode\nfind $findString"
    val randomNumber = Random.nextLong.abs
    val tmpFile = dqlDir+"/"+randomNumber+".result"        
    val allFactFiles = getAllFiles(dqlDir, Array("facts"), false).map{path => 
      val origName = new File(path).getName
      val truncatedName = origName.split('.')(0)
      (path, truncatedName, origName)
    }
    val reducedBasisNames = optConfig.get.reducedBasis.map(_.factName) // tail removes '#'
    val loadedFactFileNames = loadedFactFiles.map(_._2)
    val factFilePaths = allFactFiles.collect{
      case (path, truncatedName, origName) 
        if reducedBasisNames.contains(truncatedName) || loadedFactFileNames.contains(origName) => path
    }
    val countsRowsComments:Seq[Seq[((Int, Seq[String]), String)]] = 
      runDatalog(dslCode, factFilePaths) // this is where DQL is compiled and run
    convertResultsToStrings(countsRowsComments)
  }
  def runDatalogWriteResults(dslCode:String, dlFactsFiles:Array[String], resultFile:String) = {
    val countsRowsComments = runDatalog(dslCode, dlFactsFiles)
    val result = countsRowsComments.flatMap{
      case countRowComment =>
        countRowComment.flatMap{
          case ((count, rows), comment) => Seq(comment)++rows ++ Seq("", s"// [$count rows]")
        }
    }.reduceLeft(_+"\n"+_)
    writeToTextFile(resultFile, result)
  }
  def compile(findQuery:String) = {
    checkBasisExists
    val defsString = defs.foldLeft("")((x, y) => x + "\n"+y)
    val defCode = s"patterns console\n$defsString"    
    val dslCode = s"$defCode\n$findQuery"
    val findQueriesCompiled = getDatalog(dslCode)
    
    findQueriesCompiled.flatMap{
      case (findQueryMappings, findRules, _) => findRules.map(_.getRuleSting)
    }
  }
  
  def loadTable(tableName:String, file:String):String = {
    // tableName will start with #
    checkBasisExists    
    val tableNames = optConfig.get.reducedBasis.map(_.factName) // tail removes '#' 
    if (!tableNames.contains(tableName)) throw new Exception(s"no such table $tableName in basis")
    val factFile = dqlDir+"/"+tableName+".facts"    
    if (fileExists(factFile) && readBinaryFileToBytes(factFile).size > 0) throw new Exception(s"non-empty output file exists: $factFile")
    populateFacts(tableName, file, factFile)
  }
  private def convertStrsToData(fact:FactDef)(csvStrs:Seq[String]):Seq[Any] = {    
    fact.paramDefs.map(_.paraType).zipWithIndex.map{
      case (paraType, i) => paraType.fromString(csvStrs(i))
    }
  }

  private def populateFacts(factName:String, inputCSVFile:String, outputDLFile:String) = {
    checkBasisExists
    optConfig.get.reducedBasis.find(_.factName == factName) match {
      case Some(fact) =>
        def convert(strs:Seq[String]) = convertStrsToData(fact)(strs)
        using(new FileWriter(outputDLFile)) {fileWriter =>
          val rows = new CSVReader(new FileReader(inputCSVFile)).readAll.tail; // tail to remove header row
          val factWriter = new FactWriter(fileWriter, optConfig.get.reducedBasis)
          rows.foreach(row => factWriter.fact(factName, convert(row):_*))       
          s"loaded ${rows.size-1} facts into table $factName"
        }
      case _ => 
        optConfig.get.reducedBasis.map(_.factName) foreach println
        throw new Exception(s"no fact $factName found in reduced basis")
    }
  }

  private def runDatalog_V0(dslCode:String, dlFactsFiles:Array[String], outFile:String) = {
    checkBasisExists
    if (optConfig.get.extendedBasis.size > 0) checkRulesExists    
    val xdsl = DSLToXDSL.compileCode(dslCode, "tmpFile") // tmpFile will be shown as source in errors, not used otherwise
    val compiler = new XDSLToDatalogNew(optBasisUtil.get)    
    val loader = new XDSLLoader(xdsl, optBasisUtil.get.patterns, if (optMappings.isDefined) optMappings.get.loadedMappings else Nil)
    
    loader.loadDefinedPatterns // why is this called?
    
    loader.getFindPatterns.map{
      case (findQuery, findMappingNames) =>               
        // one find query can have multiple mappings
        // we need to process all simultaneously to avoid recomputing same thing
        
        val allMappings = optMappings.map(_.loadedMappings).getOrElse(Nil)
        val usedMappings = findMappingNames.map{
          name => allMappings.find{
            case mapping => mapping.name == name
          }.getOrElse(throw new Exception(s"no such mapping $name"))
        }       
        //:Seq[(Option[Mapping], DLHeadSchema, Seq[String])]
        val queriesMapped = compiler.getFindQueriesMapped(findQuery, usedMappings)
        val newRules = compiler.getCreatedRules 
        val xdslRulesString = optRuleFilter.map(_.ruleString).getOrElse("")
        new RuleValidator(optConfig.get, xdslRulesString, _ => () /* printer method */).validate  
        // filter rules
        val initRules = new RuleFilter(xdslRulesString).getUsedRules(newRules)            
        val ruleString = if (initRules.nonEmpty) initRules.reduceLeft(_+_) else ""
        val analyzer = new DSLAnalysis(ruleString)   
        //    analyzer.getResults(queries, newRules, dlFactsFiles, outFile)
        analyzer.getResultsMapped(
          queriesMapped.map{case (optMapping, (headSchema, _)) => (headSchema, optMapping)}, 
          "//empty",
          newRules, 
          dlFactsFiles
        )        
    }    
  }
  type HeadSchemaVarName = String
  type HeadSchemaVarNames = Seq[HeadSchemaVarName]
  type FindQueryMapping = (Option[Mapping], (DLHeadSchema, HeadSchemaVarNames)) 
  type FindQueryMappings = Seq[FindQueryMapping]  
  type FindRules = Seq[DLRule]
  type RuleString = String
  type FindQueryCompiled = (FindQueryMappings, FindRules, RuleString)
 
  //  Note types below
  //    type Count = Int
  //    type Row = String
  //    type Rows = Seq[Row]
  //    type Comment = String
  type CountRowsComment = ((Count, Rows), Comment)
  
  // V1 does not do rule-rewriting
  @deprecated("V1 does not do rule-rewriting", "14 Mar 2017")
  def runDatalog_V1(dslCode:String, dlFactsFiles:Array[String]): Seq[Seq[CountRowsComment]] = {
     
    val findQueriesCompiled:Seq[FindQueryCompiled] = getDatalog(dslCode) // this compiles DQL to Datalog
    // returns a Seq of FindQueryCompiled, one for each find query
    findQueriesCompiled.map{//for each find query
      case (findQueryMappings:FindQueryMappings, findRules:Seq[DLRule], ruleString:RuleString) =>
        // one find query
        // Now we have found following:
        //  findQueryMappings are information about converting a find query to final results
        //  findRules are Seq[DLRule] that capture the new rules created
        //  ruleString are the initial USED (filtered) rules flatMapped to a string
        // 
        val analyzer = new DSLAnalysis(ruleString)   
        analyzer.getResultsMapped(
          findQueryMappings.map{
            case (optMapping:Option[Mapping], (headSchema:DLHeadSchema, _)) => (headSchema, optMapping)
          }, 
          "//empty",
          findRules, dlFactsFiles
        ):Seq[CountRowsComment] // one CountRowsComment for each mapping       
    }:Seq[Seq[CountRowsComment]] // one Seq[CountRowsComment] for each find query
  }  

  // V2 does rule-rewriting
  def runDatalog(dslCode:String, dlFactsFiles:Array[String])
   : Seq[Seq[CountRowsComment]] = {
     
    val findQueriesCompiled:Seq[FindQueryCompiled] = getDatalog(dslCode) // this compiles DQL to Datalog
    // returns a Seq of FindQueryCompiled, one for each find query
    findQueriesCompiled.map{ //for each find query
      case (findQueryMappings:FindQueryMappings, findRules:Seq[DLRule], ruleString:RuleString) =>
        // one find query
        // Now we have found following:
        //  findQueryMappings are information about converting a find query to final results
        //  findRules are Seq[DLRule] that capture the new rules created
        //  ruleString are the initial USED (filtered) rules flatMapped to a string
        // 
        // We need to now do rule-rewriting using: 
        //  ruleString (initial used rules for this find query)
        //  findRules (new rules created for this find query)
        //  
        // initialize DSLAnalysis with empty initial rules, since now initial rules are also provided as input to getResultsMapped
        val analyzer = new DSLAnalysis("// Initial rules supplied via 'getResultsMapped'.")   
        analyzer.getResultsMapped(
          findQueryMappings.map{
            case (optMapping:Option[Mapping], (headSchema:DLHeadSchema, _)) => (headSchema, optMapping)
          }, ruleString, findRules, dlFactsFiles
        ):Seq[CountRowsComment] // one CountRowsComment for each mapping       
    }:Seq[Seq[CountRowsComment]] // one Seq[CountRowsComment] for each find query
  }  

  // converts dql to datalog rules
  def getDatalog(dslCode:String):Seq[FindQueryCompiled] = {
    checkBasisExists
    if (optConfig.get.extendedBasis.size > 0) checkRulesExists    
    val xdsl = DSLToXDSL.compileCode(dslCode, "tmpFile") // tmpFile will be shown as source in errors, not used otherwise
    val compiler = new XDSLToDatalogNew(optBasisUtil.get)    
    val loader = new XDSLLoader(xdsl, optBasisUtil.get.patterns, if (optMappings.isDefined) optMappings.get.loadedMappings else Nil)
    
    loader.loadDefinedPatterns // why is this called?
    
    loader.getFindPatterns.map{
      case (findQuery:PatternMatcher, findMappingNames) =>               
        // one find query, zero or more mappings
        // one find query can have multiple mappings
        // we need to process all simultaneously to avoid recomputing same thing
        
        val allMappings = optMappings.map(_.loadedMappings).getOrElse(Nil)
        
        // mappings used in this find query
        val usedMappings = findMappingNames.map{
          name => allMappings.find{
            case mapping => mapping.name == name
          }.getOrElse(throw new Exception(s"no such mapping $name"))
        }       
        
        // below returns the compiled find query (in DQL, compiled to Datalog), along with mappings info
        // Note that Rules are not accessed yet. The compiler only looks at basis (both reduced and extended), 
        // and creates new rules.
        // It assumes that there are additional rules for generating extended basis from reduced basis,
        // which will be fed separately to the Datalog engine to be used later.
        // 
        val queriesMapped:Seq[(Option[Mapping], (DLHeadSchema, Seq[String]))] = 
          compiler.getFindQueriesMapped(findQuery, usedMappings)
        // returns a sequence of head schemas for each mapping for the given find query
        // if no mappings, (i.e. Seq is empty, it will still return One headSchema for the ids)
        // if n mappings, then it will return n head schemas.
        // 
        // last element, Seq[String], returns a list of names to be displayed in the final results
        // they are not used anywhere internally.
        // So it may return Seq("name", "age", "<?>", "name", "age")
        //  The <?> bit is to separate multiple returned keys such as in flow, which needs two pattern keys
        //  src and dest. These two will be separated by <?>
        
        // below are the new rules created for ONE find query. Mappings are not yet considered
        val newRules:Seq[DLRule] = compiler.getCreatedRules 
        
        // Loads and processes the initial rules
        val xdslRulesString:String = optRuleFilter.map(_.ruleString).getOrElse("") // inital rules??
        // validates rules based on logic specified in DQL spec
        new RuleValidator(optConfig.get, xdslRulesString, _ => () /* printer method */).validate  
        // filter out those rules that are used in the current find query, ignoring unused rules
        // it uses "reachability" analysis to find those rules
        val initRules = new RuleFilter(xdslRulesString).getUsedRules(newRules)            
        // initRules above are only those initial rules that are used in the current find query
        
        val ruleString = if (initRules.nonEmpty) initRules.reduceLeft(_+_) else ""
        
        // returns Mapping info, new rules (in form of Seq[DLRule] amd used initial rules flatMapped as string
        (queriesMapped, newRules, ruleString)
    }    
  }
  println(" [DQLUtil] Loaded")
}
