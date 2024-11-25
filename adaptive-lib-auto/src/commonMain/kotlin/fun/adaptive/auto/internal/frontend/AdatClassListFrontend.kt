package `fun`.adaptive.auto.internal.frontend

//import `fun`.adaptive.auto.internal.backend.SetBackend
//
//open class AdatClassListFrontend<IT : AdatClass>(
//    instance: AutoInstance<AutoCollectionBackend<IT>, AutoCollectionFrontend<IT>, Collection<IT>, IT>,
//    val collectionBackend: SetBackend<IT>,
//) : AutoCollectionFrontend<IT>(instance) {
//
//    override val persistent: Boolean
//        get() = false
//
//    var classFrontends = mutableMapOf<ItemId, AdatClassFrontend<IT>>()
//
//    override val values: Collection<IT>
//        get() = checkNotNull(values)
//
//    override var valueOrNull: Collection<IT>? = null
//        protected set
//
//    var cachedList : List<IT> = emptyList()
//
//    override fun load(): Pair<AutoConnectionInfo<Collection<IT>>?, Collection<IT>?> {
//        throw UnsupportedOperationException("AdatClassListFrontend does not persist values, so it cannot load them")
//    }
//
//    override fun commit(itemBackend: AutoItemBackend<IT>?, initial: Boolean, fromPeer: Boolean) {
//        cachedList = collectionBackend.data.active().map { getItemFrontend(it).value }
//
//        if (initial) {
//            instance.onInit(values, fromPeer)
//        } else {
//            instance.onChange(values, fromPeer)
//        }
//    }
//
//    override fun commit(itemId: ItemId, newValue: IT, oldValue: IT?, initial: Boolean, fromBackend: Boolean) {
//        // TODO check AdatClassListFrontend.commit, should we throw an exception when there is no item?
//        // TODO optimise AdatClassListFrontend.commit
//        @Suppress("UNCHECKED_CAST")
//        val index = cachedList.indexOfFirst { (it.adatContext as AdatContext<ItemId>).id == itemId }
//        if (index == - 1) return
//
//        valueOrNull = cachedList.subList(0, index) + newValue + cachedList.subList(index + 1, values.size)
//
//        instance.onChange(newValue, oldValue, fromBackend)
//    }
//
//    operator fun plusAssign(item: IT) {
//        add(item)
//    }
//
//    override fun add(item: IT) {
//        collectionBackend.add(item, null, true)
//    }
//
//    operator fun minusAssign(item: IT) {
//        remove(item.adatContext !!.id as ItemId)
//    }
//
//    override fun remove(itemId: ItemId) {
//        collectionBackend.localRemove(itemId, true)
//    }
//
//    override fun remove(selector: (IT) -> Boolean) {
//        for (item in values.filter(selector)) {
//            collectionBackend.localRemove(itemId(item), true)
//        }
//    }
//
//    fun update(itemId: ItemId, propertyName: String, propertyValue: Any?) {
//        collectionBackend.data[itemId]?.localUpdate(propertyName, propertyValue)
//    }
//
//    fun replace(original: AdatClass, new: AdatClass) {
//        getItemFrontend(itemId(original)).update(new)
//    }
//
//    fun itemId(instance: AdatClass) =
//        instance.adatContext !!.id as ItemId
//
//    override fun update(instance: AdatClass, path: Array<String>, value: Any?) {
//        // FIXME only single properties are handled b y AdatClassListFrontend
//        check(path.size == 1) { "multi-level paths are not implemented yet" }
//        update(itemId(instance), path[0], value)
//    }
//
//    // TODO optimize AdatClassListFrontend.getFrontend - I think `newInstance` is unnecessary here
//    open fun getItemFrontend(itemId: ItemId) =
//        classFrontends.getOrPut(itemId) {
//
//            val propertyBackend = checkNotNull(collectionBackend.data[itemId])
//            val wireFormat = propertyBackend.wireFormat
//
//            @Suppress("UNCHECKED_CAST")
//            AdatClassFrontend<IT>(
//                instance,
//                wireFormat.newInstance(propertyBackend.values) as IT,
//                this,
//                itemId,
//            )
//                .also { it.backend = propertyBackend }
//        }
//
//}