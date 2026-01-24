package mz.genshincode.graph.dsl.event.entity

import mz.genshincode.GenshinType
import mz.genshincode.graph.GraphNodes
import mz.genshincode.graph.dsl.EventDefinition
import mz.genshincode.graph.dsl.Expr
import mz.genshincode.graph.dsl.ExprNodes
import mz.genshincode.graph.dsl.NodeGraphDsl
import mz.genshincode.graph.dsl.Trigger

data class EventEntityCreate(
    val entity: Expr<GenshinType.Entity>,
    val guid: Expr<GenshinType.Guid>
) {
    companion object : EventDefinition<EventEntityCreate> {
        override fun listen(context: NodeGraphDsl): Pair<Trigger, EventEntityCreate> {
            val node = GraphNodes.Server.Event.Entity.onCreate()
            return Pair(Trigger(node), EventEntityCreate(
                ExprNodes(node.out0),
                ExprNodes(node.out1)
            ))
        }
    }
}