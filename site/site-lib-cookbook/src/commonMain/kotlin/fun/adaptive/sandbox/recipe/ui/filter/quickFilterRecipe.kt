package `fun`.adaptive.sandbox.recipe.ui.filter

import `fun`.adaptive.sandbox.support.E
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.verticalScroll
import `fun`.adaptive.ui.filter.QuickFilterModel
import `fun`.adaptive.ui.filter.quickFilter
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun quickFilterRecipe() : AdaptiveFragment {
    column {
        maxSize .. verticalScroll .. gap { 16.dp }

        simpleQuickFilter()
    }

    return fragment()
}
@Adaptive
fun simpleQuickFilter() {

    val store = storeFor<QuickFilterModel<E>> {
        QuickFilterModel(
            E.V1,
            listOf(E.V1, E.V2, E.V3, E.V4, E.V5),
            { it.name }
        )
    }

    var model = observe { store }

    quickFilter(model) { store.value = model.copy(selected = it) }
}