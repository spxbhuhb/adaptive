package `fun`.adaptive.auto.testing

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.api.autoInstance
import `fun`.adaptive.auto.api.autoList
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.backend.TestData
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.backend.builtin.worker

class AutoTestWorker(
    trace: Boolean = false,
    instanceInit: TestData = TestData(12, "a"),
    listInit: List<TestData> = listOf(TestData(12, "a"))
) : WorkerImpl<AutoTestWorker> {

    val worker by worker<AutoWorker>()

    val instance by lazy { autoInstance(worker, TestData, instanceInit, trace = trace) }
    val list by lazy { autoList(worker, TestData, initialValues = listInit, trace = trace) }
    val polyList by lazy { autoList<AdatClass>(worker, trace = trace) }

    override suspend fun run() {

    }

    fun instance(): AutoConnectionInfo<TestData> {
        return instance.connectInfo()
    }

    fun list(): AutoConnectionInfo<List<TestData>> {
        return list.connectInfo()
    }

    fun item(i: Int): AutoConnectionInfo<TestData>? {
        return list.connectInfo<TestData> { it.i == i }
    }

    fun polyList(): AutoConnectionInfo<List<AdatClass>> {
        return polyList.connectInfo()
    }

}