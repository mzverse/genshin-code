package mz.genshincode.graph.gen

import mz.genshincode.GenshinDsl
import mz.genshincode.graph.GraphNode
import mz.genshincode.graph.GraphNodes
import kotlin.collections.forEach
import kotlin.collections.plus


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
    if (this !is Statement || that !is Statement)
        this and that
    else {
        flowsOut.forEach { it.connectAll(that.flowsIn) }
        Statement(nodes + that.nodes, flowsIn, that.flowsOut)
    }

fun Fragment(configuration: context(FragmentGenerator)() -> Unit) =
    FragmentGenerator().apply(configuration).fragment
@GenshinDsl
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