package mz.genshincode.graph

sealed interface NodeGraphType {
    object SERVER: NodeGraphType
    object `TODO CLIENT`: NodeGraphType
}