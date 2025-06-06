package `fun`.adaptive.adat

import `fun`.adaptive.adat.api.properties
import `fun`.adaptive.adat.api.validate
import `fun`.adaptive.utility.UUID
import kotlin.test.Test

class ValidateTest {
    @Test
    fun test() {
        HostConfiguration(null, null, null, "", "", "").validate()
    }
}

@Adat
private class HostConfiguration (
    val uuid : UUID<*>?,
    val hostName : String?,
    val friendlyId : String?,
    val projectUrl : String,
    val projectUser : String,
    val projectPassword : Secret
){
    override fun descriptor() {
        properties {
            hostName blank false minLength 1
            friendlyId blank false minLength 1
            projectUrl blank false
            projectUser blank false minLength 1
            projectPassword blank false secret true
        }
    }
}