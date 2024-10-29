package `fun`.adaptive.auto.internal.frontend

import `fun`.adaptive.adat.AdatClass

abstract class AutoItemFrontend<IT : AdatClass> : AutoFrontend<IT>() {

    abstract val value: IT

}