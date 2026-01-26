package mz.genshincode.graph.gen

import mz.genshincode.GenshinType
import mz.genshincode.graph.GraphNodes

@JvmName("castEntityToString")
context(context: StatementGenerator)
fun Expr<GenshinType.Entity>.asString() = cast(GraphNodes.Server.Calc.Cast.entityToString())

@JvmName("castGuidToString")
context(context: StatementGenerator)
fun Expr<GenshinType.Guid>.asString() = cast(GraphNodes.Server.Calc.Cast.guidToString())

@JvmName("castIntToString")
context(context: StatementGenerator)
fun Expr<Int>.asString() = cast(GraphNodes.Server.Calc.Cast.intToString())

context(context: StatementGenerator)
private fun <T, R> Expr<T>.cast(node: GraphNodes.Expr1<R, T>): Expr<R> {
    this.apply(node.in0)
    context.addNode(node)
    return ExprPin(node.out)
}