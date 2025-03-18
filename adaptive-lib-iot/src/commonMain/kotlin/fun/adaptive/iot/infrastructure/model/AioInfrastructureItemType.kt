package `fun`.adaptive.iot.infrastructure.model

import `fun`.adaptive.adaptive_lib_iot.generated.resources.device
import `fun`.adaptive.adaptive_lib_iot.generated.resources.host
import `fun`.adaptive.adaptive_lib_iot.generated.resources.network
import `fun`.adaptive.adaptive_lib_iot.generated.resources.point
import `fun`.adaptive.iot.infrastructure.model.AioInfrastructureItemType.entries
import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.utility.trimSignature
import `fun`.adaptive.wireformat.builtin.EnumWireFormat

enum class AioInfrastructureItemType {
    Host,
    Network,
    Device,
    Point;

    fun localized(): String =
        when (this) {
            Host -> Strings.host
            Network -> Strings.network
            Device -> Strings.device
            Point -> Strings.point
        }

    companion object : EnumWireFormat<AioInfrastructureItemType>(entries) {
        override val wireFormatName: String
            get() = AioInfrastructureItemType.typeSignature().trimSignature()
    }
}