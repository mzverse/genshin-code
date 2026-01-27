@file:Suppress("unused")

package mz.genshincode.graph.gen

import mz.genshincode.Config
import mz.genshincode.Entity
import mz.genshincode.Faction
import mz.genshincode.GenshinType
import mz.genshincode.Guid
import mz.genshincode.Prefab
import mz.genshincode.Vector


context(context: FragmentGenerator)
fun guid(id: Long) = const(Guid(id))

context(context: FragmentGenerator)
fun const(value: Boolean): Expr<Boolean> = ExprConst(GenshinType.BOOLEAN, value)
context(context: FragmentGenerator)
fun const(value: Int): Expr<Int> = ExprConst(GenshinType.INT, value)
context(context: FragmentGenerator)
fun const(value: Float): Expr<Float> = ExprConst(GenshinType.FLOAT, value)
context(context: FragmentGenerator)
fun const(value: String): Expr<String> = ExprConst(GenshinType.STRING, value)
context(context: FragmentGenerator)
fun const(value: Entity): Expr<Entity> = ExprConst(GenshinType.ENTITY, value)
context(context: FragmentGenerator)
fun const(value: Vector): Expr<Vector> = ExprConst(GenshinType.VECTOR, value)
context(context: FragmentGenerator)
fun const(value: Guid): Expr<Guid> = ExprConst(GenshinType.GUID, value)
context(context: FragmentGenerator)
fun const(value: Config): Expr<Config> = ExprConst(GenshinType.CONFIG, value)
context(context: FragmentGenerator)
fun const(value: Prefab): Expr<Prefab> = ExprConst(GenshinType.PREFAB, value)
context(context: FragmentGenerator)
fun const(value: Faction): Expr<Faction> = ExprConst(GenshinType.FACTION, value)

@JvmName("constIntList")
context(context: FragmentGenerator)
fun const(value: List<Int>): Expr<List<Int>> = ExprConst(GenshinType.INT_LIST, value)
@JvmName("constFloatList")
context(context: FragmentGenerator)
fun const(value: List<Float>): Expr<List<Float>> = ExprConst(GenshinType.FLOAT_LIST, value)
@JvmName("constStringList")
context(context: FragmentGenerator)
fun const(value: List<String>): Expr<List<String>> = ExprConst(GenshinType.STRING_LIST, value)
@JvmName("constBooleanList")
context(context: FragmentGenerator)
fun const(value: List<Boolean>): Expr<List<Boolean>> = ExprConst(GenshinType.BOOLEAN_LIST, value)
@JvmName("constEntityList")
context(context: FragmentGenerator)
fun const(value: List<Entity>): Expr<List<Entity>> = ExprConst(GenshinType.ENTITY_LIST, value)
@JvmName("constVectorList")
context(context: FragmentGenerator)
fun const(value: List<Vector>): Expr<List<Vector>> = ExprConst(GenshinType.VECTOR_LIST, value)
@JvmName("constGuidList")
context(context: FragmentGenerator)
fun const(value: List<Guid>): Expr<List<Guid>> = ExprConst(GenshinType.GUID_LIST, value)
@JvmName("constConfigList")
context(context: FragmentGenerator)
fun const(value: List<Config>): Expr<List<Config>> = ExprConst(GenshinType.CONFIG_LIST, value)
@JvmName("constPrefabList")
context(context: FragmentGenerator)
fun const(value: List<Prefab>): Expr<List<Prefab>> = ExprConst(GenshinType.Server.PREFAB_LIST, value)
@JvmName("constFactionList")
context(context: FragmentGenerator)
fun const(value: List<Faction>): Expr<List<Faction>> = ExprConst(GenshinType.Server.FACTION_LIST, value)