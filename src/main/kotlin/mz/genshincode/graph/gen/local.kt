@file:Suppress("unused")

package mz.genshincode.graph.gen

import mz.genshincode.GenshinType
import mz.genshincode.graph.GraphNodes

@JvmName("localBool")
fun StatementGenerator.local(init: Expr<Boolean>) =
    local(init, GraphNodes.Server.Query.Local.getBool()) { GraphNodes.Server.Exec.Local.setBool() }
@JvmName("localInt")
fun StatementGenerator.local(init: Expr<Int>) =
    local(init, GraphNodes.Server.Query.Local.getInt()) { GraphNodes.Server.Exec.Local.setInt() }
@JvmName("localString")
fun StatementGenerator.local(init: Expr<String>) =
    local(init, GraphNodes.Server.Query.Local.getString()) { GraphNodes.Server.Exec.Local.setString() }
@JvmName("localEntity")
fun StatementGenerator.local(init: Expr<GenshinType.Entity>) =
    local(init, GraphNodes.Server.Query.Local.getEntity()) { GraphNodes.Server.Exec.Local.setEntity() }
@JvmName("localGuid")
fun StatementGenerator.local(init: Expr<GenshinType.Guid>) =
    local(init, GraphNodes.Server.Query.Local.getGuid()) { GraphNodes.Server.Exec.Local.setGuid() }
@JvmName("localFloat")
fun StatementGenerator.local(init: Expr<Float>) =
    local(init, GraphNodes.Server.Query.Local.getFloat()) { GraphNodes.Server.Exec.Local.setFloat() }
@JvmName("localVector")
fun StatementGenerator.local(init: Expr<GenshinType.Vector>) =
    local(init, GraphNodes.Server.Query.Local.getVector()) { GraphNodes.Server.Exec.Local.setVector() }
@JvmName("localIntList")
fun StatementGenerator.local(init: Expr<List<Int>>) =
    local(init, GraphNodes.Server.Query.Local.getIntList()) { GraphNodes.Server.Exec.Local.setIntList() }
@JvmName("localStringList")
fun StatementGenerator.local(init: Expr<List<String>>) =
    local(init, GraphNodes.Server.Query.Local.getStringList()) { GraphNodes.Server.Exec.Local.setStringList() }
@JvmName("localEntityList")
fun StatementGenerator.local(init: Expr<List<GenshinType.Entity>>) =
    local(init, GraphNodes.Server.Query.Local.getEntityList()) { GraphNodes.Server.Exec.Local.setEntityList() }
@JvmName("localGuidList")
fun StatementGenerator.local(init: Expr<List<GenshinType.Guid>>) =
    local(init, GraphNodes.Server.Query.Local.getGuidList()) { GraphNodes.Server.Exec.Local.setGuidList() }
@JvmName("localFloatList")
fun StatementGenerator.local(init: Expr<List<Float>>) =
    local(init, GraphNodes.Server.Query.Local.getFloatList()) { GraphNodes.Server.Exec.Local.setFloatList() }
@JvmName("localVectorList")
fun StatementGenerator.local(init: Expr<List<GenshinType.Vector>>) =
    local(init, GraphNodes.Server.Query.Local.getVectorList()) { GraphNodes.Server.Exec.Local.setVectorList() }
@JvmName("localBoolList")
fun StatementGenerator.local(init: Expr<List<Boolean>>) =
    local(init, GraphNodes.Server.Query.Local.getBoolList()) { GraphNodes.Server.Exec.Local.setBoolList() }
@JvmName("localConfig")
fun StatementGenerator.local(init: Expr<GenshinType.Config>) =
    local(init, GraphNodes.Server.Query.Local.getConfig()) { GraphNodes.Server.Exec.Local.setConfig() }
@JvmName("localPrefab")
fun StatementGenerator.local(init: Expr<GenshinType.Prefab>) =
    local(init, GraphNodes.Server.Query.Local.getPrefab()) { GraphNodes.Server.Exec.Local.setPrefab() }
@JvmName("localConfigList")
fun StatementGenerator.local(init: Expr<List<GenshinType.Config>>) =
    local(init, GraphNodes.Server.Query.Local.getConfigList()) { GraphNodes.Server.Exec.Local.setConfigList() }
@JvmName("localPrefabList")
fun StatementGenerator.local(init: Expr<List<GenshinType.Prefab>>) =
    local(init, GraphNodes.Server.Query.Local.getPrefabList()) { GraphNodes.Server.Exec.Local.setPrefabList() }
@JvmName("localFaction")
fun StatementGenerator.local(init: Expr<GenshinType.Faction>) =
    local(init, GraphNodes.Server.Query.Local.getFaction()) { GraphNodes.Server.Exec.Local.setFaction() }
@JvmName("localFactionList")
fun StatementGenerator.local(init: Expr<List<GenshinType.Faction>>) =
    local(init, GraphNodes.Server.Query.Local.getFactionList()) { GraphNodes.Server.Exec.Local.setFactionList() }

private fun <T> StatementGenerator.local(
    init: Expr<T>,
    node: GraphNodes.Server.Query.Local.Node<T>,
    setter: (StatementGenerator) -> GraphNodes.Statement2_0<GenshinType.Server.Local, T>
): Local<T> {
    init.apply(node.inInit)
    addNode(node)
    return Local(node, setter)//.apply { set(get()) }
}

data class Local<T>(
    internal val node: GraphNodes.Server.Query.Local.Node<T>,
    internal val setter: StatementGenerator.() -> GraphNodes.Statement2_0<GenshinType.Server.Local, T>
) {
    context(context: StatementGenerator)
    fun get(): Expr<T> = ExprPin(node.outValue)
    context(context: StatementGenerator)
    fun set(value: Expr<T>) {
        context.append(Statement(context.setter().also {
            node.outLocal.connect(it.in0)
            value.apply(it.in1)
        }))
    }
}