package mz.genshincode.graph.gen


interface EventDefinition<T> {
    fun listen(context: NodeGraphGenerator): Pair<Statement, T>
}

context(context: NodeGraphGenerator)
fun <T> on(def: EventDefinition<T>, configuration: context(StatementGenerator)(T) -> Unit) {
    val (trigger, event) = def.listen(context)
    val chain = StatementGenerator().apply { configuration(event) }.statement
    context.graph.addNodes((trigger + chain).nodes)
}