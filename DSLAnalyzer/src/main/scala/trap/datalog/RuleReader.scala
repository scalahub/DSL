
package trap.datalog

class RuleReader(initialRulesFile:String) {
  def xdslRulesString = trap.file.Util.readTextFileToString(initialRulesFile)
}