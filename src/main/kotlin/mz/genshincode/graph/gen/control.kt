@file:Suppress("FunctionName")

package mz.genshincode.graph.gen

import mz.genshincode.graph.GraphNodes

fun StatementGenerator.If(con: Expr<Boolean>, then: StatementGenerator.() -> Unit): IfContext {
    val node = GraphNodes.Server.Control.If()
    con.connect(node.inCondition)
    val statement = StatementGenerator().apply(then).statements.join()
    node.flowThen.connectAll(statement.flowsIn)
    this.append(Statement(con.nodes + node + statement.nodes, listOf(node.flowIn)))
    return IfContext(this, node)
}

data class IfContext(val parent: StatementGenerator, val node: GraphNodes.Server.Control.NodeIf) {
    infix fun Else(then: StatementGenerator.() -> Unit) {
        val statement = StatementGenerator().apply(then).statements.join()
        node.flowElse.connectAll(statement.flowsIn)
        parent.append(Statement(statement.nodes, emptyList()))
    }
}
