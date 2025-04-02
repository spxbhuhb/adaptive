package `fun`.adaptive.auth.util

import `fun`.adaptive.auth.api.basic.AuthBasicApi
import `fun`.adaptive.auth.model.PrincipalSpec
import `fun`.adaptive.auth.model.basic.BasicAccountSpec
import `fun`.adaptive.auth.model.basic.BasicAccountSummary
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.foundation.unsupported
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.value.*
import `fun`.adaptive.value.item.AvItem
import `fun`.adaptive.value.item.AvItem.Companion.withSpec
import `fun`.adaptive.value.local.AvLocalStore

class BasicAccountSummaryStore(
    backend: BackendAdapter
) : AvLocalStore<List<BasicAccountSummary>>() {

    override val scope = backend.scope

    override val localWorker = backend.firstImpl<AvValueWorker>()

    val service = getService<AuthBasicApi>(backend.transport)

    override suspend fun subscribe(id: AvValueSubscriptionId): List<AvSubscribeCondition> =
        service.subscribe(id)

    override suspend fun unsubscribe(id: AvValueSubscriptionId) =
        service.unsubscribe(id)

    private class Entry(
        val principal: AvItem<PrincipalSpec>?,
        val account: AvItem<BasicAccountSpec>?
    )

    private val entryMap = mutableMapOf<AvValueId, Entry>()
    private val summaryMap = mutableMapOf<AvValueId, BasicAccountSummary>()

    override fun process(value: AvValue) {
        if (value !is AvItem<*>) return

        val principalId: AvValueId
        val original: Entry?
        val new: Entry

        when (value.spec) {
            is PrincipalSpec -> {
                principalId = value.uuid
                original = entryMap[principalId]
                new = Entry(value.withSpec<PrincipalSpec>(), original?.account)
            }

            is BasicAccountSpec -> {
                principalId = value.parentId ?: return
                original = entryMap[principalId]
                new = Entry(original?.principal, value.withSpec<BasicAccountSpec>())
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