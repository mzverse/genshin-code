package mz.genshincode.graph.gen

import mz.genshincode.graph.GraphNodes


context(context: FragmentGenerator)
operator fun Expr<Float>.plus(that: Expr<Float>) =
    operateBinaryClosed(this, that, Float::plus, GraphNodes.Server.Calc.Math::addFloat)

context(context: FragmentGenerator)
operator fun Expr<Float>.minus(that: Expr<Float>) =
    operateBinaryClosed(this, that, Float::minus, GraphNodes.Server.Calc.Math::subtractFloat)

context(context: FragmentGenerator)
operator fun Expr<Float>.times(that: Expr<Float>) =
    operateBinaryClosed(this, that, Float::times, GraphNodes.Server.Calc.Math::multiplyFloat)

context(context: FragmentGenerator)
operator fun Expr<Float>.div(that: Expr<Float>) =
    operateBinaryClosed(this, that, Float::div, GraphNodes.Server.Calc.Math::divideFloat)