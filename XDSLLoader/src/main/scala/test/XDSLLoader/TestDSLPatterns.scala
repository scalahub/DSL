package test.XDSLLoader

object TestDSLPatterns {
  val invoke="#invoke[userID2](@name:String,@class:String,@interface:String,@static:String,@count:Int)"
  val foo="#foo[userID2](@name:String,@class:String,@interface:String,@static:String,@count:Int,@bar:Boolean)"
  val assign="#assign[userID](@name:String,@type:String,@invoke:String,@static:String,@count:Int)"
  val users="#users[userID](@name:String,@type:String,@age:Int,@static:String,@count:Int)"
  val user="#user[userID2](@age:Int,@type:String,@invoke:String,@static:String,@count:Int)"
//  val dataflow="#dataflow[3,3](@source:Pattern{3},@sink:Pattern{3},@type:String,@count:Int)"
//  val flow="#flow[3,3](@start:Pattern{3},@end:Pattern{3},@type:String,@count:Int)"
//  val loop="#loop[3](@start:Pattern{3},@end:Pattern,@count:Int)"
  
  val lib = Array(invoke, assign, user, users,foo)
//  val lib = Array(invoke, assign, dataflow, flow, loop)
}

