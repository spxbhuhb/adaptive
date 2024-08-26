package `fun`.adaptive.adat.descriptor.kotlin.bool

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.descriptor.AdatDescriptor

@Adat
class BooleanDefault(
    val default : Boolean
) : AdatDescriptor()