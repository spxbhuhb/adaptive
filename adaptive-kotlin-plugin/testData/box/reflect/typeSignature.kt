package somePackage.someSubPackage

import `fun`.adaptive.reflect.*
import `fun`.adaptive.foundation.instruction.*

fun box(): String {

    if (1.typeSignature() != "I") return "Fail: 1.typeSignature() != \"I\""
    if ("".typeSignature() != "T") return "Fail: a.typeSignature() != \"T\""
    if (listOf(1).typeSignature() != "Lkotlin.collections.List<I>;") return "Fail: listOf(1).typeSignature() != \"Lkotlin.collections.List<I>;\""
    if (typeSignature<AdaptiveInstructionGroup>() != "Lfun.adaptive.foundation.instruction.AdaptiveInstructionGroup;") return "Fail: instruction group"

    return "OK"
}