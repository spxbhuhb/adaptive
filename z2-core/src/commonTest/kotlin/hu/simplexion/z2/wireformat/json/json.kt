package hu.simplexion.z2.wireformat.json

import hu.simplexion.z2.wireformat.AbstractCollectionTest
import hu.simplexion.z2.wireformat.AbstractDatetimeTest
import hu.simplexion.z2.wireformat.AbstractKotlinTest
import hu.simplexion.z2.wireformat.json.elements.JsonElement

class JsonKotlinTest : AbstractKotlinTest<JsonElement>(JsonWireFormatProvider()), JsonTestHelpers

class JsonCollectionTest : AbstractCollectionTest<JsonElement>(JsonWireFormatProvider()), JsonTestHelpers

class JsonDatetimeTest : AbstractDatetimeTest<JsonElement>(JsonWireFormatProvider()), JsonTestHelpers
