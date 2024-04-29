package hu.simplexion.adaptive.services.runtime.test.box

import hu.simplexion.adaptive.serialization.protobuf.*
import hu.simplexion.adaptive.util.UUID
import hu.simplexion.adaptive.services.*
import hu.simplexion.adaptive.services.transport.ServiceCallTransport
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

fun box(): String {
    val booleanVal = true
    val booleanListVal = listOf(true, false, true)

    val intVal = 123
    val intListVal = listOf(1, 2, 3)

    val longVal = 1234L
    val longListVal = listOf(1L, 2L, 3L, 4L)

    val stringVal = "abc"
    val stringListVal = listOf("a", "b", "c")

    val byteArrayVal = byteArrayOf(9, 8, 7)
    val byteArrayListVal = listOf(byteArrayOf(1), byteArrayOf(2), byteArrayOf(3))

    val uuidVal = UUID<TypesService>()
    val uuidListVal = listOf(UUID<TypesService>(), UUID(), UUID())

    val instanceVal = A(true, 12, "hello")
    val instanceListVal = listOf(
        B(A(true, 123, "a", mutableListOf(1, 2, 3)), "aa"),
        B(A(false, 456, "b", mutableListOf(4, 5, 6)), "bb"),
        B(A(true, 789, "c", mutableListOf(7, 8, 9)), "cc")
    )

    val enumVal = E.V1
    val enumListVal = listOf(E.V2, E.V1)

    val durationVal = 10.seconds
    val durationListVal = listOf(durationVal)

    val instantVal = Clock.System.now()
    val instantListVal = listOf(instantVal)

    val localDateVal = LocalDate(2023, 7, 27)
    val localDateListVal = listOf(localDateVal)

    val localDateTimeVal = LocalDateTime(2023, 7, 27, 15, 35, 5, 11)
    val localDateTimeListVal = listOf(localDateTimeVal)

    val errors = mutableListOf<String>()

    runBlocking {
        defaultServiceCallTransport = DumpTransport()
        defaultServiceImplFactory += TypesServiceImpl()

        // -- Boolean --
        if (typesServiceConsumer.testFun(booleanVal) != booleanVal) errors += "booleanValue"
        if (typesServiceConsumer.testFunBooleanNull(null) != null) errors += "booleanValue-null"
        if (typesServiceConsumer.testFunBooleanNull(booleanVal) != booleanVal) errors += "booleanValue-non-null"

        if (typesServiceConsumer.testBooleanList(booleanListVal) != booleanListVal) errors += "booleanListVal"
        if (typesServiceConsumer.testBooleanListNull(null) != null) errors += "booleanListValue-null"
        if (typesServiceConsumer.testBooleanListNull(booleanListVal) != booleanListVal) errors += "booleanListValue-non-null"

        // -- Int --

        if (typesServiceConsumer.testFun(intVal) != intVal) errors += "intVal"
        if (typesServiceConsumer.testFunIntNull(null) != null) errors += "intValue-null"
        if (typesServiceConsumer.testFunIntNull(intVal) != intVal) errors += "intValue-non-null"

        if (typesServiceConsumer.testIntList(intListVal) != intListVal) errors += "intListVal"
        if (typesServiceConsumer.testIntListNull(null) != null) errors += "intListValue-null"
        if (typesServiceConsumer.testIntListNull(intListVal) != intListVal) errors += "intListValue-non-null"

        // -- Long --

        if (typesServiceConsumer.testFun(longVal) != longVal) errors += "longVal"
        if (typesServiceConsumer.testFunLongNull(null) != null) errors += "longValue-null"
        if (typesServiceConsumer.testFunLongNull(longVal) != longVal) errors += "longValue-non-null"

        if (typesServiceConsumer.testLongList(longListVal) != longListVal) errors += "longListVal"
        if (typesServiceConsumer.testLongListNull(null) != null) errors += "longListValue-null"
        if (typesServiceConsumer.testLongListNull(longListVal) != longListVal) errors += "longListValue-non-null"

        // -- String --

        if (typesServiceConsumer.testFun(stringVal) != stringVal) errors += "stringVal"
        if (typesServiceConsumer.testFunStringNull(null) != null) errors += "stringValue-null"
        if (typesServiceConsumer.testFunStringNull(stringVal) != stringVal) errors += "stringValue-non-null"

        if (typesServiceConsumer.testStringList(stringListVal) != stringListVal) errors += "stringListVal"
        if (typesServiceConsumer.testStringListNull(null) != null) errors += "stringListValue-null"
        if (typesServiceConsumer.testStringListNull(stringListVal) != stringListVal) errors += "stringListValue-non-null"

        // -- ByteArray --

        if (! typesServiceConsumer.testFun(byteArrayVal).contentEquals(byteArrayVal)) errors += "byteArrayVal"
        if (typesServiceConsumer.testFunByteArrayNull(null) != null) errors += "byteArrayValue-null"
        if (! typesServiceConsumer.testFunByteArrayNull(byteArrayVal).contentEquals(byteArrayVal)) errors += "byteArrayValue-non-null"

        typesServiceConsumer.testByteArrayList(byteArrayListVal).forEachIndexed { index, bytes ->
            if (! bytes.contentEquals(byteArrayListVal[index])) errors += "byteArrayListVal"
        }
        if (typesServiceConsumer.testByteArrayListNull(null) != null) errors += "byteArrayListValue-null"
        typesServiceConsumer.testByteArrayListNull(byteArrayListVal)?.forEachIndexed { index, bytes ->
            if (! bytes.contentEquals(byteArrayListVal[index])) errors += "byteArrayListVal-non-null"
        } ?: { errors += "byteArrayListVal-non-null" }

        // -- Uuid --

        if (typesServiceConsumer.testFun(uuidVal) != uuidVal) errors += "uuidVal"
        if (typesServiceConsumer.testFunUuidNull(null) != null) errors += "uuidValue-null"
        if (typesServiceConsumer.testFunUuidNull(uuidVal) != uuidVal) errors += "uuidValue-non-null"

        if (typesServiceConsumer.testUuidList(uuidListVal) != uuidListVal) errors += "uuidListVal"
        if (typesServiceConsumer.testUuidListNull(null) != null) errors += "uuidListValue-null"
        if (typesServiceConsumer.testUuidListNull(uuidListVal) != uuidListVal) errors += "uuidListValue-non-null"

        // -- Instance --

        if (typesServiceConsumer.testFun(instanceVal) != instanceVal) errors += "instanceVal"
        if (typesServiceConsumer.testFunInstanceNull(null) != null) errors += "instanceValue-null"
        if (typesServiceConsumer.testFunInstanceNull(instanceVal) != instanceVal) errors += "instanceValue-non-null"

        if (typesServiceConsumer.testInstanceList(instanceListVal) != instanceListVal) errors += "instanceListVal"
        if (typesServiceConsumer.testInstanceListNull(null) != null) errors += "instanceListValue-null"
        if (typesServiceConsumer.testInstanceListNull(instanceListVal) != instanceListVal) errors += "instanceListValue-non-null"

        // -- Enum --

//        org.opentest4j.AssertionFailedError: expected: <OK> but was: <Fail: enumValue-null, enumListVal, enumListValue-null, enumListValue-non-null>

        if (typesServiceConsumer.testFun(enumVal) != enumVal) errors += "enumVal"
        if (typesServiceConsumer.testFunEnumNull(null) != null) errors += "enumValue-null"
        if (typesServiceConsumer.testFunEnumNull(enumVal) != enumVal) errors += "enumValue-non-null"

        if (typesServiceConsumer.testEnumList(enumListVal) != enumListVal) errors += "enumListVal"
        if (typesServiceConsumer.testEnumListNull(null) != null) errors += "enumListValue-null"
        if (typesServiceConsumer.testEnumListNull(enumListVal) != enumListVal) errors += "enumListValue-non-null"
        // -- DateTime --

        if (typesServiceConsumer.testFun(durationVal) != durationVal) errors += "durationVal"
        if (typesServiceConsumer.testDurationList(durationListVal) != durationListVal) errors += "durationListVal"

        if (typesServiceConsumer.testFun(instantVal) != instantVal) errors += "instantVal"
        if (typesServiceConsumer.testInstantList(instantListVal) != instantListVal) errors += "instantListVal"

        if (typesServiceConsumer.testFun(localDateVal) != localDateVal) errors += "localDateVal"
        if (typesServiceConsumer.testLocalDateList(localDateListVal) != localDateListVal) errors += "localDateListVal"

        if (typesServiceConsumer.testFun(localDateTimeVal) != localDateTimeVal) errors += "localDateTimeVal"
        if (typesServiceConsumer.testLocalDateTimeList(localDateTimeListVal) != localDateTimeListVal) errors += "localDateTimeListVal"
    }

    return if (errors.isEmpty()) "OK" else "Fail: ${errors.joinToString(", ")}"
}

