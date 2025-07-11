package `fun`.adaptive.sandbox.recipe.ui.filter

import `fun`.adaptive.sandbox.support.ExampleEnum
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

    val store = storeFor<QuickFilterModel<ExampleEnum>> {
        QuickFilterModel(
            ExampleEnum.V1,
            listOf(ExampleEnum.V1, ExampleEnum.V2, ExampleEnum.V3, ExampleEnum.V4, ExampleEnum.V5),
            { it.name }
        )
    }

    var model = observe { store }

    quickFilter(model) { store.value = model.copy(selected = it) }
}