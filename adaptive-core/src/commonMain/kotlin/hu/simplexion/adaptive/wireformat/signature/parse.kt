/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat.signature

import hu.simplexion.adaptive.utility.peek
import hu.simplexion.adaptive.utility.pop
import hu.simplexion.adaptive.utility.push

data class Type(
    var name: String = "",
    var nullable: Boolean = false,
    val generics: MutableList<Type> = mutableListOf()
)

enum class TokenType {
    Name,
    Open,
    Close,
    Nullable
}

data class Token(
    val type: TokenType,
    val value: String
)

fun tokenizeSignature(signature: String): List<Token> {

    val tokens = mutableListOf<Token>()
    val currentName = StringBuilder()
    var inName = false

    for (char in signature) {
        when (char) {
            'L' -> {
                if (inName) {
                    currentName.append(char)
                } else {
                    check(currentName.isEmpty())
                    inName = true
                }
            }

            ';' -> {
                if (inName) {
                    tokens.add(Token(TokenType.Name, currentName.toString()))
                    currentName.clear()
                    inName = false
                }
            }

            '<' -> {
                check(inName)
                tokens.add(Token(TokenType.Name, currentName.toString()))
                tokens.add(Token(TokenType.Open, ""))
                currentName.clear()
                inName = false
            }

            '>' -> {
                tokens.add(Token(TokenType.Close, ""))
            }

            '[' -> {
                currentName.append(char)
            }

            '+' -> {
                currentName.append(char)
            }

            '?' -> {
                check(!inName)
                tokens.add(Token(TokenType.Nullable, ""))
            }

            else -> {
                if (inName) {
                    currentName.append(char)
                } else {
                    currentName.append(char)
                    tokens.add(Token(TokenType.Name, currentName.toString()))
                    currentName.clear()
                }
            }
        }
    }

    return tokens
}

fun parseSignature(signature: String): Type {
    val stack = mutableListOf(Type())

    for (token in tokenizeSignature(signature)) {
        when (token.type) {
            TokenType.Name -> stack.peek().generics += (Type(token.value))
            TokenType.Open -> stack.push(stack.peek().generics.last())
            TokenType.Close -> stack.pop()
            TokenType.Nullable -> stack.peek().nullable = true
        }
    }

    return stack.pop().generics.first()
}
