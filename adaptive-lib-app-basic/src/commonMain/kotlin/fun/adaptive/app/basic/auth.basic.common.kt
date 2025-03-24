package `fun`.adaptive.app.basic

import `fun`.adaptive.app.basic.auth.model.BasicAccount
import `fun`.adaptive.app.basic.auth.model.BasicAccountSummary
import `fun`.adaptive.app.basic.auth.model.BasicSignIn
import `fun`.adaptive.app.basic.auth.model.BasicSignUp
import `fun`.adaptive.auth.authCommon
import `fun`.adaptive.app.basic.ui.BasicAppData
import `fun`.adaptive.app.basic.ui.DefaultLayoutState
import `fun`.adaptive.app.basic.ui.SidebarUserMode
import `fun`.adaptive.wireformat.WireFormatRegistry
import `fun`.adaptive.wireformat.builtin.EnumWireFormat


lateinit var appData : BasicAppData

fun authBasicCommon() {

    authCommon()

    val r = WireFormatRegistry

    r += BasicAccount
    r += BasicAccountSummary
    r += BasicSignIn
    r += BasicSignUp


    WireFormatRegistry += DefaultLayoutState
    WireFormatRegistry += SidebarUserMode

}