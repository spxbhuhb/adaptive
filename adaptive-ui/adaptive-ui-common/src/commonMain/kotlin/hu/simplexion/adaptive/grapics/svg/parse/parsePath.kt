/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.grapics.svg.parse

import hu.simplexion.adaptive.grapics.svg.instruction.*

fun parsePath(source: String): List<SvgPathCommand> {

    if (source.isEmpty()) return emptyList()

    val commands = mutableListOf<SvgPathCommand>()
    val end = source.length
    var index = 0

    var command: Char? = null
    val parameters = mutableListOf(StringBuilder())
    var parameterIndex = 0
    var parameter = parameters[parameterIndex]
    var position = Pair(0.0, 0.0)
    var subPathStart = Pair(0.0, 0.0)

    while (index < end) {

        val char = source[index ++]

        when  {
            char in DOUBLE_CHARS -> {
                if ((char == '-' && parameter.isNotEmpty()) || (char == '.' && parameter.contains('.'))) {
                    parameterIndex ++
                    if (parameterIndex == parameters.size) {
                        parameters.add(StringBuilder())
                    }
                    parameter = parameters[parameterIndex]
                    parameter.clear()
                }
                parameter.append(char)
            }

            char.isWhitespace() || char == ',' -> {
                if (parameter.isNotEmpty()) {
                    parameterIndex ++
                    if (parameterIndex == parameters.size) {
                        parameters.add(StringBuilder())
                    }
                    parameter = parameters[parameterIndex]
                    parameter.clear()
                }
            }

            else -> {
                val parameterCount = if (parameter.isNotEmpty()) parameterIndex + 1 else parameterIndex
                position = build(command, parameters, parameterCount, position, subPathStart, commands)

                if (command == 'M' || command == 'm') {
                    subPathStart = position
                }

                command = char
                parameterIndex = 0
                parameter = parameters[parameterIndex]
                parameter.clear()
            }
        }
    }

    if (command != null) {
        val parameterCount = if (parameter.isNotEmpty()) parameterIndex + 1 else parameterIndex
        build(command, parameters, parameterCount, position, subPathStart, commands)
    }

    return commands
}

private fun build(
    command: Char?,
    parameters: List<StringBuilder>,
    parameterCount: Int,
    position: Pair<Double, Double>,
    subPathStart: Pair<Double, Double>,
    commands: MutableList<SvgPathCommand>
): Pair<Double, Double> =
    when (command) {
        'M' -> moveToAbsolute(parameters, parameterCount, commands)
        'm' -> moveToRelative(parameters, parameterCount, position, commands)

        'L' -> lineToAbsolute(parameters, parameterCount, commands)
        'l' -> lineToRelative(parameters, parameterCount, position, commands)

        'H' -> horizontalLineToAbsolute(parameters, parameterCount, position, commands)
        'h' -> horizontalLineToRelative(parameters, parameterCount, position, commands)

        'V' -> verticalLineToAbsolute(parameters, parameterCount, position, commands)
        'v' -> verticalLineToRelative(parameters, parameterCount, position, commands)

        'C' -> cubicCurveAbsolute(parameters, parameterCount, commands)
        'c' -> cubicCurveRelative(parameters, parameterCount, position, commands)

        'S' -> cubicCurveSmoothAbsolute(parameters, parameterCount, commands)
        's' -> cubicCurveSmoothRelative(parameters, parameterCount, position, commands)

        'Q' -> quadraticCurveAbsolute(parameters, parameterCount, commands)
        'q' -> quadraticCurveRelative(parameters, parameterCount, position, commands)

        'T' -> quadraticCurveSmoothAbsolute(parameters, parameterCount, commands)
        't' -> quadraticCurveSmoothRelative(parameters, parameterCount, position, commands)

        'A' -> arcAbsolute(parameters, parameterCount, position, commands)
        'a' -> arcRelative(parameters, parameterCount, position, commands)

        'Z' -> closePath(position, subPathStart, commands)

        null -> position

        else -> throw IllegalArgumentException("unknown SVG path command: $command")
    }


