@file:Suppress("FunctionName", "PropertyName", "unused")

package mz.genshincode.graph.gen

import mz.genshincode.GenshinDsl
import mz.genshincode.graph.GraphNode
import mz.genshincode.graph.GraphNodes
import mz.genshincode.graph.GraphNodes.Server.Control.NodeSwitch

@GenshinDsl
context(context: FragmentGenerator)
fun If(con: Expr<Boolean>, then: context(FragmentGenerator)() -> Unit): IfContext {
    val node = GraphNodes.Server.Control.If()
    con.apply(node.inCondition)
    context append Statement(node, node.flowIn, node.flowThen)
    then()
    context.fork(Statement(emptySet(), emptyList(), setOf(node.flowElse)))
    return IfContext(node)
}

data class IfContext(val node: GraphNodes.Server.Control.If) {
    @GenshinDsl
    context(context: FragmentGenerator)
    infix fun Else(then: context(FragmentGenerator)() -> Unit) {
        context.fragment = (context.fragment as Statement).let {
            it.copy(flowsOut = HashSet(it.flowsOut).apply {
                assert(remove(node.flowElse))
            })
        }
        context fork Statement(emptySet(), emptyList(), setOf(node.flowElse)) + Fragment(then)
    }
}

@GenshinDsl
@JvmName("SwitchInt")
context(context: FragmentGenerator)
fun Switch(con: Expr<Int>, body: context(FragmentGenerator)SwitchContext<Int>.() -> Unit) =
    Switch(con, body, GraphNodes.Server.Control.SwitchInt())
@GenshinDsl
@JvmName("SwitchString")
context(context: FragmentGenerator)
fun Switch(con: Expr<String>, body: context(FragmentGenerator)SwitchContext<String>.() -> Unit) =
    Switch(con, body, GraphNodes.Server.Control.SwitchString())
@GenshinDsl
context(context: FragmentGenerator)
fun <T> Switch(con: Expr<T>, body: context(FragmentGenerator)SwitchContext<T>.() -> Unit, node: NodeSwitch<T>) {
    context append Statement(setOf(node), listOf(node.flowIn), emptySet())
    con.apply(node.inControlling)
    val cases = ArrayList<T>()
    node.inCases.setValue(cases)
    SwitchContext(node, cases, Label()).apply {
        body()
        finish()
    }
}
class SwitchContext<T>(val node: NodeSwitch<T>, val cases: MutableList<T>, val label: Label) {
    lateinit var def: Unit
    @GenshinDsl
    context(context: FragmentGenerator)
    fun Case(value: T) {
        cases.add(value)
        context fork Statement(emptySet(), emptyList(), setOf(node.addCase()))
    }
    @GenshinDsl
    context(context: FragmentGenerator)
    val Default: Unit get() {
        def = Unit
        context fork Statement(emptySet(), emptyList(), setOf(node.flowDefault))
    }
    @GenshinDsl
    context(context: FragmentGenerator)
    val Break: Unit get() = label.go()
    context(context: FragmentGenerator)
    internal fun finish() {
        if (!::def.isInitialized)
            Default
        label.place()
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
