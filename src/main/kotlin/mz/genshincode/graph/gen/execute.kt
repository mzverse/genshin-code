@file:Suppress("unused")

package mz.genshincode.graph.gen

import mz.genshincode.Entity
import mz.genshincode.Guid
import mz.genshincode.graph.GraphNodes

// TODO
@JvmName("logInt")
context(context: FragmentGenerator)
fun log(message: Expr<Int>) =
    log(message.asString())
@JvmName("logEntity")
context(context: FragmentGenerator)
fun log(message: Expr<Entity>) =
    log(message.asString())
@JvmName("logGuid")
context(context: FragmentGenerator)
fun log(message: Expr<Guid>) =
    log(message.asString())

context(context: FragmentGenerator)
fun log(message: String) =
    log(const(message))
context(context: FragmentGenerator)
fun log(message: Expr<String>) =
    context.append(Statement(GraphNodes.Server.Exec.print().also {
        message.apply(it.in0)
    }))