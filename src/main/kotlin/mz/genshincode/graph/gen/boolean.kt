@file:Suppress("unused")

package mz.genshincode.graph.gen

import mz.genshincode.GenshinType
import mz.genshincode.graph.GraphNodes

context(context: FragmentGenerator)
operator fun Expr<Boolean>.not(): Expr<Boolean> {
    val node = GraphNodes.Server.Calc.Math.not()
    context.addNode(node)
    this.apply(node.in0)
    return ExprPin(node.out)
}

@JvmName("ltInt")
context(context: FragmentGenerator)
infix fun Expr<Int>.lt(that: Expr<Int>) =
    compare(that, GraphNodes.Server.Calc.Math.lessThanInt())
@JvmName("gtInt")
context(context: FragmentGenerator)
infix fun Expr<Int>.gt(that: Expr<Int>) =
    compare(that, GraphNodes.Server.Calc.Math.greaterThanInt())
@JvmName("leInt")
context(context: FragmentGenerator)
infix fun Expr<Int>.le(that: Expr<Int>) =
    compare(that, GraphNodes.Server.Calc.Math.lessThanOrEqualInt())
@JvmName("geInt")
context(context: FragmentGenerator)
infix fun Expr<Int>.ge(that: Expr<Int>) =
    compare(that, GraphNodes.Server.Calc.Math.greaterThanOrEqualInt())

@JvmName("ltFloat")
context(context: FragmentGenerator)
infix fun Expr<Float>.lt(that: Expr<Float>) =
    compare(that, GraphNodes.Server.Calc.Math.lessThanFloat())
@JvmName("gtFloat")
context(context: FragmentGenerator)
infix fun Expr<Float>.gt(that: Expr<Float>) =
    compare(that, GraphNodes.Server.Calc.Math.greaterThanFloat())
@JvmName("leFloat")
context(context: FragmentGenerator)
infix fun Expr<Float>.le(that: Expr<Float>) =
    compare(that, GraphNodes.Server.Calc.Math.lessThanOrEqualFloat())
@JvmName("geFloat")
context(context: FragmentGenerator)
infix fun Expr<Float>.ge(that: Expr<Float>) =
    compare(that, GraphNodes.Server.Calc.Math.greaterThanOrEqualFloat())

@JvmName("eqString")
context(context: FragmentGenerator)
infix fun Expr<String>.eq(that: Expr<String>) =
    compare(that, GraphNodes.Server.Calc.IsEqual.ofString())
@JvmName("eqGuid")
context(context: FragmentGenerator)
infix fun Expr<GenshinType.Guid>.eq(that: Expr<GenshinType.Guid>) =
    compare(that, GraphNodes.Server.Calc.IsEqual.ofGuid())
@JvmName("eqEntity")
context(context: FragmentGenerator)
infix fun Expr<GenshinType.Entity>.eq(that: Expr<GenshinType.Entity>) =
    compare(that, GraphNodes.Server.Calc.IsEqual.ofEntity())
@JvmName("eqVector")
context(context: FragmentGenerator)
infix fun Expr<GenshinType.Vector>.eq(that: Expr<GenshinType.Vector>) =
    compare(that, GraphNodes.Server.Calc.IsEqual.ofVector())
@JvmName("eqFaction")
context(context: FragmentGenerator)
infix fun Expr<GenshinType.Faction>.eq(that: Expr<GenshinType.Faction>) =
    compare(that, GraphNodes.Server.Calc.IsEqual.ofFaction())
@JvmName("eqInt")
context(context: FragmentGenerator)
infix fun Expr<Int>.eq(that: Expr<Int>) =
    compare(that, GraphNodes.Server.Calc.IsEqual.ofInt())
@JvmName("eqFloat")
context(context: FragmentGenerator)
infix fun Expr<Float>.eq(that: Expr<Float>) =
    compare(that, GraphNodes.Server.Calc.IsEqual.ofFloat())
@JvmName("eqConfig")
context(context: FragmentGenerator)
infix fun Expr<GenshinType.Config>.eq(that: Expr<GenshinType.Config>) =
    compare(that, GraphNodes.Server.Calc.IsEqual.ofConfig())
@JvmName("eqPrefab")
context(context: FragmentGenerator)
infix fun Expr<GenshinType.Prefab>.eq(that: Expr<GenshinType.Prefab>) =
    compare(that, GraphNodes.Server.Calc.IsEqual.ofPrefab())
@JvmName("eqBoolean")
context(context: FragmentGenerator)
infix fun Expr<Boolean>.eq(that: Expr<Boolean>) =
    compare(that, GraphNodes.Server.Calc.IsEqual.ofBoolean())

@JvmName("neString")
context(context: FragmentGenerator)
infix fun Expr<String>.ne(that: Expr<String>) =
    !(this eq that)
@JvmName("neGuid")
context(context: FragmentGenerator)
infix fun Expr<GenshinType.Guid>.ne(that: Expr<GenshinType.Guid>) =
    !(this eq that)
@JvmName("neEntity")
context(context: FragmentGenerator)
infix fun Expr<GenshinType.Entity>.ne(that: Expr<GenshinType.Entity>) =
    !(this eq that)
@JvmName("neVector")
context(context: FragmentGenerator)
infix fun Expr<GenshinType.Vector>.ne(that: Expr<GenshinType.Vector>) =
    !(this eq that)
@JvmName("neFaction")
context(context: FragmentGenerator)
infix fun Expr<GenshinType.Faction>.ne(that: Expr<GenshinType.Faction>) =
    !(this eq that)
@JvmName("neInt")
context(context: FragmentGenerator)
infix fun Expr<Int>.ne(that: Expr<Int>) =
    !(this eq that)
@JvmName("neFloat")
context(context: FragmentGenerator)
infix fun Expr<Float>.ne(that: Expr<Float>) =
    !(this eq that)
@JvmName("neConfig")
context(context: FragmentGenerator)
infix fun Expr<GenshinType.Config>.ne(that: Expr<GenshinType.Config>) =
    !(this eq that)
@JvmName("nePrefab")
context(context: FragmentGenerator)
infix fun Expr<GenshinType.Prefab>.ne(that: Expr<GenshinType.Prefab>) =
    !(this eq that)
@JvmName("neBoolean")
context(context: FragmentGenerator)
infix fun Expr<Boolean>.ne(that: Expr<Boolean>) =
    !(this eq that)

context(context: FragmentGenerator)
private fun <T> Expr<T>.compare(that: Expr<T>, node: GraphNodes.Expr2<Boolean, T, T>): Expr<Boolean> {
    context.addNode(node)
    this.apply(node.in0)
    that.apply(node.in1)
    return ExprPin(node.out)
}