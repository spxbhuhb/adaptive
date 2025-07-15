package `fun`.adaptive.sandbox.support

import `fun`.adaptive.value.domain.AvValueDomainDef
import `fun`.adaptive.value.model.AvTreeDef

val exampleDomain
    inline get() = ExampleDomain

object ExampleDomain : AvValueDomainDef() {
    val treeDef = AvTreeDef("example-node", "example-children", "exampleParentRef", "exampleChildListRef", null)
}