interface TypesService : Service {

    suspend fun testFun()

    suspend fun testFun(arg1: Boolean): Boolean
    suspend fun testFunBooleanNull(arg1: Boolean?): Boolean?
    suspend fun testBooleanList(arg1: List<Boolean>): List<Boolean>
    suspend fun testBooleanListNull(arg1: List<Boolean>?): List<Boolean>?

    suspend fun testFun(arg1: Int): Int
    suspend fun testFunIntNull(arg1: Int?): Int?
    suspend fun testIntList(arg1: List<Int>): List<Int>
    suspend fun testIntListNull(arg1: List<Int>?): List<Int>?

    suspend fun testFun(arg1: Long): Long
    suspend fun testFunLongNull(arg1: Long?): Long?
    suspend fun testLongList(arg1: List<Long>): List<Long>
    suspend fun testLongListNull(arg1: List<Long>?): List<Long>?

    suspend fun testFun(arg1: String): String
    suspend fun testFunStringNull(arg1: String?): String?
    suspend fun testStringList(arg1: List<String>): List<String>
    suspend fun testStringListNull(arg1: List<String>?): List<String>?

    suspend fun testFun(arg1: ByteArray): ByteArray
    suspend fun testFunByteArrayNull(arg1: ByteArray?): ByteArray?
    suspend fun testByteArrayList(arg1: List<ByteArray>): List<ByteArray>
    suspend fun testByteArrayListNull(arg1: List<ByteArray>?): List<ByteArray>?

