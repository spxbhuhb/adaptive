package `fun`.adaptive.app.system.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.Secret
import `fun`.adaptive.adat.api.properties

@Adat
class SystemSettingsSpec(
    val upstreamUrl: String? = null,
    val upstreamAccount : String? = null,
    val upstreamPassword : Secret? = null
){
    override fun descriptor() {
        properties {
            upstreamPassword secret true
        }
    }
}