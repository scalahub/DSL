package trap.dsl.analyzer

import trap.datalog.ResultParser
import trap.datalog.ResultWriter
import trap.datalog.rewriter.RuleRewriter
import trap.datalog.rewriter.RuleUtil
import trap.xdsl.XMappingDataStructures.AggrType
import trap.xdsl.XMappingDataStructures.Count
import trap.xdsl.XMappingDataStructures.Mapping
import trap.xdsl.datalog.RuleDataStructures.DLHeadSchema
import trap.xdsl.datalog.RuleDataStructures.DLPrimaryKeyVar
import trap.xdsl.datalog.RuleDataStructures.DLRule

class DSLAnalysis(extendedRules:String) {
  val solver = new trap.datalog.DatalogSolver()
  solver.addRules(extendedRules)
  @deprecated
  def getResults(find:Seq[DLHeadSchema], newRules:Seq[DLRule], inputFactFiles:Array[String], outputFile:String) = {
    newRules map (_.toString) foreach solver.addRules
    solver.loadFacts(inputFactFiles)
    solver.addQueries(find.map(_.toString).reduceLeft(_+"\n"+_))
    val answers = solver.getAnswers
    //println("loaded "+solver.getNumFacts+" facts")
    
    ResultWriter.writeResults(outputFile, answers)
    //println("Total rows returned: "+rows+". Results written to file: "+outputFile)
  }
  
  def getResultsMapped(
    finds:Seq[(DLHeadSchema, Option[Mapping])], 
    initialRulesString:String, 
    newRules:Seq[DLRule], 
    inputFactFiles:Array[String]
  ) = {
    
    val initialRules:Seq[DLRule] = new RuleUtil(initialRulesString).getRules // load initial rules into rewriter.RuleUtil

    // allRules are the rules combined from initial and the rules for this find query
    val allRules = initialRules ++ newRules

    //    // now invoke rule-rewriter for all the rules involved
    //    val optimizedRules = RuleRewriter.getOptimizedRules(allRules)
    //    //optimizedRules foreach (r => println("- "+r))
    //        
    //    // analyze using the optimizedRules
    //    optimizedRules map (_.toString) foreach solver.addRules

    solver.loadFacts(inputFactFiles)

    finds.map{case (headSchema, optMapping) =>
      solver.removeAllRules
      // now invoke rule-rewriter for all the rules involved
      val optimizedRules = RuleRewriter.getOptimizedRules(allRules, headSchema)
      //optimizedRules foreach (r => println("- "+r))
        
      // analyze using the optimizedRules
      optimizedRules map (_.toString) foreach solver.addRules


      // optMapping.get is of type 
      //   "map $name as $a => #User, $a => #Foo.@bar"     or 
      //   "map $name as $a => count, $a => #Foo.count"    or
      //   "map $name as $a => #User.count, $a => #User.@age.count,@sal.count"    
      //   
      //   each of 
      //     $a => count
      //     $a => #User
      //     $a => #Foo.@bar
      //     $a => #User.count 
      //     $a => #User.@age.count,@sal.count
      //     
      //   are of type KeyMap
      // need to make sure that vars in query are unique for aggregate mappings
      var ctr = 0
      def getCtr = {ctr += 1; ctr}

      val DLHeadSchema(name, vars) = headSchema
      val newVars = vars.map{
        case DLPrimaryKeyVar(varName) => DLPrimaryKeyVar(varName+getCtr)
      }
      solver.removeAllQuries
      solver.addQueries(DLHeadSchema(name, newVars).toString)
      val answers = solver.getAnswers
      val findQueryName = finds.map(_._1.name)
      val comment = 
      //  s"// find query #$findQueryName"+"\n"+
      "// "+(if (optMapping.isEmpty) "no mapping" else optMapping.get.getCode)
      
      //trap.file.Util.appendToTextFile(outputFile, s"""// $comment""")
      val isAggrMapping = optMapping.isDefined && optMapping.get.isAggr
      // def writeAggrResults(resultFile:String, results:Seq[(IQuery,IRelation, Seq[AggrType])]):Int = using (new FileWriter(resultFile, true)) { fw => 
      if (isAggrMapping) {
        val mapping:Mapping = optMapping.get
        val aggrTypes:Seq[AggrType] = mapping.keyMaps.flatMap{ keyMap => 
          // value:Either[AggrType, PatternAttrAggr]
          keyMap.value match {
            case Left(Count) => Seq(Count)
              // **** this case of Left(Count) is not yet handled in ResultWriter.writeAggrResults ****
              //     $a => count
            case Right(patternAttrAggr) => 
              //     $a => #User
              //     $a => #Foo.@bar
              //     $a => #User.count 
              //     $a => #User.@age.count,@sal.count
              // optionAttrAggr:Option[Either[Count, Seq[AttrAggr]]]
              patternAttrAggr.optionAttrAggr match {
                case Some(attrAggr) =>
                  attrAggr match{
                    case Left(Count) => Seq(Count)
                      //     $a => #User.count 
                    case Right(attrAggr) => 
                      attrAggr.map(x => x.optAggr.getOrElse(throw new Exception(s"cannot mix aggregate and non-aggregate in mapping: $mapping for $attrAggr")).aggrType)
                      // AttrAggr(attr:Attr, optAggr:Option[Aggr])
                      //     $a => #Foo.@bar
                      //     $a => #User.@age.count,@sal.count
                    case any => throw new Exception(s"invalid OptAttrAggr: $any for mapping: $mapping")
                      // should not happen
                  }
                case None => throw new Exception(s"cannot mix aggregate and non-aggregate in mapping: $mapping for $patternAttrAggr")
                  //     $a => #User
              }
            case Left(any) => throw new Exception(s"invalid aggregate $any in mapping: $mapping for $keyMap") 
          }
        }
        (ResultParser.getAggrResults(answers.map{case (iQuery, iRelation) => (iQuery, iRelation, aggrTypes)}), comment)
      } else (ResultParser.getResults(answers), comment)
    }
  }
}
