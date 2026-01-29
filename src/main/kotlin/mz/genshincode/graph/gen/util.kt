package mz.genshincode.graph.gen

import mz.genshincode.graph.GraphNodes


context(context: FragmentGenerator)
internal fun <T> operateBinaryClosed(a: Expr<T>, b: Expr<T>, constHandler: (T, T) -> T, nodeSupplier: () -> GraphNodes.Expr2<T, T, T>): Expr<T> =
    if(a is ExprConst && b is ExprConst)
        ExprConst(a.type, constHandler(a.value, b.value))
    else {
        val node = nodeSupplier()
        a.apply(node.in0)
        b.apply(node.in1)
        context.addNode(node)
        ExprPin(node.out)
    }