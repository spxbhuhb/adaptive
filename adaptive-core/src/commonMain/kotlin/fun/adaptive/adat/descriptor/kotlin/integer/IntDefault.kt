package `fun`.adaptive.adat.descriptor.kotlin.integer

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.descriptor.AdatDescriptor

@Adat
class IntDefault(
    val default : Int
) : AdatDescriptor()