package `fun`.adaptive.auto.api

import `fun`.adaptive.auto.internal.backend.PropertyBackend
import `fun`.adaptive.auto.internal.backend.SetBackend
import `fun`.adaptive.auto.internal.frontend.AdatClassFrontend
import `fun`.adaptive.auto.internal.frontend.AdatClassListFrontend
import `fun`.adaptive.auto.internal.frontend.FileFrontend
import `fun`.adaptive.auto.internal.frontend.FolderFrontend
import `fun`.adaptive.auto.internal.origin.AutoInstance
import `fun`.adaptive.auto.internal.origin.OriginListBase
import `fun`.adaptive.auto.model.AutoConnectionInfo

typealias ItemBase<A> = AutoInstance<PropertyBackend<A>, AdatClassFrontend<A>, A, A>
typealias ListBase<A> = OriginListBase<SetBackend<A>, AdatClassListFrontend<A>, A>
typealias FileBase<A> = AutoInstance<PropertyBackend<A>, FileFrontend<A>, A, A>
typealias FolderBase<A> = OriginListBase<SetBackend<A>, FolderFrontend<A>, A>

typealias InfoFunOrNull<T> = (() -> AutoConnectionInfo<T>)?
typealias AutoGeneric = AutoInstance<*, *, *, *>