package `fun`.adaptive.auto.internal.instance

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.store.AdatStore
import `fun`.adaptive.auto.model.itemId
import kotlin.reflect.KProperty

class AutoAdatStore<IT : AdatClass>(
    val instance : AutoInstance<*,*,*,IT>
) : AdatStore<IT>() {

    override fun update(original: IT, path: Array<String>, value: Any?) {
        this.instance.localUpdate(original.itemId, listOf(path[0] to value))
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

    override fun update(original: IT, changes: Array<out Pair<KProperty<*>, *>>): IT =
        this.instance.localUpdate(original.itemId, changes.map { it.first.name to it.second })

}