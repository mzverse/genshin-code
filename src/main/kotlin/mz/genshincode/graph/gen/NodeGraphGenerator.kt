@file:Suppress("unused")

package mz.genshincode.graph.gen

import mz.genshincode.GenshinDsl
import mz.genshincode.data.asset.AssetsGenerator
import mz.genshincode.graph.GraphNode
import mz.genshincode.graph.NodeGraph
import mz.genshincode.graph.NodeGraphType

fun <T: NodeGraphType> NodeGraph(type: T, configuration: context(NodeGraphGenerator, T)() -> Unit) =
    NodeGraphGenerator().apply { type.run { configuration() } }.graph

@GenshinDsl
class NodeGraphGenerator {
    val graph = NodeGraph()
    fun addNode(value: GraphNode) = graph.addNode(value)
    fun addNodes(values: Set<GraphNode>) = graph.addNodes(values)
    fun add(value: Fragment) = addNodes(value.nodes)
}

@GenshinDsl
fun AssetsGenerator.graph(configuration: context(NodeGraphGenerator, NodeGraphType.SERVER)() -> Unit) =
    graph(NodeGraphType.SERVER, configuration)
@GenshinDsl
fun <T: NodeGraphType> AssetsGenerator.graph(type: T, configuration: context(NodeGraphGenerator, T)() -> Unit) {
    val result = NodeGraph(type, configuration)
    result.autoLayout()
    result.generateAssets(this)
}