    suspend fun testFun(arg1: UUID<TypesService>): UUID<TypesService>
    suspend fun testFunUuidNull(arg1: UUID<TypesService>?): UUID<TypesService>?
    suspend fun testUuidList(arg1: List<UUID<TypesService>>): List<UUID<TypesService>>
    suspend fun testUuidListNull(arg1: List<UUID<TypesService>>?): List<UUID<TypesService>>?

    suspend fun testFun(arg1: A): A
    suspend fun testFunInstanceNull(arg1: A?): A?
    suspend fun testInstanceList(arg1: List<B>): List<B>
    suspend fun testInstanceListNull(arg1: List<B>?): List<B>?

    suspend fun testFun(arg1: E): E
    suspend fun testFunEnumNull(arg1: E?): E?
    suspend fun testEnumList(arg1: List<E>): List<E>
    suspend fun testEnumListNull(arg1: List<E>?): List<E>?

    // these are general instances, no need to test nullable
    suspend fun testFun(arg1: Duration): Duration
    suspend fun testDurationList(arg1: List<Duration>): List<Duration>

    suspend fun testFun(arg1: Instant): Instant
    suspend fun testInstantList(arg1: List<Instant>): List<Instant>

    suspend fun testFun(arg1: LocalDate): LocalDate
    suspend fun testLocalDateList(arg1: List<LocalDate>): List<LocalDate>

    suspend fun testFun(arg1: LocalDateTime): LocalDateTime
    suspend fun testLocalDateTimeList(arg1: List<LocalDateTime>): List<LocalDateTime>

}

val typesServiceConsumer = getService<TypesService>()

class TypesServiceImpl : TypesService, ServiceImpl<TypesServiceImpl> {

    override suspend fun testFun() = Unit

    override suspend fun testFun(arg1: Boolean) = arg1
    override suspend fun testFunBooleanNull(arg1: Boolean?) = arg1
    override suspend fun testBooleanList(arg1: List<Boolean>) = arg1
    override suspend fun testBooleanListNull(arg1: List<Boolean>?) = arg1

    override suspend fun testFun(arg1: Int) = arg1
    override suspend fun testFunIntNull(arg1: Int?) = arg1
    override suspend fun testIntList(arg1: List<Int>) = arg1
    override suspend fun testIntListNull(arg1: List<Int>?) = arg1