private fun moveToAbsolute(parameters: List<StringBuilder>, parameterCount: Int, commands: MutableList<SvgPathCommand>): Pair<Double, Double> {
    var parameterIndex = 0

    check(parameterCount % 2 == 0) { "invalid number of parameters" }

    var x = parameters.toDouble(parameterIndex ++)
    var y = parameters.toDouble(parameterIndex ++)

    commands.add(MoveTo(x, y))

    while (parameterIndex < parameterCount) {

        x = parameters.toDouble(parameterIndex ++)
        y = parameters.toDouble(parameterIndex ++)

        commands.add(LineTo(x, y))
    }

    return x to y
}

private fun moveToRelative(parameters: List<StringBuilder>, parameterCount: Int, point: Pair<Double, Double>, commands: MutableList<SvgPathCommand>): Pair<Double, Double> {
    var parameterIndex = 0

    check(parameterCount % 2 == 0) { "invalid number of parameters" }

    var x = point.first + parameters.toDouble(parameterIndex ++)
    var y = point.second + parameters.toDouble(parameterIndex ++)

    commands.add(MoveTo(x, y))

    while (parameterIndex < parameterCount) {

        x += parameters.toDouble(parameterIndex ++)
        y += parameters.toDouble(parameterIndex ++)

        commands.add(LineTo(x, y))
    }

    return x to y
}

private fun closePath(point: Pair<Double, Double>, subPathStart: Pair<Double, Double>, commands: MutableList<SvgPathCommand>): Pair<Double, Double> {
    commands += ClosePath(point.first, point.second, subPathStart.first, subPathStart.second)
    return subPathStart
}

private fun lineToAbsolute(parameters: List<StringBuilder>, parameterCount: Int, commands: MutableList<SvgPathCommand>): Pair<Double, Double> {
    var parameterIndex = 0

    check(parameterCount >= 2 && parameterCount % 2 == 0) { "invalid number of parameters" }

    var x = 0.0
    var y = 0.0

    while (parameterIndex < parameterCount) {

        x = parameters.toDouble(parameterIndex ++)
        y = parameters.toDouble(parameterIndex ++)

        commands.add(LineTo(x, y))
    }

    return x to y
}

private fun lineToRelative(parameters: List<StringBuilder>, parameterCount: Int, point: Pair<Double, Double>, commands: MutableList<SvgPathCommand>): Pair<Double, Double> {
    var parameterIndex = 0

    check(parameterCount >= 2 && parameterCount % 2 == 0) { "invalid number of parameters" }

    var x = point.first
    var y = point.second

    while (parameterIndex < parameterCount) {

        x += parameters.toDouble(parameterIndex ++)
        y += parameters.toDouble(parameterIndex ++)

        commands.add(LineTo(x, y))
    }

    return x to y
}

private fun horizontalLineToAbsolute(parameters: List<StringBuilder>, parameterCount: Int, position: Pair<Double, Double>, commands: MutableList<SvgPathCommand>): Pair<Double, Double> {
    var lastIndex = position.first

    for (i in 0 until parameterCount) {
        val x = parameters.toDouble(i)
        commands.add(LineTo(x, position.second))
        lastIndex = x
    }

    return lastIndex to position.second
}

private fun horizontalLineToRelative(parameters: List<StringBuilder>, parameterCount: Int, position: Pair<Double, Double>, commands: MutableList<SvgPathCommand>): Pair<Double, Double> {
    var lastRelativeValue = position.first

    for (i in 0 until parameterCount) {
        lastRelativeValue += parameters.toDouble(i)
        commands.add(LineTo(lastRelativeValue, position.second))
    }

    return lastRelativeValue to position.second
}

private fun verticalLineToAbsolute(parameters: List<StringBuilder>, parameterCount: Int, position: Pair<Double, Double>, commands: MutableList<SvgPathCommand>): Pair<Double, Double> {
    var lastY = position.second

    for (i in 0 until parameterCount) {
        val y = parameters.toDouble(i)
        commands.add(LineTo(position.first, y))
        lastY = y
    }

    return position.first to lastY
}

private fun verticalLineToRelative(parameters: List<StringBuilder>, parameterCount: Int, position: Pair<Double, Double>, commands: MutableList<SvgPathCommand>): Pair<Double, Double> {
    var lastRelativeValue = position.second

    for (i in 0 until parameterCount) {
        lastRelativeValue += parameters.toDouble(i)
        commands.add(LineTo(position.first, lastRelativeValue))
    }

    return position.first to lastRelativeValue
}

