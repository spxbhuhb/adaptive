/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.wireformat.signature

import `fun`.adaptive.wireformat.WireFormat

class WireFormatTypeArgument<T>(
    val wireFormat: WireFormat<T>,
    val nullable : Boolean
)