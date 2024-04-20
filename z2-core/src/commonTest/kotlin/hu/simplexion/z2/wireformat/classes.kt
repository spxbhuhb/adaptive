package hu.simplexion.z2.wireformat


data class A(
    var b: Boolean = false,
    var i: Int = 0,
    var s: String = "",
    var l: MutableList<Int> = mutableListOf()
) {
    companion object : WireFormat<A> {

        override fun encodeInstance(builder: MessageBuilder, value: A) =
            builder
                .startInstance()
                .boolean(1, "b", value.b)
                .int(2, "i", value.i)
                .string(3, "s", value.s)
                .intList(4, "l", value.l)
                .endInstance()

        override fun decodeInstance(message: Message?): A {
            if (message == null) return A()

            return A(
                message.boolean(1, "b"),
                message.int(2, "i"),
                message.string(3, "s"),
                message.intList(4, "l").toMutableList()
            )
        }
    }
}

data class B(
    var a: A = A(),
    var s: String = ""
) {
    companion object : WireFormat<B> {

        override fun encodeInstance(builder: MessageBuilder, value: B) =
            builder
                .startInstance()
                .instance(1, "a", A, value.a)
                .string(2, "s", value.s)
                .endInstance()

        override fun decodeInstance(message: Message?): B {
            if (message == null) return B()

            return B(
                message.instance(1, "a", A),
                message.string(2, "s")
            )
        }
    }
}

enum class E {
    V1,
    V2
}