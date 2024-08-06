package hu.simplexion.adaptive.auto.model

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.auto.LamportTimestamp

/**
 * @property  originHandle       Handle of an existing backend to connect with, the origin backend.
 * @property  originTime         Current time of the origin backend.
 * @property  connectingHandle   A new handle created for the backend that wants to connect to the origin backend.
 */
@Adat
class AutoConnectInfo(
    val originHandle: AutoHandle,
    val originTime: LamportTimestamp,
    val connectingHandle: AutoHandle
)