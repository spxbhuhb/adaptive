package `fun`.adaptive.runtime

import `fun`.adaptive.adat.Adat
import kotlin.time.Instant

@Adat
class AppAboutData(
    val name : String? = null,
    val version : String? = null,
    val buildTime : Instant? = null,
    val buildCommit : String? = null,
    val website : String? = null,
    val license : String? = null,
    val licenseUrl : String? = null,
    val copyright : String? = null,
    val copyrightUrl : String? = null,
    val authors : List<String>? = null,
    val contact : String? = null,
    val openSource : String? = null
)