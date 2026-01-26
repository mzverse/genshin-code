@file:Suppress("unused")

package mz.genshincode.graph.gen

import mz.genshincode.GenshinType
import mz.genshincode.graph.GraphNodes

context(context: StatementGenerator)
operator fun Expr<Boolean>.not(): Expr<Boolean> {
    val node = GraphNodes.Server.Calc.Math.not()
    context.addNode(node)
    this.apply(node.in0)
    return ExprPin(node.out)
}

@JvmName("ltInt")
context(context: StatementGenerator)
infix fun Expr<Int>.lt(that: Expr<Int>) =
    compare(that, GraphNodes.Server.Calc.Math.lessThanInt())
@JvmName("gtInt")
context(context: StatementGenerator)
infix fun Expr<Int>.gt(that: Expr<Int>) =
    compare(that, GraphNodes.Server.Calc.Math.greaterThanInt())
@JvmName("leInt")
context(context: StatementGenerator)
infix fun Expr<Int>.le(that: Expr<Int>) =
    compare(that, GraphNodes.Server.Calc.Math.lessThanOrEqualInt())
@JvmName("geInt")
context(context: StatementGenerator)
infix fun Expr<Int>.ge(that: Expr<Int>) =
    compare(that, GraphNodes.Server.Calc.Math.greaterThanOrEqualInt())

@JvmName("ltFloat")
context(context: StatementGenerator)
infix fun Expr<Float>.lt(that: Expr<Float>) =
    compare(that, GraphNodes.Server.Calc.Math.lessThanFloat())
@JvmName("gtFloat")
context(context: StatementGenerator)
infix fun Expr<Float>.gt(that: Expr<Float>) =
    compare(that, GraphNodes.Server.Calc.Math.greaterThanFloat())
@JvmName("leFloat")
context(context: StatementGenerator)
infix fun Expr<Float>.le(that: Expr<Float>) =
    compare(that, GraphNodes.Server.Calc.Math.lessThanOrEqualFloat())
@JvmName("geFloat")
context(context: StatementGenerator)
infix fun Expr<Float>.ge(that: Expr<Float>) =
    compare(that, GraphNodes.Server.Calc.Math.greaterThanOrEqualFloat())

@JvmName("eqString")
context(context: StatementGenerator)
infix fun Expr<String>.eq(that: Expr<String>) =
    compare(that, GraphNodes.Server.Calc.IsEqual.ofString())
@JvmName("eqGuid")
context(context: StatementGenerator)
infix fun Expr<GenshinType.Guid>.eq(that: Expr<GenshinType.Guid>) =
    compare(that, GraphNodes.Server.Calc.IsEqual.ofGuid())
@JvmName("eqEntity")
context(context: StatementGenerator)
infix fun Expr<GenshinType.Entity>.eq(that: Expr<GenshinType.Entity>) =
    compare(that, GraphNodes.Server.Calc.IsEqual.ofEntity())
@JvmName("eqVector")
context(context: StatementGenerator)
infix fun Expr<GenshinType.Vector>.eq(that: Expr<GenshinType.Vector>) =
    compare(that, GraphNodes.Server.Calc.IsEqual.ofVector())
@JvmName("eqFaction")
context(context: StatementGenerator)
infix fun Expr<GenshinType.Faction>.eq(that: Expr<GenshinType.Faction>) =
    compare(that, GraphNodes.Server.Calc.IsEqual.ofFaction())
@JvmName("eqInt")
context(context: StatementGenerator)
infix fun Expr<Int>.eq(that: Expr<Int>) =
    compare(that, GraphNodes.Server.Calc.IsEqual.ofInt())
@JvmName("eqFloat")
context(context: StatementGenerator)
infix fun Expr<Float>.eq(that: Expr<Float>) =
    compare(that, GraphNodes.Server.Calc.IsEqual.ofFloat())
@JvmName("eqConfig")
context(context: StatementGenerator)
infix fun Expr<GenshinType.Config>.eq(that: Expr<GenshinType.Config>) =
    compare(that, GraphNodes.Server.Calc.IsEqual.ofConfig())
@JvmName("eqPrefab")
context(context: StatementGenerator)
infix fun Expr<GenshinType.Prefab>.eq(that: Expr<GenshinType.Prefab>) =
    compare(that, GraphNodes.Server.Calc.IsEqual.ofPrefab())
@JvmName("eqBoolean")
context(context: StatementGenerator)
infix fun Expr<Boolean>.eq(that: Expr<Boolean>) =
    compare(that, GraphNodes.Server.Calc.IsEqual.ofBoolean())

@JvmName("neString")
context(context: StatementGenerator)
infix fun Expr<String>.ne(that: Expr<String>) =
    !(this eq that)
@JvmName("neGuid")
context(context: StatementGenerator)
infix fun Expr<GenshinType.Guid>.ne(that: Expr<GenshinType.Guid>) =
    !(this eq that)
@JvmName("neEntity")
context(context: StatementGenerator)
infix fun Expr<GenshinType.Entity>.ne(that: Expr<GenshinType.Entity>) =
    !(this eq that)
@JvmName("neVector")
context(context: StatementGenerator)
infix fun Expr<GenshinType.Vector>.ne(that: Expr<GenshinType.Vector>) =
    !(this eq that)
@JvmName("neFaction")
context(context: StatementGenerator)
infix fun Expr<GenshinType.Faction>.ne(that: Expr<GenshinType.Faction>) =
    !(this eq that)
@JvmName("neInt")
context(context: StatementGenerator)
infix fun Expr<Int>.ne(that: Expr<Int>) =
    !(this eq that)
@JvmName("neFloat")
context(context: StatementGenerator)
infix fun Expr<Float>.ne(that: Expr<Float>) =
    !(this eq that)
@JvmName("neConfig")
context(context: StatementGenerator)
infix fun Expr<GenshinType.Config>.ne(that: Expr<GenshinType.Config>) =
    !(this eq that)
@JvmName("nePrefab")
context(context: StatementGenerator)
infix fun Expr<GenshinType.Prefab>.ne(that: Expr<GenshinType.Prefab>) =
    !(this eq that)
@JvmName("neBoolean")
context(context: StatementGenerator)
infix fun Expr<Boolean>.ne(that: Expr<Boolean>) =
    !(this eq that)

context(context: StatementGenerator)
private fun <T> Expr<T>.compare(that: Expr<T>, node: GraphNodes.Expr2<Boolean, T, T>): Expr<Boolean> {
    context.addNode(node)
    this.apply(node.in0)
    that.apply(node.in1)
    return ExprPin(node.out)
}