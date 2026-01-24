package mz.genshincode.graph.dsl

import mz.genshincode.graph.GraphNodes

fun StatementsDsl.log(expr: Expr<String>) {
    val node = GraphNodes.Server.Exec.print()
    when(expr) {
        is ExprConst ->
            node.in0.setValue(expr.value)
        is ExprNodes ->
            node.in0.connect(expr.pin)
    }
    this.append(Statement(expr.nodes + node, node.flowIn, node.flowOut))
}