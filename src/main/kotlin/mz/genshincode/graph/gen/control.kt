@file:Suppress("FunctionName", "unused")

package mz.genshincode.graph.gen

import mz.genshincode.graph.GraphNodes
import kotlin.apply

context(context: StatementGenerator)
fun If(con: Expr<Boolean>, then: context(StatementGenerator)() -> Unit): IfContext {
    val node = GraphNodes.Server.Control.If()
    con.apply(node.inCondition)
    context.append(Statement(node, node.flowIn, node.flowThen))
    then()
    context.fork(Statement(emptySet(), emptyList(), setOf(node.flowElse))) // connection 2
    return IfContext(node)
}

data class IfContext(val node: GraphNodes.Server.Control.NodeIf) {
    context(context: StatementGenerator)
    infix fun Else(then: StatementGenerator.() -> Unit) {
        context.addNodes((Statement(emptySet(), emptyList(), setOf(node.flowElse))
                + Statement(then)).nodes) // connection 1
    }
}
context(context: StatementGenerator)
fun While(con: context(StatementGenerator)() -> Expr<Boolean>, body: context(StatementGenerator)LoopContext.() -> Unit) =
    Loop {
        If(con()) {
            body()
        } Else {
            Break
        }
    }

context(context: StatementGenerator)
fun Loop(body: context(StatementGenerator)LoopContext.() -> Unit) =
    ForClosed(const(Int.MIN_VALUE), const(Int.MAX_VALUE)) { body() }

context(context: StatementGenerator)
fun For(start: Expr<Int>, end: Expr<Int>, body: context(StatementGenerator)LoopContext.(Expr<Int>) -> Unit) =
    ForClosed(start, end - const(1), body)

context(context: StatementGenerator)
fun ForClosed(start: Expr<Int>, end: Expr<Int>, body: context(StatementGenerator)LoopContext.(Expr<Int>) -> Unit) {
    val node = GraphNodes.Server.Control.ForInt()
    start.apply(node.inBegin)
    end.apply(node.inEnd)
    context.append(Statement(node, node.flowIn, node.flowBody))
    LoopContext(node).run {
        body(ExprPin(node.outValue))
    }
    context.append(Statement(emptySet(), emptyList(), setOf(node.flowOut)))
}

data class LoopContext(internal val node: GraphNodes.Server.Control.NodeForInt)

context(context: StatementGenerator)
val LoopContext.Break: Unit
    get() {
        val nodeBreak = GraphNodes.Server.Control.Break()
        nodeBreak.flowOut.connect(node.flowBreak)
        context.append(Statement(setOf(nodeBreak), listOf(nodeBreak.flowIn), emptySet()))
    }