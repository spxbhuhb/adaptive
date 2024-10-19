package `fun`.adaptive.auto.api

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.wireformat.AdatClassWireFormat
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.backend.SetBackend
import `fun`.adaptive.auto.internal.frontend.FolderFrontend
import `fun`.adaptive.auto.internal.origin.OriginListBase
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.utility.exists
import `fun`.adaptive.wireformat.WireFormatProvider
import `fun`.adaptive.wireformat.api.Json
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

/**
 * Register a directory-based Auto list with the [worker].
 *
 * - backend: [SetBackend]
 * - frontend: [FolderFrontend]
 *
 * Initialization:
 *
 * - throw exception if the directory specified by [path] does not exist
 * - load each file in the directory with `FileFrontend.read`
 * - add each loaded item to the list
 *
 * [FolderFrontend] writes all changes to the files specified by [path] and [fileNameFun].
 *
 * After registration peers can use [autoList] to connect to the registered
 * list. To get the connection info needed for the [autoList] use the `connectInfo`
 * function of the returned frontend.
 *
 * - Adding/removing items from the list generate a new list instance (on all peers).
 * - Property changes (on any peer) generate a new list instance (on all peers).
 * - Property changes keep the non-affected properties as they are.
 *
 * Each new instance is validated by default, so code that use values
 * produced by [autoFolder] can safely use the validation result as it is
 * up-to-date all the time.
 *
 * Registers a cleanup handler into the session through [serviceContext] or
 * into the context if there is no session.
 *
 * **This function, the list and the instances are NOT thread safe.**
 *
 * @param    worker             Origins that support peer connections must specify pass an [AutoWorker] in this
 *                              parameter. Standalone origins may pass `null`.
 *
 * @param    fileNameFun        Generates the name of the files. The actual file name is not important, but it should
 *                              **not start with a '.' character** as those files are ignored at list load.
 *
 * @param    includeFun         When loading the items, this function is called to check if the given file should be
 *                              loaded into this list or not.
 *
 * @return   The Auto frontend of this list. Use this instance to change
 *           properties and to get connection info for the connecting peers.
 *
 * @throws   IllegalArgumentException  if the directory does not exist
 */
fun <A : AdatClass> autoFolder(
    worker: AutoWorker?,
    path: Path,
    fileNameFun: (itemId: ItemId, item: A) -> String,
    wireFormatProvider: WireFormatProvider = Json,
    includeFun: (Path) -> Boolean = { true },
    defaultWireFormat: AdatClassWireFormat<*>? = null,
    listener : AutoCollectionListener<A>? = null,
    serviceContext: ServiceContext? = null,
    handle: AutoHandle = AutoHandle(),
    register : Boolean = true,
    trace: Boolean = false,
): FolderBase<A> {

    require(path.exists()) { "missing directory: ${SystemFileSystem.resolve(path)}" }

    return OriginListBase(
        worker,
        handle,
        serviceContext,
        defaultWireFormat,
        trace = trace,
        register = register
    ) {

        if (listener != null) context.addListener(listener)

        backend = SetBackend(
            context,
            FolderFrontend.load(context, path, includeFun, wireFormatProvider)
        )

        frontend = FolderFrontend(
            backend,
            wireFormatProvider,
            path,
            fileNameFun
        )

        frontend.commit(initial = true, fromBackend = false)
    }

}