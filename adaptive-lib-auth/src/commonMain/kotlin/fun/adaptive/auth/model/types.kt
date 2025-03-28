package `fun`.adaptive.auth.model

import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.item.AvItem

typealias AuthPrincipal = AvItem<PrincipalSpec>
typealias AuthRole = AvItem<RoleSpec>

typealias AuthPrincipalId = AvValueId
typealias AuthRoleId = AvValueId

const val AUTH_PRINCIPAL = "auth:principal"
const val AUTH_ROLE = "auth:role"
const val AUTH_CREDENTIAL_LIST = "auth:credential-list"