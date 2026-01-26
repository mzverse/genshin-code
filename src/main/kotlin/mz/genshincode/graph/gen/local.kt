@file:Suppress("unused")

package mz.genshincode.graph.gen

import mz.genshincode.GenshinType
import mz.genshincode.graph.GraphNodes

@JvmName("localBoolean")
context(context: StatementGenerator)
fun local(init: Expr<Boolean>) =
    local(init, GraphNodes.Server.Query.Local.getBoolean()) { GraphNodes.Server.Exec.Local.setBoolean() }
@JvmName("localInt")
context(context: StatementGenerator)
fun local(init: Expr<Int>) =
    local(init, GraphNodes.Server.Query.Local.getInt()) { GraphNodes.Server.Exec.Local.setInt() }
@JvmName("localString")
context(context: StatementGenerator)
fun local(init: Expr<String>) =
    local(init, GraphNodes.Server.Query.Local.getString()) { GraphNodes.Server.Exec.Local.setString() }
@JvmName("localEntity")
context(context: StatementGenerator)
fun local(init: Expr<GenshinType.Entity>) =
    local(init, GraphNodes.Server.Query.Local.getEntity()) { GraphNodes.Server.Exec.Local.setEntity() }
@JvmName("localGuid")
context(context: StatementGenerator)
fun local(init: Expr<GenshinType.Guid>) =
    local(init, GraphNodes.Server.Query.Local.getGuid()) { GraphNodes.Server.Exec.Local.setGuid() }
@JvmName("localFloat")
context(context: StatementGenerator)
fun local(init: Expr<Float>) =
    local(init, GraphNodes.Server.Query.Local.getFloat()) { GraphNodes.Server.Exec.Local.setFloat() }
@JvmName("localVector")
context(context: StatementGenerator)
fun local(init: Expr<GenshinType.Vector>) =
    local(init, GraphNodes.Server.Query.Local.getVector()) { GraphNodes.Server.Exec.Local.setVector() }
@JvmName("localIntList")
context(context: StatementGenerator)
fun local(init: Expr<List<Int>>) =
    local(init, GraphNodes.Server.Query.Local.getIntList()) { GraphNodes.Server.Exec.Local.setIntList() }
@JvmName("localStringList")
context(context: StatementGenerator)
fun local(init: Expr<List<String>>) =
    local(init, GraphNodes.Server.Query.Local.getStringList()) { GraphNodes.Server.Exec.Local.setStringList() }
@JvmName("localEntityList")
context(context: StatementGenerator)
fun local(init: Expr<List<GenshinType.Entity>>) =
    local(init, GraphNodes.Server.Query.Local.getEntityList()) { GraphNodes.Server.Exec.Local.setEntityList() }
@JvmName("localGuidList")
context(context: StatementGenerator)
fun local(init: Expr<List<GenshinType.Guid>>) =
    local(init, GraphNodes.Server.Query.Local.getGuidList()) { GraphNodes.Server.Exec.Local.setGuidList() }
@JvmName("localFloatList")
context(context: StatementGenerator)
fun local(init: Expr<List<Float>>) =
    local(init, GraphNodes.Server.Query.Local.getFloatList()) { GraphNodes.Server.Exec.Local.setFloatList() }
@JvmName("localVectorList")
context(context: StatementGenerator)
fun local(init: Expr<List<GenshinType.Vector>>) =
    local(init, GraphNodes.Server.Query.Local.getVectorList()) { GraphNodes.Server.Exec.Local.setVectorList() }
@JvmName("localBooleanList")
context(context: StatementGenerator)
fun local(init: Expr<List<Boolean>>) =
    local(init, GraphNodes.Server.Query.Local.getBooleanList()) { GraphNodes.Server.Exec.Local.setBooleanList() }
@JvmName("localConfig")
context(context: StatementGenerator)
fun local(init: Expr<GenshinType.Config>) =
    local(init, GraphNodes.Server.Query.Local.getConfig()) { GraphNodes.Server.Exec.Local.setConfig() }
@JvmName("localPrefab")
context(context: StatementGenerator)
fun local(init: Expr<GenshinType.Prefab>) =
    local(init, GraphNodes.Server.Query.Local.getPrefab()) { GraphNodes.Server.Exec.Local.setPrefab() }
@JvmName("localConfigList")
context(context: StatementGenerator)
fun local(init: Expr<List<GenshinType.Config>>) =
    local(init, GraphNodes.Server.Query.Local.getConfigList()) { GraphNodes.Server.Exec.Local.setConfigList() }
@JvmName("localPrefabList")
context(context: StatementGenerator)
fun local(init: Expr<List<GenshinType.Prefab>>) =
    local(init, GraphNodes.Server.Query.Local.getPrefabList()) { GraphNodes.Server.Exec.Local.setPrefabList() }
@JvmName("localFaction")
context(context: StatementGenerator)
fun local(init: Expr<GenshinType.Faction>) =
    local(init, GraphNodes.Server.Query.Local.getFaction()) { GraphNodes.Server.Exec.Local.setFaction() }
@JvmName("localFactionList")
context(context: StatementGenerator)
fun local(init: Expr<List<GenshinType.Faction>>) =
    local(init, GraphNodes.Server.Query.Local.getFactionList()) { GraphNodes.Server.Exec.Local.setFactionList() }

context(context: StatementGenerator)
private fun <T> local(
    init: Expr<T>,
    node: GraphNodes.Server.Query.Local.Node<T>,
    setter: (StatementGenerator) -> GraphNodes.Statement2_0<GenshinType.Server.Local, T>
): Local<T> {
    init.apply(node.inInit)
    context.addNode(node)
    return Local(node, setter)//.apply { set(get()) }
}

data class Local<T>(
    internal val node: GraphNodes.Server.Query.Local.Node<T>,
    internal val setter: context(StatementGenerator)() -> GraphNodes.Statement2_0<GenshinType.Server.Local, T>
) {
    context(context: StatementGenerator)
    fun get(): Expr<T> = ExprPin(node.outValue)
    context(context: StatementGenerator)
    fun set(value: Expr<T>) =
        context.append(Statement(setter().also {
            node.outLocal.connect(it.in0)
            value.apply(it.in1)
        }))
}