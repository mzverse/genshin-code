package mz.genshincode.graph.gen

import mz.genshincode.GenshinType
import mz.genshincode.graph.GraphNodes

fun StatementGenerator.log(message: Expr<String>) {
    val node = GraphNodes.Server.Exec.print()
    message.apply(node.in0)
    this.append(Statement(setOf(node), listOf(node.flowIn)))
}
@JvmName("logInt")
fun StatementGenerator.log(message: Expr<Int>) {
    log(message.asString())
}
@JvmName("logEntity")
fun StatementGenerator.log(message: Expr<GenshinType.Entity>) {
    log(message.asString())
}
@JvmName("logGuid")
fun StatementGenerator.log(message: Expr<GenshinType.Guid>) {
    log(message.asString())
}