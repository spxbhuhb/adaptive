package `fun`.adaptive.auto.internal.frontend

//import `fun`.adaptive.adat.AdatClass
//import `fun`.adaptive.adat.toArray
//import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
//import `fun`.adaptive.auto.internal.backend.PropertyBackend
//import `fun`.adaptive.auto.internal.backend.SetBackend
//import `fun`.adaptive.auto.internal.frontend.FileFrontend.Companion.write
//import `fun`.adaptive.auto.model.ItemId
//import `fun`.adaptive.utility.exists
//import `fun`.adaptive.wireformat.WireFormatProvider
//import kotlinx.io.files.Path
//import kotlinx.io.files.SystemFileSystem
//
//class FolderFrontend<A : AdatClass>(
//    backend: SetBackend<A>,
//    val wireFormatProvider: WireFormatProvider,
//    val path: Path,
//    val fileNameFun: (itemId: ItemId, item: A) -> String
//) : AdatClassListFrontend<A>(
//    backend
//) {
//
//    fun pathFor(itemId: ItemId, instance: A) =
//        Path(path, fileNameFun(itemId, instance))
//
//    // TODO optimize AdatClassListFrontend.getFrontend - I think `newInstance` is unnecessary here
//    override fun getItemFrontend(itemId: ItemId) =
//
//        classFrontends.getOrPut(itemId) {
//
//            val propertyBackend = checkNotNull(backend.data[itemId])
//
//            @Suppress("UNCHECKED_CAST")
//            val wireFormat = propertyBackend.wireFormat as AdatClassWireFormat<A>
//
//            val instance = wireFormat.newInstance(propertyBackend.values)
//            val path = pathFor(itemId, instance)
//
//            if (! path.exists()) {
//                write(path, wireFormatProvider, itemId, propertyBackend.propertyTimes, instance)
//            }
//
//            FileFrontend(
//                propertyBackend,
//                wireFormat,
//                itemId,
//                instance,
//                this,
//                wireFormatProvider,
//                path
//            )
//                .also { propertyBackend.frontend = it }
//        }
//
//    companion object {
//
//        fun <A : AdatClass> load(
//            context: BackendContext<A>,
//            path: Path,
//            includeFun : (path: Path) -> Boolean,
//            wireFormatProvider: WireFormatProvider
//        ): MutableMap<ItemId, PropertyBackend<A>> {
//            require(path.exists()) { "path $path does not exist" }
//
//            val result = mutableMapOf<ItemId, PropertyBackend<A>>()
//
//            SystemFileSystem.list(path).forEach {
//
//                // ignore hidden files
//                if (it.name.startsWith(".")) return@forEach
//                if (! includeFun(it)) return@forEach
//
//                val (itemId, propertyTimes, instance) = FileFrontend.read<A>(it, wireFormatProvider)
//                checkNotNull(itemId)
//
//                check(itemId !in result) { "duplicated item id $itemId in $it" }
//
//                result[itemId] = PropertyBackend(
//                    context,
//                    itemId,
//                    instance.adatCompanion.wireFormatName,
//                    instance.toArray(),
//                    propertyTimes
//                )
//            }
//
//            return result
//        }
//    }
//}