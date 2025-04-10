/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.wireformat.protobuf

import `fun`.adaptive.wireformat.AbstractCollectionTest
import `fun`.adaptive.wireformat.AbstractDatetimeTest
import `fun`.adaptive.wireformat.AbstractKotlinTest
import `fun`.adaptive.wireformat.AbstractMessageTest
import `fun`.adaptive.wireformat.api.Proto
import `fun`.adaptive.wireformat.json.JsonTestHelpers
import `fun`.adaptive.wireformat.json.elements.JsonElement

class ProtoKotlinTest : AbstractKotlinTest<ProtoRecord>(Proto), ProtoTestHelpers

class ProtoCollectionTest : AbstractCollectionTest<ProtoRecord>(Proto), ProtoTestHelpers

class ProtoDatetimeTest : AbstractDatetimeTest<ProtoRecord>(Proto), ProtoTestHelpers

class ProtoMessageTest : AbstractMessageTest<JsonElement>(Proto), JsonTestHelpers