private fun cubicCurveAbsolute(parameters: List<StringBuilder>, parameterCount: Int, commands: MutableList<SvgPathCommand>): Pair<Double, Double> {
    check(parameterCount >= 6 && parameterCount % 6 == 0) { "invalid number of parameters" }

    var parameterIndex = 0
    var lastCommand: CubicCurve? = null

    while (parameterIndex < parameterCount) {
        lastCommand = CubicCurve(
            parameters.toDouble(parameterIndex ++),
            parameters.toDouble(parameterIndex ++),
            parameters.toDouble(parameterIndex ++),
            parameters.toDouble(parameterIndex ++),
            parameters.toDouble(parameterIndex ++),
            parameters.toDouble(parameterIndex ++)
        )
        commands.add(lastCommand)
    }

    return lastCommand !!.let { it.x to it.y }
}

private fun cubicCurveRelative(parameters: List<StringBuilder>, parameterCount: Int, position: Pair<Double, Double>, commands: MutableList<SvgPathCommand>): Pair<Double, Double> {
    check(parameterCount >= 6 && parameterCount % 6 == 0) { "invalid number of parameters" }

    var parameterIndex = 0
    var x = position.first
    var y = position.second
    var x1: Double
    var y1: Double
    var x2: Double
    var y2: Double

    while (parameterIndex < parameterCount) {
        x1 = x + parameters.toDouble(parameterIndex ++)
        y1 = y + parameters.toDouble(parameterIndex ++)
        x2 = x + parameters.toDouble(parameterIndex ++)
        y2 = y + parameters.toDouble(parameterIndex ++)
        x += parameters.toDouble(parameterIndex ++)
        y += parameters.toDouble(parameterIndex ++)

        commands.add(CubicCurve(x1, y1, x2, y2, x, y))
    }

    return x to y
}

private fun cubicCurveSmoothAbsolute(parameters: List<StringBuilder>, parameterCount: Int, commands: MutableList<SvgPathCommand>): Pair<Double, Double> {
    check(parameterCount >= 4 && parameterCount % 4 == 0) { "invalid number of parameters" }

    var parameterIndex = 0
    var lastCommand: CubicCurveSmooth? = null

    while (parameterIndex < parameterCount) {
        lastCommand = CubicCurveSmooth(
            parameters.toDouble(parameterIndex ++),
            parameters.toDouble(parameterIndex ++),
            parameters.toDouble(parameterIndex ++),
            parameters.toDouble(parameterIndex ++)
        )
        commands.add(lastCommand)
    }

    return lastCommand !!.let { it.x to it.y }
}

private fun cubicCurveSmoothRelative(parameters: List<StringBuilder>, parameterCount: Int, position: Pair<Double, Double>, commands: MutableList<SvgPathCommand>): Pair<Double, Double> {
    check(parameterCount >= 4 && parameterCount % 4 == 0) { "invalid number of parameters" }

    var parameterIndex = 0
    var x = position.first
    var y = position.second
    var x2: Double
    var y2: Double

    while (parameterIndex < parameterCount) {
        x2 = x + parameters.toDouble(parameterIndex ++)
        y2 = y + parameters.toDouble(parameterIndex ++)
        x += parameters.toDouble(parameterIndex ++)
        y += parameters.toDouble(parameterIndex ++)

        commands.add(CubicCurveSmooth(x2, y2, x, y))
    }

    return x to y
}

private fun quadraticCurveAbsolute(parameters: List<StringBuilder>, parameterCount: Int, commands: MutableList<SvgPathCommand>): Pair<Double, Double> {
    check(parameterCount >= 4 && parameterCount % 4 == 0) { "invalid number of parameters" }

    var parameterIndex = 0
    var lastCommand: QuadraticCurve? = null

    while (parameterIndex < parameterCount) {
        lastCommand = QuadraticCurve(
            parameters.toDouble(parameterIndex ++),
            parameters.toDouble(parameterIndex ++),
            parameters.toDouble(parameterIndex ++),
            parameters.toDouble(parameterIndex ++)
        )
        commands.add(lastCommand)
    }

    return lastCommand !!.let { it.x to it.y }
}

