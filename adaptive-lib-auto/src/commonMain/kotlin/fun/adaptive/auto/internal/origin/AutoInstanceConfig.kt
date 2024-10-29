package `fun`.adaptive.auto.internal.origin

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.auto.api.AutoCollectionListener
import `fun`.adaptive.auto.api.AutoItemListener
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.service.ServiceContext

/**
 * This class describes the basic information needed to set up an auto instance.
 *
 * Most fields are use-case dependent, different backend and frontends
 * may use or not use fields in this class.
 *
 * Also, this class does not store everything that is required to configure an
 * instance for a specific use case. For example, file and SQL frontends might
 * need more information about the location of data.
 *
 * The instance of this class **SHOULD NOT BE KEPT** in memory as it c
 */
class AutoInstanceSetupData<VT, IT : AdatClass>(
    val origin: Boolean,
    val persistent: Boolean,
    val service: Boolean,
    val value: VT?,
    val info: AutoConnectionInfo<VT>?,
    val infoFun: (suspend () -> AutoConnectionInfo<VT>)?,
    val companion: AdatCompanion<IT>,
    val itemListener: AutoItemListener<IT>,
    val collectionListener: AutoCollectionListener<IT>?,
    val worker: AutoWorker?,
    val serviceContext: ServiceContext?,
    val trace: Boolean,
)