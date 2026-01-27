@file:Suppress("unused")

package mz.genshincode.graph.gen

import mz.genshincode.GenshinType
import mz.genshincode.data.asset.AssetsGenerator
import mz.genshincode.graph.GraphNode
import mz.genshincode.graph.GraphNodes
import mz.genshincode.graph.NodeGraph
import mz.genshincode.graph.NodeGraphType

fun AssetsGenerator.graph(configuration: context(NodeGraphGenerator, NodeGraphType.SERVER)() -> Unit) =
    graph(NodeGraphType.SERVER, configuration)
fun <T: NodeGraphType> AssetsGenerator.graph(type: T, configuration: context(NodeGraphGenerator, T)() -> Unit) {
    val result = NodeGraph(type, configuration)
    result.autoLayout()
    result.generateAssets(this)
}

fun <T: NodeGraphType> NodeGraph(type: T, configuration: context(NodeGraphGenerator, T)() -> Unit) =
    NodeGraphGenerator().apply { type.run { configuration() } }.graph

class NodeGraphGenerator {
    val graph = NodeGraph()
    fun addNode(value: GraphNode) = graph.addNode(value)
    fun addNodes(values: Set<GraphNode>) = graph.addNodes(values)
    fun add(value: Fragment) = addNodes(value.nodes)
}

fun Fragment(configuration: context(FragmentGenerator)() -> Unit) =
    FragmentGenerator().apply(configuration).fragment

class FragmentGenerator {
    var fragment: Fragment = Attachment.EMPTY
    fun fork(value: Fragment) {
        fragment = fragment and value
    }
    fun append(value: Fragment) {
        fragment += value
    }
    fun addNodes(values: Set<GraphNode>) {
        fork(Attachment(values))
    }
    fun addNode(value: GraphNode) =
        addNodes(setOf(value))
}

sealed interface Expr<T> {
    val type: GenshinType<T>
    fun apply(pin: GraphNode.Pin<T>) = when(this) {
        is ExprPin ->
            pin.connect(this.pin)
        is ExprConst ->
            pin.setValue(this.value)
    }
}
data class ExprPin<T>(val pin: GraphNode.Pin<T>): Expr<T> {
    override val type: GenshinType<T>
        get() = pin.type.get()
}
data class ExprConst<T>(override val type: GenshinType<T>, val value: T): Expr<T>

sealed class Fragment(open val nodes: Set<GraphNode>)
data class Attachment(override val nodes: Set<GraphNode>): Fragment(nodes) {
    companion object {
        val EMPTY = Attachment(emptySet())
    }
}
data class Statement(
    override val nodes: Set<GraphNode>,
    val flowsIn: List<GraphNode.Pin<Void>>,
    val flowsOut: Set<GraphNode.Pin<Void>>
): Fragment(nodes) {
    companion object {
        val EMPTY = Statement(emptySet(), emptyList(), emptySet())
    }
    constructor(node: GraphNode, flowIn: GraphNode.Pin<Void>, flowOut: GraphNode.Pin<Void>): this(setOf(node), listOf(flowIn), setOf(flowOut))
    constructor(node: GraphNodes.Statement0_0): this(node, node.flowIn, node.flowOut)
    constructor(node: GraphNodes.Trigger0): this(setOf(node), emptyList(), setOf(node.flow))
}
infix fun Fragment.and(that: Fragment): Fragment = when(this) {
    is Attachment ->
        when(that) {
            is Attachment ->
                Attachment(nodes + that.nodes)
            is Statement ->
                Statement(nodes + that.nodes, that.flowsIn, that.flowsOut)
        }
    is Statement ->
        when(that) {
            is Attachment ->
                that and this
            is Statement ->
                Statement(nodes + that.nodes, flowsIn + that.flowsIn, flowsOut + that.flowsOut)
        }
}
operator fun Fragment.plus(that: Fragment): Fragment =
    if(this is Statement && that is Statement) {
        flowsOut.forEach { it.connectAll(that.flowsIn) }
        Statement(nodes + that.nodes, flowsIn, that.flowsOut)
    }
    else
        this and that