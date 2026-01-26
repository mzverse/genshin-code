package mz.genshincode.graph.gen.event.entity

import mz.genshincode.GenshinType
import mz.genshincode.graph.GraphNodes
import mz.genshincode.graph.gen.EventDefinition
import mz.genshincode.graph.gen.Expr
import mz.genshincode.graph.gen.ExprPin
import mz.genshincode.graph.gen.NodeGraphGenerator
import mz.genshincode.graph.gen.Statement

data class EventEntityCreate(
    val entity: Expr<GenshinType.Entity>,
    val guid: Expr<GenshinType.Guid>
) {
    companion object : EventDefinition<EventEntityCreate> {
        override fun listen(context: NodeGraphGenerator): Pair<Statement, EventEntityCreate> {
            val node = GraphNodes.Server.Event.Entity.onCreate()
            return Pair(Statement(node), EventEntityCreate(
                ExprPin(node.out0),
                ExprPin(node.out1)
            ))
        }
    }
}