package `fun`.adaptive.kotlin.writer.model

import `fun`.adaptive.kotlin.writer.KotlinWriter

interface KwElement {

    fun toKotlin(writer : KotlinWriter) : KotlinWriter

}