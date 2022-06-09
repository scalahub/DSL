package dql

import trap.Util
import trap.repl.REPLConsole

object DQLConsole {
  implicit def strToSeqStr(s: String) = List(s)

  def dqlReplCode(inputWithInlineComments: String): Seq[String] = {
    try {
      // added 01 Jan 2019
      //  allow inline comments using //
      //  example run abc // run the file abc
      val input = inputWithInlineComments.split("//", 2)(0).trim
      if (input.startsWith("!")) {
        //if (input.substring(0, 1) == "!") {
        val code = input.trim.substring(1).trim
        "" +: runCommand(code)
      } else {
        if (input.startsWith("//")) Nil
        else {
          val array1 = input.trim.split(" ", 2) // split input using spaces
          val command = array1(0)
          array1.length match {
            case 1 => // one command
              command match {
                case ";" | "" => Nil
                case "help" | "?" =>
                  Seq(
                    "DQL Console",
                    "------------General commands ---------",
                    "help or ?                 : show this screen",
                    "exit or q                 : exit the REPL",
                    "! <command>               : run OS shell command",
                    "run <scriptFile>          : run commands from script file",
                    "----------- Commands to show config ----------",
                    "basis / mappings / defs / rules / facts",
                    "----------- Commands to load config ----------",
                    "load <type> <file>        : load <type> from <file>",
                    "     <type> can be: basis / mappings / rules / facts",
                    "load #<pattern> <file>    : load basis pattern facts from CSV file",
                    "----------- Commands to unload config ----------",
                    "unload <type>             : unload <type>",
                    "     <type> can be: basis / mappings / rules / facts",
                    "reset                     : reset basis, rules and mappings",
                    "----------- Commands to define inline config ----------",
                    "reduced <table_schema>    : define reduced basis pattern",
                    "extended <table_schema>   : define extended basis pattern",
                    "map <mapping>             : define new mapping",
                    "rule <rule>               : add rule for extended basis",
                    "----------- DQL commands ----------",
                    "def <pattern>             : define new pattern",
                    "find <pattern>            : find previously defined pattern",
                    "compile <find query>      : displays generated rules without running query"
                  )
                case "basis" => DQLUtil.getBasis
                case "todo" =>
                  Seq("save results to file", "fix mappings for multiple pattern keys", "load script in console rather than type code")
                case "srules"   => DQLUtil.getRules // for debug. Does not parse rules, just prints them as string, hence the "s"
                case "rules"    => DQLUtil.getParsedRules
                case "mappings" => DQLUtil.getMappings
                case "defs"     => DQLUtil.getDefs
                case "facts"    => DQLUtil.getFactFiles
                case "reset"    => DQLUtil.reset
                case any        => throw new Exception(s"Invalid command [$any]")
              }
            case 2 => // two commands
              val remaining = array1(1)
              command match {
                case "run" =>
                  val commands = trap.file.Util.readTextFileToString(remaining).split("\n").map(_.trim).filterNot(_ == "")
                  commands.flatMap { command =>
                    Seq("-------", "[RUN] " + command) ++ dqlReplCode(command)
                  }.toSeq
                case "load" =>
                  val array2 = remaining.trim.split(" ", 2)
                  if (array2.size == 1) throw new Exception("load takes two parameters")
                  val what = array2(0).trim
                  val file = array2(1).trim
                  what match {
                    case "basis"    => DQLUtil.loadBasis(file)
                    case "rules"    => DQLUtil.loadRules(file)
                    case "mappings" => DQLUtil.loadMappings(file)
                    case "facts" =>
                      DQLUtil.unloadFactFiles
                      DQLUtil.loadFactFile(file)
                    case table if table.startsWith("#") => DQLUtil.loadTable(table.substring(1), file)
                    case any                            => throw new Exception(s"Invalid load command $any")
                  }
                case "unload" =>
                  remaining match {
                    case "basis"    => DQLUtil.unloadBasis
                    case "rules"    => DQLUtil.unloadRules
                    case "mappings" => DQLUtil.unloadMappings
                    case "facts"    => DQLUtil.unloadFactFiles
                    case any        => throw new Exception(s"Invalid load command: Type ? to see valid commands.")
                  }
                  "Ok"
                case "reduced"  => DQLUtil.addReducedBasis(remaining)
                case "extended" => DQLUtil.addExtendedBasis(remaining)
                case "map"      => DQLUtil.addMapping(remaining)
                case "rule"     => DQLUtil.addRule(remaining)
                case "def"      => DQLUtil.addDef(remaining)
                case "find"     => DQLUtil.find(remaining)
                case "compile"  => DQLUtil.compile(remaining)
                case any        => "Invalid command " + any
              }
            case any => throw new Exception(s"Invalid number of commands: $any")
          }
        }
      }
    } catch {
      case any: Any =>
        any.printStackTrace
        s" ** ${any.getClass.getSimpleName}: ${any.getMessage} **"
    }
  }
  val console = new REPLConsole(dqlReplCode, 1000, Seq("exit", "q"), "DQL console")

  private def runCommand(str: String) = {
    val (code, out, err) = Util.runCommand(str.split(' '))
    if (code == 0) {
      out.split("\n").toSeq
    } else throw new Exception(err)

  }
  def main(s: Array[String]): Unit = {
    console.start
    System.exit(0)
  }
  def onDataReceive(data: String): Unit = console.pushString("received " + data)

}
