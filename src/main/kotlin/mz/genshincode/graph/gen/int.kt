package mz.genshincode.graph.gen

import mz.genshincode.graph.GraphNodes


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