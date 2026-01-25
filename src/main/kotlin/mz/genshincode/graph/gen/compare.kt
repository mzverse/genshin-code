package mz.genshincode.graph.gen

import mz.genshincode.GenshinType
import mz.genshincode.graph.GraphNodes

@JvmName("eqString")
infix fun Expr<String>.eq(that: Expr<String>) =
    eq(that, GraphNodes.Server.Calc.IsEqual.ofString())
@JvmName("eqGuid")
infix fun Expr<GenshinType.Guid>.eq(that: Expr<GenshinType.Guid>) =
    eq(that, GraphNodes.Server.Calc.IsEqual.ofGuid())
@JvmName("eqEntity")
infix fun Expr<GenshinType.Entity>.eq(that: Expr<GenshinType.Entity>) =
    eq(that, GraphNodes.Server.Calc.IsEqual.ofEntity())
@JvmName("eqVector")
infix fun Expr<GenshinType.Vector>.eq(that: Expr<GenshinType.Vector>) =
    eq(that, GraphNodes.Server.Calc.IsEqual.ofVector())
@JvmName("eqFaction")
infix fun Expr<GenshinType.Faction>.eq(that: Expr<GenshinType.Faction>) =
    eq(that, GraphNodes.Server.Calc.IsEqual.ofFaction())
@JvmName("eqInt")
infix fun Expr<Int>.eq(that: Expr<Int>) =
    eq(that, GraphNodes.Server.Calc.IsEqual.ofInt())
@JvmName("eqFloat")
infix fun Expr<Float>.eq(that: Expr<Float>) =
    eq(that, GraphNodes.Server.Calc.IsEqual.ofFloat())
@JvmName("eqConfig")
infix fun Expr<GenshinType.Config>.eq(that: Expr<GenshinType.Config>) =
    eq(that, GraphNodes.Server.Calc.IsEqual.ofConfig())
@JvmName("eqPrefab")
infix fun Expr<GenshinType.Prefab>.eq(that: Expr<GenshinType.Prefab>) =
    eq(that, GraphNodes.Server.Calc.IsEqual.ofPrefab())
@JvmName("eqBool")
infix fun Expr<Boolean>.eq(that: Expr<Boolean>) =
    eq(that, GraphNodes.Server.Calc.IsEqual.ofBool())

private fun <T> Expr<T>.eq(that: Expr<T>, node: GraphNodes.Expr2<Boolean, T, T>): Expr<Boolean> {
    this.connect(node.in0)
    that.connect(node.in1)
    return ExprNodes(this.nodes + that.nodes + node, node.out)
}