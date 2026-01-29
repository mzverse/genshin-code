package mz.genshincode.graph.gen

import mz.genshincode.GenshinType
import mz.genshincode.graph.GraphNode


sealed interface Expr<T> {
    val type: GenshinType<T>
    fun apply(pin: GraphNode.Pin<T>) = when(this) {
        is ExprPin ->
            pin.connect(this.pin)
        is ExprConst ->
            pin.setValue(this.value)
    }
}
data class ExprPin<T>(val pin: GraphNode.Pin<T>): Expr<T> {
    override val type: GenshinType<T>
        get() = pin.type.get().unwrap()
}
data class ExprConst<T>(override val type: GenshinType<T>, val value: T): Expr<T>
