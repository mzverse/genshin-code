package mz.genshincode.graph.dsl

import mz.genshincode.graph.GraphNode
import mz.genshincode.graph.GraphNodes
import mz.genshincode.graph.NodeGraph

fun NodeGraph(configuration: NodeGraphDsl.() -> Unit) =
    NodeGraphDsl().apply(configuration).graph

class NodeGraphDsl {
    val graph = NodeGraph()
    val chains: MutableMap<Trigger, List<Statement>> = HashMap()

    fun <T> on(eventDef: EventDefinition<T>, configuration: StatementsDsl.(T) -> Unit) {
        val (trigger, event) = eventDef.listen(this)
        val chain = StatementsDsl().apply { configuration(event) }.statements.join()
        trigger.nodes.forEach(graph::addNode)
        if(chain == null)
            return
        chain.nodes.forEach(graph::addNode)
        trigger.flow.connect(chain.flowIn)
    }
}

class StatementsDsl {
    val statements: MutableList<Statement> = ArrayList()
    fun append(statement: Statement) {
        this.statements.add(statement)
    }
}

sealed interface Expr<T> {
    val nodes: Set<GraphNode>
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
    constructor(node: GraphNodes.Trigger0) : this(setOf(node), node.flow)
}

data class Statement(
    val nodes: Set<GraphNode> = HashSet(),
    val flowIn: GraphNode.Pin<Void>,
    val flowOut: GraphNode.Pin<Void>,
)

fun List<Statement>.join(): Statement? = if(isEmpty()) null else run {
    this.zipWithNext { a, b ->
        a.flowOut.connect(b.flowIn)
    }
    return Statement(
        this.asSequence().map(Statement::nodes).flatMap(Set<GraphNode>::asSequence).toSet(),
        this.first().flowIn,
        this.last().flowOut
    )
}

interface EventDefinition<T> {
    fun listen(context: NodeGraphDsl): Pair<Trigger, T>
}