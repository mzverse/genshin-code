@file:Suppress("unused")

package mz.genshincode.graph.gen

import mz.genshincode.graph.GraphNodes
import mz.genshincode.graph.NodeGraphType


context(context: FragmentGenerator)
operator fun Local<Int>.inc() =
    apply { set(this.get() + const(1)) }

context(context: FragmentGenerator)
operator fun Local<Int>.dec() =
    apply { set(this.get() - const(1)) }

context(context: FragmentGenerator)
operator fun Expr<Int>.plus(that: Expr<Int>) =
    operateBinaryClosed(this, that, Int::plus, GraphNodes.Server.Calc.Math::addInt)

context(context: FragmentGenerator)
operator fun Expr<Int>.minus(that: Expr<Int>) =
    operateBinaryClosed(this, that, Int::minus, GraphNodes.Server.Calc.Math::subtractInt)

context(context: FragmentGenerator)
operator fun Expr<Int>.times(that: Expr<Int>) =
    operateBinaryClosed(this, that, Int::times, GraphNodes.Server.Calc.Math::multiplyInt)

context(context: FragmentGenerator)
operator fun Expr<Int>.div(that: Expr<Int>) =
    operateBinaryClosed(this, that, Int::div, GraphNodes.Server.Calc.Math::divideInt)

context(context: FragmentGenerator)
infix fun Expr<Int>.shl(that: Expr<Int>) =
    operateBinaryClosed(this, that, Int::shl, GraphNodes.Server.Calc.Math::shl)
context(context: FragmentGenerator)
infix fun Expr<Int>.ushr(that: Expr<Int>) =
    operateBinaryClosed(this, that, Int::ushr, GraphNodes.Server.Calc.Math::ushr)
context(context: FragmentGenerator)
infix fun Expr<Int>.and(that: Expr<Int>) =
    operateBinaryClosed(this, that, Int::and, GraphNodes.Server.Calc.Math::bitwiseAnd)
context(context: FragmentGenerator)
infix fun Expr<Int>.or(that: Expr<Int>) =
    operateBinaryClosed(this, that, Int::or, GraphNodes.Server.Calc.Math::bitwiseOr)

context(context: FragmentGenerator, serverTag: NodeGraphType.SERVER)
infix fun Expr<Int>.shr(n: Expr<Int>): Expr<Int> { // FIXME: too complex
    val result = Local(this)
    If(n gt const(0)) {
        If (n ge const(32)) {
            If(this lt const(0)) {
                result.set(const(-1))
            } Else {
                result.set(const(0))
            }
        } Else {
            val logical = this ushr n
            If (this ge const(0)) {
                result.set(logical)
            } Else {
                val mask = const(-1) shl (const(32) - n)
                result.set(logical or mask)
            }
        }
    }
    return result.get()
}
