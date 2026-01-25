@file:Suppress("FunctionName")

package mz.genshincode.graph.gen

import mz.genshincode.graph.GraphNodes
import kotlin.apply

fun StatementGenerator.If(con: Expr<Boolean>, then: StatementGenerator.() -> Unit): IfContext {
    val node = GraphNodes.Server.Control.If()
    con.apply(node.inCondition)
    val statement = StatementGenerator().apply(then).join()
    node.flowThen.connectAll(statement.flowsIn)
    this.append(Statement(statement.nodes + node, listOf(node.flowIn)))
    return IfContext(node)
}

data class IfContext(val node: GraphNodes.Server.Control.NodeIf) {
    context(context: StatementGenerator)
    infix fun Else(then: StatementGenerator.() -> Unit) {
        val statement = StatementGenerator().apply(then).statements.join()
        node.flowElse.connectAll(statement.flowsIn)
        context.append(Statement(statement.nodes, emptyList()))
    }
}

/**
 * @param begin including
 * @param end including
 */
fun StatementGenerator.ForInt(begin: Expr<Int>, end: Expr<Int>, body: context(LoopContext)StatementGenerator.(Expr<Int>) -> Unit) {
    val node = GraphNodes.Server.Control.ForInt()
    begin.apply(node.inBegin)
    end.apply(node.inEnd)
    val statement = StatementGenerator().apply{ LoopContext(node).apply { body(ExprPin(node.outValue)) } }.statements.join()
    node.flowBody.connectAll(statement.flowsIn)
    this.append(Statement(statement.nodes + node, listOf(node.flowIn)))
}

data class LoopContext(internal val node: GraphNodes.Server.Control.NodeForInt)

context(context: LoopContext)
val StatementGenerator.loop
    get() = context

fun StatementGenerator.Break(context: LoopContext) {
    val nodeBreak = GraphNodes.Server.Control.Break()
    nodeBreak.flowOut.connect(context.node.flowBreak)
    append(Statement(setOf(nodeBreak), listOf(nodeBreak.flowIn)))
}

context(context: LoopContext)
fun StatementGenerator.Break() {
    Break(loop)
}