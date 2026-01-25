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
    operator fun plus(supplier: StatementSupplier) {
        this.append(supplier(this))
    }
}

sealed interface Expr<T> {
    val nodes: Set<GraphNode>

    fun connect(pin: GraphNode.Pin<T>) = when(this) {
        is ExprNodes ->
            pin.connect(this.pin)
        is ExprConst ->
            pin.setValue(this.value)
    }
}
data class ExprNodes<T>(override val nodes: Set<GraphNode>, val pin: GraphNode.Pin<T>): Expr<T> {
    constructor(pin: GraphNode.Pin<T>): this(setOf(pin.node), pin)
    constructor(node: GraphNodes.Expr0<T>): this(node.out)
}
data class ExprConst<T>(val value: T): Expr<T> {
    override val nodes: Set<GraphNode>
        get() = emptySet()
}

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
    operator fun plus(that: Statement) =
        Statement(nodes + that.nodes, flowsIn + that.flowsIn)
}
typealias StatementSupplier = (StatementGenerator) -> Statement

fun List<Statement>.join(): Statement =
    fold(Statement.EMPTY) { a, b -> a + b }