package mz.genshincode.graph.gen.event.entity

import mz.genshincode.GenshinType
import mz.genshincode.graph.GraphNodes
import mz.genshincode.graph.gen.EventEmitter
import mz.genshincode.graph.gen.Expr
import mz.genshincode.graph.gen.ExprPin
import mz.genshincode.graph.gen.NodeGraphGenerator
import mz.genshincode.graph.gen.Statement
import mz.genshincode.graph.gen.StatementGenerator

data class EventEntityCreate(
    val entity: Expr<GenshinType.Entity>,
    val guid: Expr<GenshinType.Guid>
) {
    companion object : EventEmitter<EventEntityCreate> {
        context(context: NodeGraphGenerator)
        override fun subscribe(configuration: context(StatementGenerator)(EventEntityCreate) -> Unit) {
            val node = GraphNodes.Server.Event.Entity.onCreate()
            context.add(Statement(node) + Statement { configuration(EventEntityCreate(
                ExprPin(node.out0),
                ExprPin(node.out1)
            )) })
        }
    }
}