/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.canvas.path

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.graphics.canvas.platform.ActualPath

abstract class PathCommand : AdatClass {
    abstract fun apply(path: ActualPath)
}