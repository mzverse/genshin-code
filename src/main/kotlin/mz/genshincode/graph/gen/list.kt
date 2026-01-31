package mz.genshincode.graph.gen

import mz.genshincode.GenshinType
import mz.genshincode.graph.GraphNode
import mz.genshincode.graph.GraphNodes

context(context: FragmentGenerator)
val Expr<List<*>>.size: Expr<Int> get() {
    val node = when (this.type) {
        GenshinType.INT_LIST -> GraphNodes.Server.Query.GetListSize.ofInt()
        GenshinType.STRING_LIST -> GraphNodes.Server.Query.GetListSize.ofString()
        GenshinType.ENTITY_LIST -> GraphNodes.Server.Query.GetListSize.ofEntity()
        GenshinType.GUID_LIST -> GraphNodes.Server.Query.GetListSize.ofGuid()
        GenshinType.FLOAT_LIST -> GraphNodes.Server.Query.GetListSize.ofFloat()
        GenshinType.VECTOR_LIST -> GraphNodes.Server.Query.GetListSize.ofVector()
        GenshinType.BOOLEAN_LIST -> GraphNodes.Server.Query.GetListSize.ofBoolean()
        GenshinType.CONFIG_LIST -> GraphNodes.Server.Query.GetListSize.ofConfig()
        GenshinType.Server.PREFAB_LIST -> GraphNodes.Server.Query.GetListSize.ofPrefab()
        GenshinType.Server.FACTION_LIST -> GraphNodes.Server.Query.GetListSize.ofFaction()
        else -> throw UnsupportedOperationException()
    }
    context.addNode(node)
    @Suppress("UNCHECKED_CAST")
    this.apply(node.inList as GraphNode.Pin<List<*>>)
    return ExprPin(node.outSize)
}
