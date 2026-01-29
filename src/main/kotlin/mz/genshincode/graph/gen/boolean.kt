@file:Suppress("unused")

package mz.genshincode.graph.gen

import mz.genshincode.graph.GraphNodes
import mz.genshincode.graph.NodeGraphType

context(context: FragmentGenerator)
operator fun Expr<Boolean>.not(): Expr<Boolean> {
    val node = GraphNodes.Server.Calc.Math.not()
    context.addNode(node)
    this.apply(node.in0)
    return ExprPin(node.out)
}

context(context: FragmentGenerator)
infix fun Expr<Boolean>.and(that: Expr<Boolean>) =
    logic(this, that, GraphNodes.Server.Calc.Math.logicAnd())
context(context: FragmentGenerator)
infix fun Expr<Boolean>.or(that: Expr<Boolean>) =
    logic(this, that, GraphNodes.Server.Calc.Math.logicOr())
context(context: FragmentGenerator)
infix fun Expr<Boolean>.xor(that: Expr<Boolean>) =
    logic(this, that, GraphNodes.Server.Calc.Math.logicXor())

context(context: FragmentGenerator)
private fun logic(a: Expr<Boolean>, b: Expr<Boolean>, node: GraphNodes.Expr2<Boolean, Boolean, Boolean>): Expr<Boolean> {
    context.addNode(node)
    a.apply(node.in0)
    b.apply(node.in1)
    return ExprPin(node.out)
}

context(context: FragmentGenerator, serverTag: NodeGraphType.SERVER)
infix fun Expr<Boolean>.and(then: context(FragmentGenerator)() -> Expr<Boolean>) =
    Local(this).also {
        If(it.get()) {
            it.set(then())
        }
    }.get()
context(context: FragmentGenerator, serverTag: NodeGraphType.SERVER)
infix fun Expr<Boolean>.or(then: context(FragmentGenerator)() -> Expr<Boolean>) =
    Local(this).also {
        If(!it.get()) {
            it.set(then())
        }
    }.get()
