package mz.genshincode.graph.gen.event.entity

import mz.genshincode.Entity
import mz.genshincode.Guid
import mz.genshincode.graph.GraphNodes
import mz.genshincode.graph.gen.*

data class EventEntityCreate(
    val entity: Expr<Entity>,
    val guid: Expr<Guid>
) {
    companion object : EventEmitter<EventEntityCreate> {
        context(context: NodeGraphGenerator)
        override fun subscribe(configuration: context(FragmentGenerator)(EventEntityCreate) -> Unit) {
            val node = GraphNodes.Server.Event.Entity.onCreate()
            context.add(Statement(node) + Fragment { configuration(EventEntityCreate(
                ExprPin(node.out0),
                ExprPin(node.out1)
            )) })
        }
    }
}