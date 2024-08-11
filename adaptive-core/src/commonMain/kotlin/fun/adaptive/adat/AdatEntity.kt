/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.adat

import `fun`.adaptive.utility.UUID

interface AdatEntity<A : AdatClass<A>> : AdatClass<A> {

    val id: UUID<A>

}