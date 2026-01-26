@file:Suppress("unused")

package mz.genshincode.graph.gen

import mz.genshincode.graph.GraphNodes

context(context: StatementGenerator)
fun Local<Int>.increment() =
    this.set(this.get() + const(1))
context(context: StatementGenerator)
fun Local<Int>.decrement() =
    this.set(this.get() - const(1))

context(context: StatementGenerator)
operator fun Expr<Int>.plus(that: Expr<Int>) =
    if(this is ExprConst && that is ExprConst)
        const(this.value + that.value)
    else
        binary(this, that, GraphNodes.Server.Calc.Math.addInt())
context(context: StatementGenerator)
operator fun Expr<Int>.minus(that: Expr<Int>) =
    if(this is ExprConst && that is ExprConst)
        const(this.value - that.value)
    else
        binary(this, that, GraphNodes.Server.Calc.Math.subtractInt())

context(context: StatementGenerator)
operator fun Expr<Float>.plus(that: Expr<Float>) =
    if(this is ExprConst && that is ExprConst)
        const(this.value + that.value)
    else
        binary(this, that, GraphNodes.Server.Calc.Math.addFloat())
context(context: StatementGenerator)
operator fun Expr<Float>.minus(that: Expr<Float>) =
    if(this is ExprConst && that is ExprConst)
        const(this.value - that.value)
    else
        binary(this, that, GraphNodes.Server.Calc.Math.subtractFloat())

context(context: StatementGenerator)
internal fun <T> binary(a: Expr<T>, b: Expr<T>, node: GraphNodes.Expr2<T, T, T>): Expr<T> {
    a.apply(node.in0)
    b.apply(node.in1)
    context.addNode(node)
    return ExprPin(node.out)
}