package mz.genshincode.graph.gen

import mz.genshincode.Entity
import mz.genshincode.Guid
import mz.genshincode.graph.GraphNodes

// TODO

@JvmName("castEntityToString")
context(context: FragmentGenerator)
fun Expr<Entity>.asString() = cast(GraphNodes.Server.Calc.Cast.entityToString())

@JvmName("castGuidToString")
context(context: FragmentGenerator)
fun Expr<Guid>.asString() = cast(GraphNodes.Server.Calc.Cast.guidToString())

@JvmName("castIntToString")
context(context: FragmentGenerator)
fun Expr<Int>.asString() = cast(GraphNodes.Server.Calc.Cast.intToString())

context(context: FragmentGenerator)
private fun <T, R> Expr<T>.cast(node: GraphNodes.Expr1<R, T>): Expr<R> {
    this.apply(node.in0)
    context.addNode(node)
    return ExprPin(node.out)
}