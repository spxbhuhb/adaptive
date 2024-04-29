package hu.simplexion.z2.wireformat.protobuf

import hu.simplexion.z2.wireformat.AbstractCollectionTest
import hu.simplexion.z2.wireformat.AbstractDatetimeTest
import hu.simplexion.z2.wireformat.AbstractKotlinTest

class ProtoKotlinTest : AbstractKotlinTest<ProtoRecord>(ProtoWireFormatProvider()), ProtoTestHelpers

class ProtoCollectionTest : AbstractCollectionTest<ProtoRecord>(ProtoWireFormatProvider()), ProtoTestHelpers

class ProtoDatetimeTest : AbstractDatetimeTest<ProtoRecord>(ProtoWireFormatProvider()), ProtoTestHelpers