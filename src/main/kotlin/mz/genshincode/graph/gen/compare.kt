package mz.genshincode.graph.gen

import mz.genshincode.GenshinType
import mz.genshincode.graph.GraphNodes

@JvmName("eqString")
context(context: StatementGenerator)
infix fun Expr<String>.eq(that: Expr<String>) =
    eq(that, GraphNodes.Server.Calc.IsEqual.ofString())
@JvmName("eqGuid")
context(context: StatementGenerator)
infix fun Expr<GenshinType.Guid>.eq(that: Expr<GenshinType.Guid>) =
    eq(that, GraphNodes.Server.Calc.IsEqual.ofGuid())
@JvmName("eqEntity")
context(context: StatementGenerator)
infix fun Expr<GenshinType.Entity>.eq(that: Expr<GenshinType.Entity>) =
    eq(that, GraphNodes.Server.Calc.IsEqual.ofEntity())
@JvmName("eqVector")
context(context: StatementGenerator)
infix fun Expr<GenshinType.Vector>.eq(that: Expr<GenshinType.Vector>) =
    eq(that, GraphNodes.Server.Calc.IsEqual.ofVector())
@JvmName("eqFaction")
context(context: StatementGenerator)
infix fun Expr<GenshinType.Faction>.eq(that: Expr<GenshinType.Faction>) =
    eq(that, GraphNodes.Server.Calc.IsEqual.ofFaction())
@JvmName("eqInt")
context(context: StatementGenerator)
infix fun Expr<Int>.eq(that: Expr<Int>) =
    eq(that, GraphNodes.Server.Calc.IsEqual.ofInt())
@JvmName("eqFloat")
context(context: StatementGenerator)
infix fun Expr<Float>.eq(that: Expr<Float>) =
    eq(that, GraphNodes.Server.Calc.IsEqual.ofFloat())
@JvmName("eqConfig")
context(context: StatementGenerator)
infix fun Expr<GenshinType.Config>.eq(that: Expr<GenshinType.Config>) =
    eq(that, GraphNodes.Server.Calc.IsEqual.ofConfig())
@JvmName("eqPrefab")
context(context: StatementGenerator)
infix fun Expr<GenshinType.Prefab>.eq(that: Expr<GenshinType.Prefab>) =
    eq(that, GraphNodes.Server.Calc.IsEqual.ofPrefab())
@JvmName("eqBool")
context(context: StatementGenerator)
infix fun Expr<Boolean>.eq(that: Expr<Boolean>) =
    eq(that, GraphNodes.Server.Calc.IsEqual.ofBool())

context(context: StatementGenerator)
private fun <T> Expr<T>.eq(that: Expr<T>, node: GraphNodes.Expr2<Boolean, T, T>): Expr<Boolean> {
    context.append(Statement(setOf(node)))
    this.apply(node.in0)
    that.apply(node.in1)
    return ExprPin(node.out)
}