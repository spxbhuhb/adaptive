package `fun`.adaptive.app

import `fun`.adaptive.auth.model.Session

open class UiClientApplicationData {

    val session: Session
        get() = checkNotNull(sessionOrNull) { "Session is not initialized" }

    var sessionOrNull: Session? = null

}