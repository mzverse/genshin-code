@file:Suppress("unused")

package mz.genshincode.graph.gen

import mz.genshincode.GenshinType
import mz.genshincode.graph.GraphNodes

// TODO
@JvmName("logInt")
context(context: StatementGenerator)
fun log(message: Expr<Int>) =
    log(message.asString())
@JvmName("logEntity")
context(context: StatementGenerator)
fun log(message: Expr<GenshinType.Entity>) =
    log(message.asString())
@JvmName("logGuid")
context(context: StatementGenerator)
fun log(message: Expr<GenshinType.Guid>) =
    log(message.asString())

context(context: StatementGenerator)
fun log(message: String) =
    log(const(message))
context(context: StatementGenerator)
fun log(message: Expr<String>) =
    context.append(Statement(GraphNodes.Server.Exec.print().also {
        message.apply(it.in0)
    }))