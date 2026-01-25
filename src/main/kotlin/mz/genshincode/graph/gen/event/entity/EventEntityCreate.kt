package mz.genshincode.graph.gen.event.entity

import mz.genshincode.GenshinType
import mz.genshincode.graph.GraphNodes
import mz.genshincode.graph.gen.EventDefinition
import mz.genshincode.graph.gen.Expr
import mz.genshincode.graph.gen.ExprNodes
import mz.genshincode.graph.gen.NodeGraphGenerator
import mz.genshincode.graph.gen.Trigger

data class EventEntityCreate(
    val entity: Expr<GenshinType.Entity>,
    val guid: Expr<GenshinType.Guid>
) {
    companion object : EventDefinition<EventEntityCreate> {
        override fun listen(context: NodeGraphGenerator): Pair<Trigger, EventEntityCreate> {
            val node = GraphNodes.Server.Event.Entity.onCreate()
            return Pair(Trigger(node), EventEntityCreate(
                ExprNodes(node.out0),
                ExprNodes(node.out1)
            ))
        }
    }
}