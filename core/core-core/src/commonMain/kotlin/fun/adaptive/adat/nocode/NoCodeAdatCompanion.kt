package `fun`.adaptive.adat.nocode

import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.adat.metadata.AdatClassMetadata
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat

class NoCodeAdatCompanion(
    override val adatMetadata: AdatClassMetadata
) : AdatCompanion<NoCodeAdatClass> {

    override val wireFormatName
        get() = adatMetadata.name

    override val adatWireFormat by lazy { AdatClassWireFormat(this, adatMetadata) }

    override val adatDescriptors by lazy { adatMetadata.generateDescriptors() }

    override fun newInstance(values: Array<Any?>): NoCodeAdatClass =
        NoCodeAdatClass(this, values)

}