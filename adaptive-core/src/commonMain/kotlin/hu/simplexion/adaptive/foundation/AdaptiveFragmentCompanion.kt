/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.foundation

interface AdaptiveFragmentCompanion{

    val fragmentType : String

    fun newInstance(parent : AdaptiveFragment, index : Int): AdaptiveFragment

}