package mz.genshincode.graph.gen

import mz.genshincode.GenshinDsl


interface EventEmitter<T> {
    context(context: NodeGraphGenerator)
    fun subscribe(configuration: context(FragmentGenerator)(T) -> Unit)
}

@GenshinDsl
context(context: NodeGraphGenerator)
fun <T> on(emitter: EventEmitter<T>, configuration: context(FragmentGenerator)(T) -> Unit) =
    emitter.subscribe(configuration)