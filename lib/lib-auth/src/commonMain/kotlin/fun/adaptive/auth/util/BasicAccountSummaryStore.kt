package `fun`.adaptive.auth.util

import `fun`.adaptive.auth.model.AuthMarkers
import `fun`.adaptive.auth.model.AuthRefLabels
import `fun`.adaptive.auth.model.PrincipalSpec
import `fun`.adaptive.auth.model.basic.BasicAccountSpec
import `fun`.adaptive.auth.model.basic.BasicAccountSummary
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.unsupported
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValue.Companion.checkSpec
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.avByMarker
import `fun`.adaptive.value.client.AvValueSubscriber

class BasicAccountSummaryStore(
    backend: BackendAdapter
) : AvValueSubscriber<List<BasicAccountSummary>>(
    { s, id -> markers.also { s.subscribe(it, id) } },
    backend,
) {

    companion object {
        val markers = listOf(
            avByMarker(AuthMarkers.BASIC_ACCOUNT),
            avByMarker(AuthMarkers.PRINCIPAL)
        )
    }

    private class Entry(
        val principal: AvValue<PrincipalSpec>?,
        val account: AvValue<BasicAccountSpec>?
    )

    private val entryMap = mutableMapOf<AvValueId, Entry>()
    private val summaryMap = mutableMapOf<AvValueId, BasicAccountSummary>()

    override fun process(value: AvValue<*>) {
        val principalId: AvValueId
        val original: Entry?
        val new: Entry

        when (value.spec) {
            is PrincipalSpec -> {
                principalId = value.uuid
                original = entryMap[principalId]
                new = Entry(value.checkSpec<PrincipalSpec>(), original?.account)
            }

            is BasicAccountSpec -> {
                principalId = value.refIdOrNull(AuthRefLabels.PRINCIPAL_REF) ?: return
                original = entryMap[principalId]
                new = Entry(original?.principal, value.checkSpec<BasicAccountSpec>())
            }

            else -> return
        }

        entryMap[principalId] = new

        if (new.principal == null || new.account == null) return

        summaryMap[principalId] = BasicAccountSummary(new.principal, new.account)

        notifyListeners()
    }

    override var value: List<BasicAccountSummary>
        get() = summaryMap.values.toList()
        set(_) = unsupported()

}