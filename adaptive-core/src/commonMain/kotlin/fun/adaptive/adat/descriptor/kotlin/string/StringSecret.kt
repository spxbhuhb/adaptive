package `fun`.adaptive.adat.descriptor.kotlin.string

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.descriptor.AdatDescriptor

@Adat
class StringSecret(
    val isSecret : Boolean
) : AdatDescriptor()