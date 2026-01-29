@file:Suppress("FunctionName", "unused")

package mz.genshincode.graph.gen

import mz.genshincode.GenshinDsl
import mz.genshincode.graph.GraphNode
import mz.genshincode.graph.GraphNodes

@GenshinDsl
context(context: FragmentGenerator)
fun If(con: Expr<Boolean>, then: context(FragmentGenerator)() -> Unit): IfContext {
    val node = GraphNodes.Server.Control.If()
    con.apply(node.inCondition)
    context.append(Statement(node, node.flowIn, node.flowThen))
    then()
    context.fork(Statement(emptySet(), emptyList(), setOf(node.flowElse))) // connection 2
    return IfContext(node)
}

data class IfContext(val node: GraphNodes.Server.Control.If) {
    @GenshinDsl
    context(context: FragmentGenerator)
    infix fun Else(then: FragmentGenerator.() -> Unit) {
        context.addNodes((Statement(emptySet(), emptyList(), setOf(node.flowElse))
                + Fragment(then)).nodes) // connection 1
    }
}

@GenshinDsl
context(context: FragmentGenerator)
fun While(con: context(FragmentGenerator)() -> Expr<Boolean>, body: context(FragmentGenerator)LoopContext.() -> Unit) =
    Loop {
        If(con()) {
            body()
        } Else {
            Break
        }
    }

@GenshinDsl
context(context: FragmentGenerator)
fun For(con: context(FragmentGenerator)() -> Expr<Boolean>, update: context(FragmentGenerator)() -> Unit, body: context(FragmentGenerator)LoopContext.() -> Unit) =
    While(con) {
        body()
        labelContinue.place()
        update()
    }

@GenshinDsl
context(context: FragmentGenerator)
fun <T> For(i: T, con: context(FragmentGenerator)(T) -> Expr<Boolean>, update: context(FragmentGenerator)(T) -> Unit, body: context(FragmentGenerator)LoopContext.(T) -> Unit) {
    For({ con(i) }, { update(i) }) {
        body(i)
    }
}

@GenshinDsl
context(context: FragmentGenerator)
fun Loop(body: context(FragmentGenerator)LoopContext.() -> Unit) =
    ForClosed(const(Int.MIN_VALUE), const(Int.MAX_VALUE)) { body() }

@GenshinDsl
context(context: FragmentGenerator)
fun For(start: Expr<Int>, end: Expr<Int>, body: context(FragmentGenerator)LoopContext.(Expr<Int>) -> Unit) =
    ForClosed(start, end - const(1), body)

@GenshinDsl
context(context: FragmentGenerator)
fun ForClosed(start: Expr<Int>, end: Expr<Int>, body: context(FragmentGenerator)LoopContext.(Expr<Int>) -> Unit) {
    val node = GraphNodes.Server.Control.ForClosed()
    start.apply(node.inBegin)
    end.apply(node.inEnd)
    context.append(Statement(node, node.flowIn, node.flowBody))
    LoopContext(node, Label()).run {
        body(ExprPin(node.outValue))
    }
    context.append(Statement(emptySet(), emptyList(), setOf(node.flowOut)))
}

data class LoopContext(
    internal val node: GraphNodes.Server.Control.ForClosed,
    internal val labelContinue: Label
)

@GenshinDsl
context(context: FragmentGenerator)
val LoopContext.Break: Unit
    get() {
        val nodeBreak = GraphNodes.Server.Control.Break()
        nodeBreak.flowOut.connect(node.flowBreak)
        context.append(Statement(setOf(nodeBreak), listOf(nodeBreak.flowIn), emptySet()))
    }
@GenshinDsl
context(context: FragmentGenerator)
val LoopContext.Continue: Unit
    get() =
        labelContinue.go()

context(context: FragmentGenerator)
fun Label() = Label(HashSet())
class Label(internal val flowsOut: MutableSet<GraphNode.Pin<Void>>) {
    internal lateinit var loc: Unit

    @GenshinDsl
    context(context: FragmentGenerator)
    fun go() =
        if(::loc.isInitialized)
            throw IllegalStateException("Cannot go() after place()")
        else {
            (context.fragment as? Statement)?.let {
                flowsOut.addAll(it.flowsOut)
            }
            context.append(Statement.EMPTY)
        }
    @GenshinDsl
    context(context: FragmentGenerator)
    fun place() =
        context.fork(Statement(emptySet(), emptyList(), flowsOut)).also {
            loc = Unit
        }
}
