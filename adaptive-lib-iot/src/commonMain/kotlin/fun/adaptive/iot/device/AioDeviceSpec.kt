package `fun`.adaptive.iot.device

import `fun`.adaptive.adat.AdatClass

abstract class AioDeviceSpec : AdatClass {

    abstract val notes: String
    abstract val manufacturer: String?
    abstract val model: String?
    abstract val serialNumber: String?
    abstract val firmwareVersion: String?
    abstract val hardwareVersion: String?
    abstract val displayAddress: String?

    open val virtual: Boolean
        get() = false

}