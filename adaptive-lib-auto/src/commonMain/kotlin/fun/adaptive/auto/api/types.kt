package `fun`.adaptive.auto.api

import `fun`.adaptive.auto.internal.backend.PropertyBackend
import `fun`.adaptive.auto.internal.backend.SetBackend
import `fun`.adaptive.auto.internal.frontend.AdatClassFrontend
import `fun`.adaptive.auto.internal.frontend.AdatClassListFrontend
import `fun`.adaptive.auto.internal.frontend.FileFrontend
import `fun`.adaptive.auto.internal.frontend.FolderFrontend
import `fun`.adaptive.auto.internal.origin.OriginBase

typealias InstanceBase<A> = OriginBase<PropertyBackend<A>, AdatClassFrontend<A>, A, A>
typealias ListBase<A> = OriginBase<SetBackend<A>, AdatClassListFrontend<A>, List<A>, A>
typealias FileBase<A> = OriginBase<PropertyBackend<A>, FileFrontend<A>, A, A>
typealias FolderBase<A> = OriginBase<SetBackend<A>, FolderFrontend<A>, List<A>, A>
