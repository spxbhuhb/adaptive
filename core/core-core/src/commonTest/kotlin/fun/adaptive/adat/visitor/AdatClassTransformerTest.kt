package `fun`.adaptive.adat.visitor

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.TestClass
import `fun`.adaptive.adat.TestClass2
import `fun`.adaptive.adat.metadata.AdatPropertyMetadata
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class AdatClassTransformerTest {

    @Test
    fun basicTest() {
        val t1 = TestClass(12, true, emptySet())

        val transformer = object : AdatClassTransformerVoid() {

            override fun visitProperty(instance: AdatClass, value: Any?, metadata: AdatPropertyMetadata, data: Nothing?): Any? {
                if (metadata.name == "someInt") {
                    return 23
                } else {
                    return super.visitProperty(instance, value, metadata, data)
                }
            }

        }

        val t2 = t1.transformChildren(transformer, null)

        assertIs<TestClass>(t2)
        assertEquals(23, t2.someInt)
        assertEquals(true, t2.someBoolean)
    }


    @Test
    fun testClass2InstanceTest() {
        val inner = TestClass(42, false, emptySet())
        val t1 = TestClass2(inner)

        val transformer = object : AdatClassTransformerVoid() {
            override fun visitProperty(instance: AdatClass, value: Any?, metadata: AdatPropertyMetadata, data: Nothing?): Any? {
                if (value is TestClass && metadata.name == "instance") {
                    return TestClass(99, false, emptySet())
                }
                return super.visitProperty(instance, value, metadata, data)
            }
        }

        val t2 = t1.transformChildren(transformer, null)
        assertIs<TestClass2>(t2)
        assertEquals(99, t2.instance.someInt)
    }

    @Test
    fun testClass2ListTest() {
        val inner = TestClass(42, false, emptySet())
        val t1 = TestClass2(inner, list = listOf(TestClass2(TestClass(1, true, emptySet()))))

        val transformer = object : AdatClassTransformerVoid() {
            override fun visitProperty(instance: AdatClass, value: Any?, metadata: AdatPropertyMetadata, data: Nothing?): Any? {
                if (metadata.name == "list") {
                    return emptyList<TestClass2>()
                }
                return super.visitProperty(instance, value, metadata, data)
            }
        }

        val t2 = t1.transformChildren(transformer, null)
        assertIs<TestClass2>(t2)
        assertEquals(0, t2.list.size)
    }

    @Test
    fun testClass2NestedTest() {
        val inner = TestClass(42, false, emptySet())
        val nested = TestClass2(inner)
        val t1 = TestClass2(inner, instanceOrNull = nested)

        val transformer = object : AdatClassTransformerVoid() {
            override fun visitProperty(instance: AdatClass, value: Any?, metadata: AdatPropertyMetadata, data: Nothing?): Any? {
                if (value is TestClass2 && metadata.name == "instanceOrNull") {
                    return null
                }
                return super.visitProperty(instance, value, metadata, data)
            }
        }

        val t2 = t1.transformChildren(transformer, null)
        assertIs<TestClass2>(t2)
        assertEquals(null, t2.instanceOrNull)
    }

    @Test
    fun testClass2MapTest() {
        val inner = TestClass(42, false, emptySet())
        val map = mapOf("test" to TestClass2(inner))
        val t1 = TestClass2(inner, map = map)

        val transformer = object : AdatClassTransformerVoid() {
            override fun visitProperty(instance: AdatClass, value: Any?, metadata: AdatPropertyMetadata, data: Nothing?): Any? {
                if (metadata.name == "map") {
                    return emptyMap<String, TestClass2>()
                }
                return super.visitProperty(instance, value, metadata, data)
            }
        }

        val t2 = t1.transformChildren(transformer, null)
        assertIs<TestClass2>(t2)
        assertEquals(0, t2.map.size)
    }

    @Test
    fun testClass2SetTest() {
        val inner = TestClass(42, false, emptySet())
        val set = setOf(TestClass2(inner))
        val t1 = TestClass2(inner, set = set)

        val transformer = object : AdatClassTransformerVoid() {
            override fun visitProperty(instance: AdatClass, value: Any?, metadata: AdatPropertyMetadata, data: Nothing?): Any? {
                if (metadata.name == "set") {
                    return emptySet<TestClass2>()
                }
                return super.visitProperty(instance, value, metadata, data)
            }
        }

        val t2 = t1.transformChildren(transformer, null)
        assertIs<TestClass2>(t2)
        assertEquals(0, t2.set.size)
    }


}