@file:Suppress("unused")

package mz.genshincode.graph.gen

import mz.genshincode.Config
import mz.genshincode.Entity
import mz.genshincode.Faction
import mz.genshincode.GenshinType
import mz.genshincode.Guid
import mz.genshincode.Prefab
import mz.genshincode.ServerLocal
import mz.genshincode.Vector
import mz.genshincode.graph.GraphNodes

@Suppress("UNCHECKED_CAST")
context(context: FragmentGenerator)
fun <T> local(init: Expr<T>): Local<T> =
    when(init.type) {
        GenshinType.BOOLEAN ->
            local(init as Expr<Boolean>, GraphNodes.Server.Query.Local.getBoolean()) { GraphNodes.Server.Exec.Local.setBoolean() }
        GenshinType.INT ->
            local(init as Expr<Int>, GraphNodes.Server.Query.Local.getInt()) { GraphNodes.Server.Exec.Local.setInt() }
        GenshinType.FLOAT ->
            local(init as Expr<Float>, GraphNodes.Server.Query.Local.getFloat()) { GraphNodes.Server.Exec.Local.setFloat() }
        GenshinType.STRING ->
            local(init as Expr<String>, GraphNodes.Server.Query.Local.getString()) { GraphNodes.Server.Exec.Local.setString() }
        GenshinType.ENTITY ->
            local(init as Expr<Entity>, GraphNodes.Server.Query.Local.getEntity()) { GraphNodes.Server.Exec.Local.setEntity() }
        GenshinType.VECTOR ->
            local(init as Expr<Vector>, GraphNodes.Server.Query.Local.getVector()) { GraphNodes.Server.Exec.Local.setVector() }
        GenshinType.GUID ->
            local(init as Expr<Guid>, GraphNodes.Server.Query.Local.getGuid()) { GraphNodes.Server.Exec.Local.setGuid() }
        GenshinType.CONFIG ->
            local(init as Expr<Config>, GraphNodes.Server.Query.Local.getConfig()) { GraphNodes.Server.Exec.Local.setConfig() }
        GenshinType.PREFAB ->
            local(init as Expr<Prefab>, GraphNodes.Server.Query.Local.getPrefab()) { GraphNodes.Server.Exec.Local.setPrefab() }
        GenshinType.FACTION ->
            local(init as Expr<Faction>, GraphNodes.Server.Query.Local.getFaction()) { GraphNodes.Server.Exec.Local.setFaction() }
        GenshinType.INT_LIST ->
            local(init as Expr<List<Int>>, GraphNodes.Server.Query.Local.getIntList()) { GraphNodes.Server.Exec.Local.setIntList() }
        GenshinType.FLOAT_LIST ->
            local(init as Expr<List<Float>>, GraphNodes.Server.Query.Local.getFloatList()) { GraphNodes.Server.Exec.Local.setFloatList() }
        GenshinType.STRING_LIST ->
            local(init as Expr<List<String>>, GraphNodes.Server.Query.Local.getStringList()) { GraphNodes.Server.Exec.Local.setStringList() }
        GenshinType.BOOLEAN_LIST ->
            local(init as Expr<List<Boolean>>, GraphNodes.Server.Query.Local.getBooleanList()) { GraphNodes.Server.Exec.Local.setBooleanList() }
        GenshinType.ENTITY_LIST ->
            local(init as Expr<List<Entity>>, GraphNodes.Server.Query.Local.getEntityList()) { GraphNodes.Server.Exec.Local.setEntityList() }
        GenshinType.VECTOR_LIST ->
            local(init as Expr<List<Vector>>, GraphNodes.Server.Query.Local.getVectorList()) { GraphNodes.Server.Exec.Local.setVectorList() }
        GenshinType.GUID_LIST ->
            local(init as Expr<List<Guid>>, GraphNodes.Server.Query.Local.getGuidList()) { GraphNodes.Server.Exec.Local.setGuidList() }
        GenshinType.CONFIG_LIST ->
            local(init as Expr<List<Config>>, GraphNodes.Server.Query.Local.getConfigList()) { GraphNodes.Server.Exec.Local.setConfigList() }
        GenshinType.Server.PREFAB_LIST ->
            local(init as Expr<List<Prefab>>, GraphNodes.Server.Query.Local.getPrefabList()) { GraphNodes.Server.Exec.Local.setPrefabList() }
        GenshinType.Server.FACTION_LIST ->
            local(init as Expr<List<Faction>>, GraphNodes.Server.Query.Local.getFactionList()) { GraphNodes.Server.Exec.Local.setFactionList() }
        else ->
            throw IllegalArgumentException()
    } as Local<T>

context(context: FragmentGenerator)
private fun <T> local(
    init: Expr<T>,
    node: GraphNodes.Server.Query.Local.Node<T>,
    setter: (FragmentGenerator) -> GraphNodes.Statement2_0<ServerLocal, T>
): Local<T> {
    init.apply(node.inInit)
    context.addNode(node)
    return Local(node, setter)
}

data class Local<T>(
    val node: GraphNodes.Server.Query.Local.Node<T>,
    val setter: context(FragmentGenerator)() -> GraphNodes.Statement2_0<ServerLocal, T>
) {
    context(context: FragmentGenerator)
    fun get(): Expr<T> = ExprPin(node.outValue)
    context(context: FragmentGenerator)
    fun set(value: Expr<T>) =
        context.append(Statement(setter().also {
            node.outLocal.connect(it.in0)
            value.apply(it.in1)
        }))
}