    override suspend fun testFun(arg1: Long) = arg1
    override suspend fun testFunLongNull(arg1: Long?) = arg1
    override suspend fun testLongList(arg1: List<Long>) = arg1
    override suspend fun testLongListNull(arg1: List<Long>?) = arg1

    override suspend fun testFun(arg1: String) = arg1
    override suspend fun testFunStringNull(arg1: String?) = arg1
    override suspend fun testStringList(arg1: List<String>) = arg1
    override suspend fun testStringListNull(arg1: List<String>?) = arg1

    override suspend fun testFun(arg1: ByteArray) = arg1
    override suspend fun testFunByteArrayNull(arg1: ByteArray?) = arg1
    override suspend fun testByteArrayList(arg1: List<ByteArray>) = arg1
    override suspend fun testByteArrayListNull(arg1: List<ByteArray>?) = arg1

    override suspend fun testFun(arg1: UUID<TypesService>) = arg1
    override suspend fun testFunUuidNull(arg1: UUID<TypesService>?) = arg1
    override suspend fun testUuidList(arg1: List<UUID<TypesService>>) = arg1
    override suspend fun testUuidListNull(arg1: List<UUID<TypesService>>?) = arg1

    override suspend fun testFun(arg1: A) = arg1
    override suspend fun testFunInstanceNull(arg1: A?) = arg1
    override suspend fun testInstanceList(arg1: List<B>) = arg1
    override suspend fun testInstanceListNull(arg1: List<B>?) = arg1

    override suspend fun testFun(arg1: E) = arg1
    override suspend fun testFunEnumNull(arg1: E?) = arg1
    override suspend fun testEnumList(arg1: List<E>) = arg1
    override suspend fun testEnumListNull(arg1: List<E>?) = arg1

    override suspend fun testFun(arg1: Duration) = arg1
    override suspend fun testDurationList(arg1: List<Duration>) = arg1

    override suspend fun testFun(arg1: Instant) = arg1
    override suspend fun testInstantList(arg1: List<Instant>) = arg1

    override suspend fun testFun(arg1: LocalDate) = arg1
    override suspend fun testLocalDateList(arg1: List<LocalDate>) = arg1

    override suspend fun testFun(arg1: LocalDateTime) = arg1
    override suspend fun testLocalDateTimeList(arg1: List<LocalDateTime>) = arg1
}

data class A(
    var b: Boolean = false,
    var i: Int = 0,
    var s: String = "",
    var l: MutableList<Int> = mutableListOf()
) {
    companion object : ProtoEncoder<A>, ProtoDecoder<A> {

        override fun decodeProto(message: ProtoMessage?): A {
            if (message == null) return A()

            println(message.dumpProto())

            return A(
                message.boolean(1),
                message.int(2),
                message.string(3),
                message.intList(4)
            )
        }

        override fun encodeProto(value: A): ByteArray =
            ProtoMessageBuilder()
                .boolean(1, value.b)
                .int(2, value.i)
                .string(3, value.s)
                .intList(4, value.l)
                .pack()
    }
}

data class B(
    var a: A = A(),
    var s: String = ""
) {
    companion object : ProtoEncoder<B>, ProtoDecoder<B> {

        override fun decodeProto(message: ProtoMessage?): B {
            if (message == null) return B()

            return B(
                message.instance(1, A),
                message.string(2)
            )
        }

        override fun encodeProto(value: B): ByteArray =
            ProtoMessageBuilder()
                .instance(1, A, value.a)
                .string(2, value.s)
                .pack()
    }
}

enum class E {
    V1,
    V2
}

class DumpTransport : ServiceCallTransport {
    override suspend fun <T> call(serviceName: String, funName: String, payload: ByteArray, decoder: ProtoDecoder<T>): T {
        println("==== REQUEST ====")
        println(serviceName)
        println(funName)
        println(payload.dumpProto())

        val service = requireNotNull(defaultServiceImplFactory[serviceName, BasicServiceContext()])

        val responseBuilder = ProtoMessageBuilder()

        service.dispatch(funName, ProtoMessage(payload), responseBuilder)

        val responsePayload = responseBuilder.pack()
        println("==== RESPONSE ====")
        println(responsePayload.dumpProto())
        println(decoder::class.qualifiedName)

        return decoder.decodeProto(ProtoMessage(responseBuilder.pack()))
    }
}