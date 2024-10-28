package `fun`.adaptive.auto.api

import `fun`.adaptive.auto.internal.backend.PropertyBackend
import `fun`.adaptive.auto.internal.backend.SetBackend
import `fun`.adaptive.auto.internal.frontend.AdatClassFrontend
import `fun`.adaptive.auto.internal.frontend.AdatClassListFrontend
import `fun`.adaptive.auto.internal.frontend.FileFrontend
import `fun`.adaptive.auto.internal.frontend.FolderFrontend
import `fun`.adaptive.auto.internal.origin.OriginBase
import `fun`.adaptive.auto.internal.origin.OriginListBase

typealias ItemBase<A> = OriginBase<PropertyBackend<A>, AdatClassFrontend<A>, A, A>
typealias ListBase<A> = OriginListBase<SetBackend<A>, AdatClassListFrontend<A>, A>
typealias FileBase<A> = OriginBase<PropertyBackend<A>, FileFrontend<A>, A, A>
typealias FolderBase<A> = OriginListBase<SetBackend<A>, FolderFrontend<A>, A>
