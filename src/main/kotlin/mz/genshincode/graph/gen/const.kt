package mz.genshincode.graph.gen

import mz.genshincode.GenshinType

fun <T> NodeGraphGenerator.const(value: T) = ExprConst(value)

fun NodeGraphGenerator.guid(id: Long) = const(GenshinType.Guid(id));