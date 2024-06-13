/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.lib.grapics.canvas

fun parsePath(path: String): List<Command> {

    if (path.isEmpty()) return emptyList()

    val commands = mutableListOf<Command>()
    val end = path.length
    var index = 0

    var command: Char? = null
    val parameters = mutableListOf(StringBuilder())
    var parameterIndex = 0
    var parameter = parameters[parameterIndex]
    var position = Pair(0f, 0f)
    var subPathStart = Pair(0f, 0f)

    while (index < end) {

        val char = path[index ++]

        when (char) {
            in '0' .. '9', '.', 'e', 'E', '-' -> {
                parameter.append(char)
            }

            ' ', ',' -> {
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
        build(command, parameters, parameterIndex + 1, position, subPathStart, commands)
    }

    return commands
}

private fun build(
    command: Char?,
    parameters: List<StringBuilder>,
    parameterCount: Int,
    position: Pair<Float, Float>,
    subPathStart: Pair<Float, Float>,
    commands: MutableList<Command>
): Pair<Float, Float> =
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

        'A' -> arcAbsolute(parameters, parameterCount, commands)
        'a' -> arcRelative(parameters, parameterCount, position, commands)

        'Z' -> closePath(position, subPathStart, commands)

        null -> position

        else -> throw IllegalArgumentException("unknown SVG path command: $command")
    }

private inline fun List<StringBuilder>.toFloat(index: Int): Float =
    this[index].toString().toFloat()

private inline fun List<StringBuilder>.toInt(index: Int): Int =
    this[index].toString().toInt()

private fun moveToAbsolute(parameters: List<StringBuilder>, parameterCount: Int, commands: MutableList<Command>): Pair<Float, Float> {
    var parameterIndex = 0

    check(parameterCount % 2 == 0) { "invalid number of parameters" }

    var x = parameters.toFloat(parameterIndex ++)
    var y = parameters.toFloat(parameterIndex ++)

    commands.add(MoveTo(x, y))

    while (parameterIndex < parameterCount) {

        x = parameters.toFloat(parameterIndex ++)
        y = parameters.toFloat(parameterIndex ++)

        commands.add(LineTo(x, y))
    }

    return x to y
}

private fun moveToRelative(parameters: List<StringBuilder>, parameterCount: Int, point: Pair<Float, Float>, commands: MutableList<Command>): Pair<Float, Float> {
    var parameterIndex = 0

    check(parameterCount % 2 == 0) { "invalid number of parameters" }

    var x = point.first + parameters.toFloat(parameterIndex ++)
    var y = point.second + parameters.toFloat(parameterIndex ++)

    commands.add(MoveTo(x, y))

    while (parameterIndex < parameterCount) {

        x += parameters.toFloat(parameterIndex ++)
        y += parameters.toFloat(parameterIndex ++)

        commands.add(LineTo(x, y))
    }

    return x to y
}

private fun closePath(point: Pair<Float, Float>, subPathStart: Pair<Float, Float>, commands: MutableList<Command>): Pair<Float, Float> {
    commands += ClosePath(point.first, point.second, subPathStart.first, subPathStart.second)
    return subPathStart
}

private fun lineToAbsolute(parameters: List<StringBuilder>, parameterCount: Int, commands: MutableList<Command>): Pair<Float, Float> {
    var parameterIndex = 0

    check(parameterCount >= 2 && parameterCount % 2 == 0) { "invalid number of parameters" }

    var x = 0f
    var y = 0f

    while (parameterIndex < parameterCount) {

        x = parameters.toFloat(parameterIndex ++)
        y = parameters.toFloat(parameterIndex ++)

        commands.add(LineTo(x, y))
    }

    return x to y
}

private fun lineToRelative(parameters: List<StringBuilder>, parameterCount: Int, point: Pair<Float, Float>, commands: MutableList<Command>): Pair<Float, Float> {
    var parameterIndex = 0

    check(parameterCount >= 2 && parameterCount % 2 == 0) { "invalid number of parameters" }

    var x = point.first
    var y = point.second

    while (parameterIndex < parameterCount) {

        x += parameters.toFloat(parameterIndex ++)
        y += parameters.toFloat(parameterIndex ++)

        commands.add(LineTo(x, y))
    }

    return x to y
}

private fun horizontalLineToAbsolute(parameters: List<StringBuilder>, parameterCount: Int, position: Pair<Float, Float>, commands: MutableList<Command>): Pair<Float, Float> {
    var lastIndex = position.first

    for (i in 0 until parameterCount) {
        val x = parameters.toFloat(i)
        commands.add(LineTo(x, position.second))
        lastIndex = x
    }

    return lastIndex to position.second
}

private fun horizontalLineToRelative(parameters: List<StringBuilder>, parameterCount: Int, position: Pair<Float, Float>, commands: MutableList<Command>): Pair<Float, Float> {
    var lastRelativeValue = position.first

    for (i in 0 until parameterCount) {
        lastRelativeValue += parameters.toFloat(i)
        commands.add(LineTo(lastRelativeValue, position.second))
    }

    return lastRelativeValue to position.second
}

private fun verticalLineToAbsolute(parameters: List<StringBuilder>, parameterCount: Int, position: Pair<Float, Float>, commands: MutableList<Command>): Pair<Float, Float> {
    var lastY = position.second

    for (i in 0 until parameterCount) {
        val y = parameters.toFloat(i)
        commands.add(LineTo(position.first, y))
        lastY = y
    }

    return position.first to lastY
}

private fun verticalLineToRelative(parameters: List<StringBuilder>, parameterCount: Int, position: Pair<Float, Float>, commands: MutableList<Command>): Pair<Float, Float> {
    var lastRelativeValue = position.second

    for (i in 0 until parameterCount) {
        lastRelativeValue += parameters[i].toString().toFloat()
        commands.add(LineTo(position.first, lastRelativeValue))
    }

    return position.first to lastRelativeValue
}

private fun cubicCurveAbsolute(parameters: List<StringBuilder>, parameterCount: Int, commands: MutableList<Command>): Pair<Float, Float> {
    check(parameterCount >= 6 && parameterCount % 6 == 0) { "invalid number of parameters" }

    var parameterIndex = 0
    var lastCommand: CubicCurve? = null

    while (parameterIndex < parameterCount) {
        lastCommand = CubicCurve(
            parameters.toFloat(parameterIndex ++),
            parameters.toFloat(parameterIndex ++),
            parameters.toFloat(parameterIndex ++),
            parameters.toFloat(parameterIndex ++),
            parameters.toFloat(parameterIndex ++),
            parameters.toFloat(parameterIndex ++)
        )
        commands.add(lastCommand)
    }

    return lastCommand !!.let { it.x to it.y }
}

private fun cubicCurveRelative(parameters: List<StringBuilder>, parameterCount: Int, position: Pair<Float, Float>, commands: MutableList<Command>): Pair<Float, Float> {
    check(parameterCount >= 6 && parameterCount % 6 == 0) { "invalid number of parameters" }

    var parameterIndex = 0
    var x = position.first
    var y = position.second
    var x1: Float
    var y1: Float
    var x2: Float
    var y2: Float

    while (parameterIndex < parameterCount) {
        x1 = x + parameters.toFloat(parameterIndex ++)
        y1 = y + parameters.toFloat(parameterIndex ++)
        x2 = x + parameters.toFloat(parameterIndex ++)
        y2 = y + parameters.toFloat(parameterIndex ++)
        x += parameters.toFloat(parameterIndex ++)
        y += parameters.toFloat(parameterIndex ++)

        commands.add(CubicCurve(x1, y1, x2, y2, x, y))
    }

    return x to y
}

private fun cubicCurveSmoothAbsolute(parameters: List<StringBuilder>, parameterCount: Int, commands: MutableList<Command>): Pair<Float, Float> {
    check(parameterCount >= 4 && parameterCount % 4 == 0) { "invalid number of parameters" }

    var parameterIndex = 0
    var lastCommand: CubicCurveSmooth? = null

    while (parameterIndex < parameterCount) {
        lastCommand = CubicCurveSmooth(
            parameters.toFloat(parameterIndex ++),
            parameters.toFloat(parameterIndex ++),
            parameters.toFloat(parameterIndex ++),
            parameters.toFloat(parameterIndex ++)
        )
        commands.add(lastCommand)
    }

    return lastCommand !!.let { it.x to it.y }
}

private fun cubicCurveSmoothRelative(parameters: List<StringBuilder>, parameterCount: Int, position: Pair<Float, Float>, commands: MutableList<Command>): Pair<Float, Float> {
    check(parameterCount >= 4 && parameterCount % 4 == 0) { "invalid number of parameters" }

    var parameterIndex = 0
    var x = position.first
    var y = position.second
    var x2: Float
    var y2: Float

    while (parameterIndex < parameterCount) {
        x2 = x + parameters.toFloat(parameterIndex ++)
        y2 = y + parameters.toFloat(parameterIndex ++)
        x += parameters.toFloat(parameterIndex ++)
        y += parameters.toFloat(parameterIndex ++)

        commands.add(CubicCurveSmooth(x2, y2, x, y))
    }

    return x to y
}

private fun quadraticCurveAbsolute(parameters: List<StringBuilder>, parameterCount: Int, commands: MutableList<Command>): Pair<Float, Float> {
    check(parameterCount >= 4 && parameterCount % 4 == 0) { "invalid number of parameters" }

    var parameterIndex = 0
    var lastCommand: QuadraticCurve? = null

    while (parameterIndex < parameterCount) {
        lastCommand = QuadraticCurve(
            parameters.toFloat(parameterIndex ++),
            parameters.toFloat(parameterIndex ++),
            parameters.toFloat(parameterIndex ++),
            parameters.toFloat(parameterIndex ++)
        )
        commands.add(lastCommand)
    }

    return lastCommand !!.let { it.x to it.y }
}

private fun quadraticCurveRelative(parameters: List<StringBuilder>, parameterCount: Int, position: Pair<Float, Float>, commands: MutableList<Command>): Pair<Float, Float> {
    check(parameterCount >= 4 && parameterCount % 4 == 0) { "invalid number of parameters" }

    var parameterIndex = 0
    var x = position.first
    var y = position.second
    var x1: Float
    var y1: Float

    while (parameterIndex < parameterCount) {
        x1 = x + parameters.toFloat(parameterIndex ++)
        y1 = y + parameters.toFloat(parameterIndex ++)
        x += parameters.toFloat(parameterIndex ++)
        y += parameters.toFloat(parameterIndex ++)

        commands.add(QuadraticCurve(x1, y1, x, y))
    }

    return x to y
}

private fun quadraticCurveSmoothAbsolute(parameters: List<StringBuilder>, parameterCount: Int, commands: MutableList<Command>): Pair<Float, Float> {
    check(parameterCount >= 2 && parameterCount % 2 == 0) { "invalid number of parameters" }

    var parameterIndex = 0
    var lastCommand: QuadraticCurveSmooth? = null

    while (parameterIndex < parameterCount) {
        lastCommand = QuadraticCurveSmooth(
            parameters.toFloat(parameterIndex ++),
            parameters.toFloat(parameterIndex ++)
        )
        commands.add(lastCommand)
    }

    return lastCommand !!.let { it.x to it.y }
}

private fun quadraticCurveSmoothRelative(parameters: List<StringBuilder>, parameterCount: Int, position: Pair<Float, Float>, commands: MutableList<Command>): Pair<Float, Float> {
    check(parameterCount >= 2 && parameterCount % 2 == 0) { "invalid number of parameters" }

    var parameterIndex = 0
    var x = position.first + parameters.toFloat(parameterIndex ++)
    var y = position.second + parameters.toFloat(parameterIndex ++)

    commands.add(QuadraticCurveSmooth(x, y))

    return x to y
}

private fun arcAbsolute(parameters: List<StringBuilder>, parameterCount: Int, commands: MutableList<Command>): Pair<Float, Float> {
    check(parameterCount >= 7 && parameterCount % 7 == 0) { "invalid number of parameters" }
    var parameterIndex = 0
    var lastCommand: Arc? = null
    while (parameterIndex < parameterCount) {
        lastCommand = Arc(
            parameters.toFloat(parameterIndex ++),
            parameters.toFloat(parameterIndex ++),
            parameters.toFloat(parameterIndex ++),
            parameters.toInt(parameterIndex ++),
            parameters.toInt(parameterIndex ++),
            parameters.toFloat(parameterIndex ++),
            parameters.toFloat(parameterIndex ++)
        )
        commands.add(lastCommand)
    }
    return lastCommand !!.let { it.x to it.y }
}

private fun arcRelative(parameters: List<StringBuilder>, parameterCount: Int, position: Pair<Float, Float>, commands: MutableList<Command>): Pair<Float, Float> {
    check(parameterCount >= 7 && parameterCount % 7 == 0) { "invalid number of parameters" }
    var parameterIndex = 0
    var lastCommand: Arc? = null
    val x = position.first
    val y = position.second
    while (parameterIndex < parameterCount) {
        lastCommand = Arc(
            parameters.toFloat(parameterIndex ++),
            parameters.toFloat(parameterIndex ++),
            parameters.toFloat(parameterIndex ++),
            parameters.toInt(parameterIndex ++),
            parameters.toInt(parameterIndex ++),
            x + parameters.toFloat(parameterIndex ++),
            y + parameters.toFloat(parameterIndex ++)
        )
        commands.add(lastCommand)
    }
    return lastCommand !!.let { it.x to it.y }
}