private fun quadraticCurveRelative(parameters: List<StringBuilder>, parameterCount: Int, position: Pair<Double, Double>, commands: MutableList<SvgPathCommand>): Pair<Double, Double> {
    check(parameterCount >= 4 && parameterCount % 4 == 0) { "invalid number of parameters" }

    var parameterIndex = 0
    var x = position.first
    var y = position.second
    var x1: Double
    var y1: Double

    while (parameterIndex < parameterCount) {
        x1 = x + parameters.toDouble(parameterIndex ++)
        y1 = y + parameters.toDouble(parameterIndex ++)
        x += parameters.toDouble(parameterIndex ++)
        y += parameters.toDouble(parameterIndex ++)

        commands.add(QuadraticCurve(x1, y1, x, y))
    }

    return x to y
}

private fun quadraticCurveSmoothAbsolute(parameters: List<StringBuilder>, parameterCount: Int, commands: MutableList<SvgPathCommand>): Pair<Double, Double> {
    check(parameterCount >= 2 && parameterCount % 2 == 0) { "invalid number of parameters" }

    var parameterIndex = 0
    var lastCommand: QuadraticCurveSmooth? = null

    while (parameterIndex < parameterCount) {
        lastCommand = QuadraticCurveSmooth(
            parameters.toDouble(parameterIndex ++),
            parameters.toDouble(parameterIndex ++)
        )
        commands.add(lastCommand)
    }

    return lastCommand !!.let { it.x to it.y }
}

private fun quadraticCurveSmoothRelative(parameters: List<StringBuilder>, parameterCount: Int, position: Pair<Double, Double>, commands: MutableList<SvgPathCommand>): Pair<Double, Double> {
    check(parameterCount >= 2 && parameterCount % 2 == 0) { "invalid number of parameters" }

    var parameterIndex = 0
    var x = position.first + parameters.toDouble(parameterIndex ++)
    var y = position.second + parameters.toDouble(parameterIndex ++)

    commands.add(QuadraticCurveSmooth(x, y))

    return x to y
}

private fun arcAbsolute(parameters: List<StringBuilder>, parameterCount: Int, position: Pair<Double, Double>, commands: MutableList<SvgPathCommand>): Pair<Double, Double> {
    check(parameterCount >= 7 && parameterCount % 7 == 0) { "invalid number of parameters" }
    var parameterIndex = 0
    var lastCommand: Arc? = null
    while (parameterIndex < parameterCount) {
        lastCommand = Arc(
            rx = parameters.toDouble(parameterIndex ++),
            ry = parameters.toDouble(parameterIndex ++),
            xAxisRotation = parameters.toDouble(parameterIndex ++),
            largeArcFlag = parameters.toInt(parameterIndex ++),
            sweepFlag = parameters.toInt(parameterIndex ++),
            x1 = position.first,
            y1 = position.second,
            x2 = parameters.toDouble(parameterIndex ++),
            y2 = parameters.toDouble(parameterIndex ++)
        )
        commands.add(lastCommand)
    }
    return lastCommand !!.let { it.x2 to it.y2 }
}

private fun arcRelative(parameters: List<StringBuilder>, parameterCount: Int, position: Pair<Double, Double>, commands: MutableList<SvgPathCommand>): Pair<Double, Double> {
    check(parameterCount >= 7 && parameterCount % 7 == 0) { "invalid number of parameters $parameters" }
    var parameterIndex = 0
    var lastCommand: Arc? = null
    val x = position.first
    val y = position.second
    while (parameterIndex < parameterCount) {
        lastCommand = Arc(
            rx = parameters.toDouble(parameterIndex ++),
            ry = parameters.toDouble(parameterIndex ++),
            xAxisRotation = parameters.toDouble(parameterIndex ++),
            largeArcFlag = parameters.toInt(parameterIndex ++),
            sweepFlag = parameters.toInt(parameterIndex ++),
            x1 = x,
            y1 = y,
            x2 = x + parameters.toDouble(parameterIndex ++),
            y2 = y + parameters.toDouble(parameterIndex ++)
        )
        commands.add(lastCommand)
    }
    return lastCommand !!.let { it.x2 to it.y2 }
}