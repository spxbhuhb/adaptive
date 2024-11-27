package `fun`.adaptive.auto.internal.instance

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.store.AdatStore
import `fun`.adaptive.auto.model.ItemId

class AutoAdatStore<IT : AdatClass>(
    val instance : AutoInstance<*,*,*,IT>
) : AdatStore<IT>() {

    override fun update(instance: IT, path: Array<String>, value: Any?) {
        val ctx = checkNotNull(instance.adatContext) { "missing adat context for $instance"}
        val id = checkNotNull(ctx.id as? ItemId) { "invalid item id ${ctx.id} in $instance"}
        this.instance.localUpdate(id, listOf(path[0] to value))
    }

    override fun update(new: IT) {
        check(instance is AutoItem<*, *, *>)
        instance.localUpdate(
            instance.backend.itemId,
            new.adatCompanion.adatDescriptors.map {
                it.property.name to new.getValue(it.property.index)
            }
        )
    }

}