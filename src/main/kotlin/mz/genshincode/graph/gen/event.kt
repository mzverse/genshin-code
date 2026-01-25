package mz.genshincode.graph.gen


interface EventDefinition<T> {
    fun listen(context: NodeGraphGenerator): Pair<Trigger, T>
}

fun <T> NodeGraphGenerator.on(def: EventDefinition<T>, configuration: StatementGenerator.(T) -> Unit) {
    val (trigger, event) = def.listen(this)
    val chain = StatementGenerator().apply { configuration(event) }.statements.join()
    graph.addNodes(trigger.nodes)
    graph.addNodes(chain.nodes)
    trigger.flow.connectAll(chain.flowsIn)
}