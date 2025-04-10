package `fun`.adaptive.code.kotlin.writer.model

import `fun`.adaptive.code.kotlin.writer.KotlinWriter

interface KwElement {

    fun toKotlin(writer : KotlinWriter) : KotlinWriter

}