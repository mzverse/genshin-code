@file:Suppress("unused")

package mz.genshincode.graph.gen

import mz.genshincode.Config
import mz.genshincode.Entity
import mz.genshincode.Faction
import mz.genshincode.GenshinDsl
import mz.genshincode.GenshinType
import mz.genshincode.Guid
import mz.genshincode.Prefab
import mz.genshincode.ServerLocal
import mz.genshincode.Vector
import mz.genshincode.graph.GraphNodes
import mz.genshincode.graph.NodeGraphType

@GenshinDsl
@Suppress("UNCHECKED_CAST")
context(context: FragmentGenerator, serverTag: NodeGraphType.SERVER)
fun <T> Local(init: Expr<T>): Local<T> =
    when(init.type) {
        GenshinType.BOOLEAN ->
            Local(init as Expr<Boolean>, GraphNodes.Server.Query.Local.getBoolean()) { GraphNodes.Server.Exec.Local.setBoolean() }
        GenshinType.INT ->
            Local(init as Expr<Int>, GraphNodes.Server.Query.Local.getInt()) { GraphNodes.Server.Exec.Local.setInt() }
        GenshinType.FLOAT ->
            Local(init as Expr<Float>, GraphNodes.Server.Query.Local.getFloat()) { GraphNodes.Server.Exec.Local.setFloat() }
        GenshinType.STRING ->
            Local(init as Expr<String>, GraphNodes.Server.Query.Local.getString()) { GraphNodes.Server.Exec.Local.setString() }
        GenshinType.ENTITY ->
            Local(init as Expr<Entity>, GraphNodes.Server.Query.Local.getEntity()) { GraphNodes.Server.Exec.Local.setEntity() }
        GenshinType.VECTOR ->
            Local(init as Expr<Vector>, GraphNodes.Server.Query.Local.getVector()) { GraphNodes.Server.Exec.Local.setVector() }
        GenshinType.GUID ->
            Local(init as Expr<Guid>, GraphNodes.Server.Query.Local.getGuid()) { GraphNodes.Server.Exec.Local.setGuid() }
        GenshinType.CONFIG ->
            Local(init as Expr<Config>, GraphNodes.Server.Query.Local.getConfig()) { GraphNodes.Server.Exec.Local.setConfig() }
        GenshinType.PREFAB ->
            Local(init as Expr<Prefab>, GraphNodes.Server.Query.Local.getPrefab()) { GraphNodes.Server.Exec.Local.setPrefab() }
        GenshinType.FACTION ->
            Local(init as Expr<Faction>, GraphNodes.Server.Query.Local.getFaction()) { GraphNodes.Server.Exec.Local.setFaction() }
        GenshinType.INT_LIST ->
            Local(init as Expr<List<Int>>, GraphNodes.Server.Query.Local.getIntList()) { GraphNodes.Server.Exec.Local.setIntList() }
        GenshinType.FLOAT_LIST ->
            Local(init as Expr<List<Float>>, GraphNodes.Server.Query.Local.getFloatList()) { GraphNodes.Server.Exec.Local.setFloatList() }
        GenshinType.STRING_LIST ->
            Local(init as Expr<List<String>>, GraphNodes.Server.Query.Local.getStringList()) { GraphNodes.Server.Exec.Local.setStringList() }
        GenshinType.BOOLEAN_LIST ->
            Local(init as Expr<List<Boolean>>, GraphNodes.Server.Query.Local.getBooleanList()) { GraphNodes.Server.Exec.Local.setBooleanList() }
        GenshinType.ENTITY_LIST ->
            Local(init as Expr<List<Entity>>, GraphNodes.Server.Query.Local.getEntityList()) { GraphNodes.Server.Exec.Local.setEntityList() }
        GenshinType.VECTOR_LIST ->
            Local(init as Expr<List<Vector>>, GraphNodes.Server.Query.Local.getVectorList()) { GraphNodes.Server.Exec.Local.setVectorList() }
        GenshinType.GUID_LIST ->
            Local(init as Expr<List<Guid>>, GraphNodes.Server.Query.Local.getGuidList()) { GraphNodes.Server.Exec.Local.setGuidList() }
        GenshinType.CONFIG_LIST ->
            Local(init as Expr<List<Config>>, GraphNodes.Server.Query.Local.getConfigList()) { GraphNodes.Server.Exec.Local.setConfigList() }
        GenshinType.Server.PREFAB_LIST ->
            Local(init as Expr<List<Prefab>>, GraphNodes.Server.Query.Local.getPrefabList()) { GraphNodes.Server.Exec.Local.setPrefabList() }
        GenshinType.Server.FACTION_LIST ->
            Local(init as Expr<List<Faction>>, GraphNodes.Server.Query.Local.getFactionList()) { GraphNodes.Server.Exec.Local.setFactionList() }
        else ->
            throw IllegalArgumentException()
    } as Local<T>

context(context: FragmentGenerator, serverTag: NodeGraphType.SERVER)
private fun <T> Local(
    init: Expr<T>,
    node: GraphNodes.Server.Query.Local.Node<T>,
    setter: (FragmentGenerator) -> GraphNodes.Statement2_0<ServerLocal, T>
): Local<T> {
    init.apply(node.inInit)
    context.addNode(node)
    return LocalServer(node, setter)
}

sealed interface Local<T> {
    context(context: FragmentGenerator)
    fun get(): Expr<T>
    context(context: FragmentGenerator)
    fun set(value: Expr<T>)
}

class LocalServer<T> internal constructor(
    val node: GraphNodes.Server.Query.Local.Node<T>,
    val setter: context(FragmentGenerator)() -> GraphNodes.Statement2_0<ServerLocal, T>
): Local<T> {
    context(context: FragmentGenerator)
    override fun get(): Expr<T> = ExprPin(node.outValue)
    context(context: FragmentGenerator)
    override fun set(value: Expr<T>) =
        context.append(Statement(setter().also {
            node.outLocal.connect(it.in0)
            value.apply(it.in1)
        }))
}