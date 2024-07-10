/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat.descriptor

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.adat.AdatCompanion
import hu.simplexion.adaptive.adat.descriptor.constraint.maximum
import hu.simplexion.adaptive.adat.descriptor.constraint.minimum
import hu.simplexion.adaptive.adat.descriptor.info.default
import hu.simplexion.adaptive.adat.wireformat.AdatClassWireFormat

@Adat
class ValidationTestClass(
    var someInt: Int,
    var someBoolean: Boolean,
    var someIntListSet: Set<List<Int>>
) : AdatClass<ValidationTestClass> {

    constructor() : this(0, false, setOf())

    override val adatCompanion = Companion

    override fun description() {
        someInt minimum 5 maximum 10 default 7
    }
//
//    override fun validate(): InstanceValidationResult {
//        val result = InstanceValidationResult()
//
//        with(someInt) {
//            val property = getPropertyMetadata("someInt")
//            minimum(this, 5, property, result)
//            maximum(this, 7, property, result)
//        }
//
//        return result
//    }

    override fun equals(other: Any?): Boolean =
        adatEquals(other as? ValidationTestClass)

    override fun hashCode(): Int =
        adatHashCode()

    override fun getValue(index: Int): Any {
        return when (index) {
            0 -> someInt
            1 -> someBoolean
            2 -> someIntListSet
            else -> invalidIndex(index)
        }
    }

    override fun setValue(index: Int, value: Any?) {
        @Suppress("UNCHECKED_CAST")
        when (index) {
            0 -> someInt = value as Int
            1 -> someBoolean = value as Boolean
            2 -> someIntListSet = value as Set<List<Int>>
            else -> invalidIndex(index)
        }
    }

    companion object : AdatCompanion<ValidationTestClass> {

        override val wireFormatName: String
            get() = "hu.simplexion.adaptive.adat.descriptor.ValidationTestClass"

        override val adatMetadata = decodeMetadata("1/hu.simplexion.adaptive.adat.TestClass/someInt/0/I/someBoolean/1/Z/someIntListSet/2/Lkotlin.collections.Set<Lkotlin.collections.List<I>;>;")
        override val adatWireFormat = AdatClassWireFormat(this, adatMetadata)

        override fun newInstance() =
            ValidationTestClass()

    }

}