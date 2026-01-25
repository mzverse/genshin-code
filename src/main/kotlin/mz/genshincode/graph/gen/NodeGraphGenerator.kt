package mz.genshincode.graph.gen

import mz.genshincode.graph.GraphNode
import mz.genshincode.graph.GraphNodes
import mz.genshincode.graph.NodeGraph

fun NodeGraph(configuration: NodeGraphGenerator.() -> Unit) =
    NodeGraphGenerator().apply(configuration).graph

class NodeGraphGenerator {
    val graph = NodeGraph()
    val chains: MutableMap<Trigger, List<Statement>> = HashMap()
}

class StatementGenerator {
    val statements: MutableList<Statement> = ArrayList()
    fun append(statement: Statement) {
        this.statements.add(statement)
    }
    fun join() = statements.join()
}

sealed interface Expr<T> {
    fun apply(pin: GraphNode.Pin<T>) = when(this) {
        is ExprPin ->
            pin.connect(this.pin)
        is ExprConst ->
            pin.setValue(this.value)
    }
}
data class ExprPin<T>(val pin: GraphNode.Pin<T>): Expr<T>
data class ExprConst<T>(val value: T): Expr<T>

data class Trigger(
    val nodes: Set<GraphNode> = HashSet(),
    val flow: GraphNode.Pin<Void>,
) {
    constructor(node: GraphNodes.Trigger0): this(setOf(node), node.flow)
}

data class Statement(
    val nodes: Set<GraphNode> = HashSet(),
    val flowsIn: List<GraphNode.Pin<Void>>,
) {
    companion object {
        val EMPTY = Statement(emptySet(), emptyList())
    }
    constructor(node: GraphNodes.Statement0_0): this(setOf(node), listOf(node.flowIn))
    constructor(nodes: Set<GraphNode>): this(nodes, emptyList())
    operator fun plus(that: Statement) =
        Statement(nodes + that.nodes, flowsIn + that.flowsIn)
}

fun List<Statement>.join(): Statement =
    fold(Statement.EMPTY) { a, b -> a + b }