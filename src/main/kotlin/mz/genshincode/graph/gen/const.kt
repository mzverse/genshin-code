package mz.genshincode.graph.gen

import mz.genshincode.GenshinType

context(context: StatementGenerator)
fun <T> const(value: T): Expr<T> = ExprConst(value)

context(context: StatementGenerator)
fun guid(id: Long) = const(GenshinType.Guid(id));