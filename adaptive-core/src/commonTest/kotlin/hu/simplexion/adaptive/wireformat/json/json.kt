/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat.json

import hu.simplexion.adaptive.wireformat.AbstractCollectionTest
import hu.simplexion.adaptive.wireformat.AbstractDatetimeTest
import hu.simplexion.adaptive.wireformat.AbstractKotlinTest
import hu.simplexion.adaptive.wireformat.AbstractMessageTest
import hu.simplexion.adaptive.wireformat.AbstractPolymorphicTest
import hu.simplexion.adaptive.wireformat.json.elements.JsonElement

class JsonKotlinTest : AbstractKotlinTest<JsonElement>(JsonWireFormatProvider()), JsonTestHelpers

class JsonCollectionTest : AbstractCollectionTest<JsonElement>(JsonWireFormatProvider()), JsonTestHelpers

class JsonDatetimeTest : AbstractDatetimeTest<JsonElement>(JsonWireFormatProvider()), JsonTestHelpers

class JsonPolymorphicTest : AbstractPolymorphicTest<JsonElement>(JsonWireFormatProvider()), JsonTestHelpers

class JsonMessageTest : AbstractMessageTest<JsonElement>(JsonWireFormatProvider()), JsonTestHelpers

