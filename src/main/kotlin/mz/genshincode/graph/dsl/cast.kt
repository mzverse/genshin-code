package mz.genshincode.graph.dsl

import mz.genshincode.GenshinType
import mz.genshincode.graph.GraphNodes

fun Expr<GenshinType.Entity>.asString(): Expr<String> {
    this as ExprNodes
    val node = GraphNodes.Server.Calc.Cast.entityToString()
    this.pin.connect(node.in0)
    return ExprNodes(this.nodes + node, node.out)
}