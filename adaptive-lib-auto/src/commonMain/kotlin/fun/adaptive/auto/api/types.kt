package `fun`.adaptive.auto.api

import `fun`.adaptive.auto.internal.backend.PropertyBackend
import `fun`.adaptive.auto.internal.backend.SetBackend
import `fun`.adaptive.auto.internal.instance.AutoCollection
//import `fun`.adaptive.auto.internal.backend.SetBackend
//import `fun`.adaptive.auto.internal.frontend.AdatClassFrontend
//import `fun`.adaptive.auto.internal.frontend.AdatClassListFrontend
//import `fun`.adaptive.auto.internal.frontend.FileFrontend
//import `fun`.adaptive.auto.internal.frontend.FolderFrontend
import `fun`.adaptive.auto.internal.instance.AutoInstance
import `fun`.adaptive.auto.internal.instance.AutoItem
import `fun`.adaptive.auto.internal.persistence.AutoCollectionPersistence
import `fun`.adaptive.auto.internal.persistence.AutoItemPersistence
import `fun`.adaptive.auto.internal.persistence.ItemFilePersistence
import `fun`.adaptive.auto.model.AutoConnectionInfo

typealias ItemBase<A> = AutoItem<PropertyBackend<A>, AutoItemPersistence<A>, A>
typealias CollectionBase<A> = AutoCollection<SetBackend<A>, AutoCollectionPersistence<A>, A>
typealias FileBase<A> = AutoInstance<PropertyBackend<A>, ItemFilePersistence<A>, A, A>
//typealias FolderBase<A> = AutoCollection<SetBackend<A>, FolderFrontend<A>, A>

typealias InfoFun<T> = (() -> AutoConnectionInfo<T>)?
typealias InfoFunSuspend<BE,FE,VT,IT> = (suspend (instance : AutoInstance<BE, FE, VT, IT>) -> AutoConnectionInfo<VT>)
typealias AutoGeneric = AutoInstance<*, *, *, *>