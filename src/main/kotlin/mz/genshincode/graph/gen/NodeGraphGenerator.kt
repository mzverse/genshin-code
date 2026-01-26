@file:Suppress("unused")

package mz.genshincode.graph.gen

import mz.genshincode.data.asset.AssetsGenerator
import mz.genshincode.graph.GraphNode
import mz.genshincode.graph.GraphNodes
import mz.genshincode.graph.NodeGraph

fun AssetsGenerator.graph(configuration: context(NodeGraphGenerator)() -> Unit) {
    val result = NodeGraph(configuration)
    result.autoLayout()
    result.generateAssets(this)
}

fun NodeGraph(configuration: context(NodeGraphGenerator)() -> Unit) =
    NodeGraphGenerator().apply(configuration).graph

class NodeGraphGenerator {
    val graph = NodeGraph()
    fun addNode(value: GraphNode) = graph.addNode(value)
    fun addNodes(values: Set<GraphNode>) = graph.addNodes(values)
    fun add(value: Statement) = addNodes(value.nodes)
}

fun Statement(configuration: context(StatementGenerator)() -> Unit) =
    StatementGenerator().apply(configuration).statement

class StatementGenerator {
    var statement: Statement = Statement.EMPTY
    private var parallel = true
    fun fork(value: Statement) {
        statement = statement and value
    }
    fun append(value: Statement) {
        if(parallel) {
            val last = statement
            statement = value
            parallel = false
            fork(last)
        } else
            statement += value
    }
    fun addNodes(values: Set<GraphNode>) {
        fork(Statement(values))
    }
    fun addNode(value: GraphNode) =
        addNodes(setOf(value))
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

data class Statement(
    val nodes: Set<GraphNode> = HashSet(),
    val flowsIn: List<GraphNode.Pin<Void>>,
    val flowsOut: Set<GraphNode.Pin<Void>>
) {
    companion object {
        val EMPTY = Statement(emptySet())
    }

    constructor(node: GraphNode, flowIn: GraphNode.Pin<Void>, flowOut: GraphNode.Pin<Void>): this(setOf(node), listOf(flowIn), setOf(flowOut))
    constructor(node: GraphNodes.Statement0_0): this(node, node.flowIn, node.flowOut)
    constructor(node: GraphNodes.Trigger0): this(setOf(node), emptyList(), setOf(node.flow))
    constructor(nodes: Set<GraphNode>): this(nodes, emptyList(), emptySet())

    infix fun and(that: Statement) =
        Statement(nodes + that.nodes, flowsIn + that.flowsIn, flowsOut + that.flowsOut)
    operator fun plus(that: Statement): Statement {
        if(this == EMPTY)
            return that
        if(that == EMPTY)
            return this
        flowsOut.forEach { it.connectAll(that.flowsIn) }
        return Statement(nodes + that.nodes, flowsIn, that.flowsOut)
    }
}
