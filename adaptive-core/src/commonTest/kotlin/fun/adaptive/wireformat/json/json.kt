/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.wireformat.json

import `fun`.adaptive.wireformat.AbstractCollectionTest
import `fun`.adaptive.wireformat.AbstractDatetimeTest
import `fun`.adaptive.wireformat.AbstractKotlinTest
import `fun`.adaptive.wireformat.AbstractMessageTest
import `fun`.adaptive.wireformat.AbstractPolymorphicTest
import `fun`.adaptive.wireformat.api.Json
import `fun`.adaptive.wireformat.json.elements.JsonElement

class JsonKotlinTest : AbstractKotlinTest<JsonElement>(Json), JsonTestHelpers

class JsonCollectionTest : AbstractCollectionTest<JsonElement>(Json), JsonTestHelpers

class JsonDatetimeTest : AbstractDatetimeTest<JsonElement>(Json), JsonTestHelpers

class JsonPolymorphicTest : AbstractPolymorphicTest<JsonElement>(Json), JsonTestHelpers

class JsonMessageTest : AbstractMessageTest<JsonElement>(Json), JsonTestHelpers

