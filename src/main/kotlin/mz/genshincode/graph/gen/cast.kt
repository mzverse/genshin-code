package mz.genshincode.graph.gen

import mz.genshincode.GenshinType
import mz.genshincode.graph.GraphNodes

@JvmName("castEntityToString")
fun Expr<GenshinType.Entity>.asString(): Expr<String> {
    val node = GraphNodes.Server.Calc.Cast.entityToString()
    this.connect(node.in0)
    return ExprNodes(this.nodes + node, node.out)
}

@JvmName("castGuidToString")
fun Expr<GenshinType.Guid>.asString(): Expr<String> {
    this as ExprNodes // FIXME
    val node = GraphNodes.Server.Calc.Cast.guidToString()
    this.connect(node.in0)
    return ExprNodes(this.nodes + node, node.out)
}
