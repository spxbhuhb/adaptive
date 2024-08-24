/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.svg.parse

import `fun`.adaptive.graphics.svg.instruction.*
import `fun`.adaptive.utility.firstNotOrNull

fun parseTransform(source: String): List<SvgTransform> {

    if (source.isEmpty()) return emptyList()

    val transforms = mutableListOf<SvgTransform>()
    val end = source.length
    var index = 0

    var transform: String? = null
    val parameters = mutableListOf(StringBuilder())
    var parameterIndex = 0
    var parameter = parameters[parameterIndex]

    while (index < end) {

        val char = source[index ++]

        when {
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
                build(transform, parameters, parameterCount, transforms)

                index = source.firstNotOrNull(index, end) { it.isWhitespace() || it == ',' } ?: break
                val startIndex = if (transform == null) index - 1 else index

                index = source.firstNotOrNull(index, end) { it != '(' } ?: break

                transform = source.substring(startIndex, index)
                index ++ // step over '('

                parameterIndex = 0
                parameter = parameters[parameterIndex]
                parameter.clear()
            }
        }
    }

    return transforms
}

private fun build(
    transform: String?,
    parameters: List<StringBuilder>,
    parameterCount: Int,
    transforms: MutableList<SvgTransform>
) {
    when (transform) {
        "translate" -> translate(parameters, parameterCount, transforms)
        "scale" -> scale(parameters, parameterCount, transforms)
        "rotate" -> rotate(parameters, parameterCount, transforms)
        "matrix" -> matrix(parameters, parameterCount, transforms)
        "skewX" -> skewX(parameters, parameterCount, transforms)
        "skewY" -> skewY(parameters, parameterCount, transforms)
        null -> return
        else -> throw IllegalArgumentException("unknown SVG transform: >$transform<")
    }
}

fun translate(parameters: List<StringBuilder>, parameterCount: Int, transforms: MutableList<SvgTransform>) {
    transforms += when (parameterCount) {
        1 -> Translate(parameters.toDouble(0), 0.0)
        2 -> Translate(parameters.toDouble(0), parameters.toDouble(1))
        else -> throw IllegalArgumentException("invalid parameter count $parameterCount for 'translate'")
    }
}

fun scale(parameters: List<StringBuilder>, parameterCount: Int, transforms: MutableList<SvgTransform>) {
    transforms += when (parameterCount) {
        1 -> {
            val amount = parameters.toDouble(0)
            Scale(amount, amount)
        }

        2 -> Scale(parameters.toDouble(0), parameters.toDouble(1))
        else -> throw IllegalArgumentException("invalid parameter count $parameterCount for 'scale'")
    }
}

fun rotate(parameters: List<StringBuilder>, parameterCount: Int, transforms: MutableList<SvgTransform>) {
    transforms += when (parameterCount) {
        1 -> Rotate(parameters.toDouble(0), 0.0, 0.0)
        3 -> Rotate(parameters.toDouble(0), parameters.toDouble(1), parameters.toDouble(2))
        else -> throw IllegalArgumentException("invalid parameter count $parameterCount for 'rotate'")
    }
}

fun matrix(parameters: List<StringBuilder>, parameterCount: Int, transforms: MutableList<SvgTransform>) {
    if (parameterCount != 6) throw IllegalArgumentException("invalid parameter count $parameterCount for 'rotate'")
    transforms += Matrix(
        parameters.toDouble(0),
        parameters.toDouble(1),
        parameters.toDouble(2),
        parameters.toDouble(3),
        parameters.toDouble(4),
        parameters.toDouble(5)
    )
}

fun skewX(parameters: List<StringBuilder>, parameterCount: Int, transforms: MutableList<SvgTransform>) {
    if (parameterCount != 1) throw IllegalArgumentException("invalid parameter count $parameterCount for 'skewX'")
    transforms += SkewX(parameters.toDouble(0))
}

fun skewY(parameters: List<StringBuilder>, parameterCount: Int, transforms: MutableList<SvgTransform>) {
    if (parameterCount != 1) throw IllegalArgumentException("invalid parameter count $parameterCount for 'skewY'")
    transforms += SkewY(parameters.toDouble(0))
}

