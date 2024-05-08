/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.meta

import hu.simplexion.adaptive.base.AdaptiveAdapter
import hu.simplexion.adaptive.base.AdaptiveFragment
import hu.simplexion.adaptive.meta.model.AdaptiveFragmentMetaData

typealias AdaptiveMetaBuildFunRegistry<BT> = Map<String,AdaptiveMetaBuildFun<BT>>

typealias AdaptiveMetaPatchFunRegistry = Map<String,AdaptiveMetaPatchFun>

typealias AdaptiveMetaFragmentData = Map<Int, AdaptiveFragmentMetaData>

typealias AdaptiveMetaBuildFun<BT> = (adapter : AdaptiveAdapter<BT>, parent : AdaptiveFragment<BT>, index : Int) -> AdaptiveFragment<BT>

typealias AdaptiveMetaPatchFun = (data : Any) -> AdaptivePatchInstructionImpl