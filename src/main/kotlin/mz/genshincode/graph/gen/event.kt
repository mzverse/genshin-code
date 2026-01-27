package mz.genshincode.graph.gen


interface EventEmitter<T> {
    context(context: NodeGraphGenerator)
    fun subscribe(configuration: context(FragmentGenerator)(T) -> Unit)
}

context(context: NodeGraphGenerator)
fun <T> on(emitter: EventEmitter<T>, configuration: context(FragmentGenerator)(T) -> Unit) =
    emitter.subscribe(configuration)