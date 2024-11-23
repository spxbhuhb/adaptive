package `fun`.adaptive.auto.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatCompanion
import kotlin.math.acos

/**
 * @property  acceptingHandle    Handle of an existing backend that accepts the connection.
 * @property  acceptingTime      Current time of the origin backend.
 * @property  connectingHandle   A new handle created for the backend that wants to connect to the accepting backend.
 */
@Adat
class AutoConnectionInfo<T>(
    val connectionType: AutoConnectionType,
    val acceptingHandle: AutoHandle,
    val acceptingTime: LamportTimestamp,
    val connectingHandle: AutoHandle,
) {
    companion object : AdatCompanion<AutoConnectionInfo<*>> {

        fun <VT> origin(collection: Boolean) =
            AutoHandle.origin(collection).let { handle ->
                AutoConnectionInfo<VT>(
                    AutoConnectionType.Origin,
                    acceptingHandle = handle,
                    acceptingTime = LamportTimestamp.ORIGIN,
                    connectingHandle = handle,
                )
            }

    }
}