package mz.genshincode.graph.gen

import mz.genshincode.graph.GraphNodes

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
internal fun <T> binary(a: Expr<T>, b: Expr<T>, node: GraphNodes.Expr2<T, T, T>): Expr<T> {
    a.apply(node.in0)
    b.apply(node.in1)
    context.addNode(node)
    return ExprPin(node.out)
}