/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat.protobuf

import hu.simplexion.adaptive.wireformat.AbstractCollectionTest
import hu.simplexion.adaptive.wireformat.AbstractDatetimeTest
import hu.simplexion.adaptive.wireformat.AbstractKotlinTest

class ProtoKotlinTest : AbstractKotlinTest<ProtoRecord>(ProtoWireFormatProvider()), ProtoTestHelpers

class ProtoCollectionTest : AbstractCollectionTest<ProtoRecord>(ProtoWireFormatProvider()), ProtoTestHelpers

class ProtoDatetimeTest : AbstractDatetimeTest<ProtoRecord>(ProtoWireFormatProvider()), ProtoTestHelpers