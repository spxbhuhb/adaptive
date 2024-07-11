/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat.signature

import hu.simplexion.adaptive.wireformat.WireFormat

/**
 * @property  wireFormat  when null, the type is polymorphic
 *                        FIXME no check on the polymorphism at the moment
 */
class WireFormatTypeArgument<T>(
    val wireFormat: WireFormat<T>?,
    val nullable : Boolean
)