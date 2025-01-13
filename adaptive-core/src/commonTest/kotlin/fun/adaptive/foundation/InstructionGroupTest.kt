package `fun`.adaptive.foundation

import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import kotlin.test.*


class InstructionGroupTest {

    private data class TestInstruction(val id: String) : AdaptiveInstruction {
        override fun applyTo(subject: Any) {
            // No-op
        }

        override fun matchOrNull(predicate: (AdaptiveInstruction) -> Boolean): AdaptiveInstruction? {
            return if (predicate(this)) this else null
        }
    }

    @Test
    fun `find should return the first matching instruction in the group`() {
        // Arrange
        val instruction1 = TestInstruction("instr1")
        val instruction2 = TestInstruction("instr2")
        val group = AdaptiveInstructionGroup(listOf(instruction1, instruction2))

        // Act
        val found = group.find { it is TestInstruction && it.id == "instr2" }

        // Assert
        assertEquals(instruction2, found, "Expected to find instruction2 first")
    }

    @Test
    fun `find should return null if no matching instruction is found`() {
        // Arrange
        val instruction1 = TestInstruction("instr1")
        val group = AdaptiveInstructionGroup(listOf(instruction1))

        // Act
        val found = group.find { it is TestInstruction && it.id == "instrX" }

        // Assert
        assertNull(found, "Expected no instruction to be found")
    }

    @Test
    fun `find should return a matching instruction from a nested group`() {
        // Arrange
        val nestedInstruction = TestInstruction("nested")
        val childGroup = AdaptiveInstructionGroup(listOf(nestedInstruction))
        val parentGroup = AdaptiveInstructionGroup(listOf(childGroup))

        // Act
        val found = parentGroup.find { it is TestInstruction && it.id == "nested" }

        // Assert
        assertEquals(nestedInstruction, found, "Expected to find 'nested' instruction in parent group")
    }

    @Test
    fun `contains should return true for an instruction present in the group`() {
        // Arrange
        val instruction1 = TestInstruction("instr1")
        val instruction2 = TestInstruction("instr2")
        val group = AdaptiveInstructionGroup(listOf(instruction1, instruction2))

        // Act & Assert
        assertTrue(instruction1 in group, "Expected group to contain instruction1")
        assertTrue(instruction2 in group, "Expected group to contain instruction2")
    }

    @Test
    fun `contains should return false for an instruction not in the group`() {
        // Arrange
        val instruction1 = TestInstruction("instr1")
        val instruction2 = TestInstruction("instr2")  // not in group
        val group = AdaptiveInstructionGroup(listOf(instruction1))

        // Act & Assert
        assertFalse(instruction2 in group, "Expected group not to contain instruction2")
    }

    @Test
    fun `contains should return true if instruction is nested in a subgroup`() {
        // Arrange
        val nestedInstruction = TestInstruction("nested")
        val childGroup = AdaptiveInstructionGroup(listOf(nestedInstruction))
        val parentGroup = AdaptiveInstructionGroup(listOf(childGroup))

        // Act & Assert
        assertTrue(nestedInstruction in parentGroup, "Expected parentGroup to contain nested instruction")
    }

    /**
     * A simple [AdaptiveInstruction] for testing, representing "Foo" type.
     */
    private data class FooInstruction(val id: String) : AdaptiveInstruction {
        override fun applyTo(subject: Any) {
            // No-op
        }

        override fun matchOrNull(predicate: (AdaptiveInstruction) -> Boolean): AdaptiveInstruction? {
            return if (predicate(this)) this else null
        }
    }

    /**
     * Another simple [AdaptiveInstruction] for testing, representing "Bar" type.
     */
    private data class BarInstruction(val id: String) : AdaptiveInstruction {
        override fun applyTo(subject: Any) {
            // No-op
        }

        override fun matchOrNull(predicate: (AdaptiveInstruction) -> Boolean): AdaptiveInstruction? {
            return if (predicate(this)) this else null
        }
    }

    @Test
    fun `firstOrNullIfInstance should return the first instance of T when present`() {
        // Arrange
        val fooInstruction = FooInstruction("foo1")
        val barInstruction = BarInstruction("bar1")
        val group = AdaptiveInstructionGroup(listOf(fooInstruction, barInstruction))

        // Act
        val foundFoo = group.firstOrNullIfInstance<FooInstruction>()

        // Assert
        assertEquals(fooInstruction, foundFoo, "Expected first FooInstruction to be found")
    }

    @Test
    fun `firstOrNullIfInstance should return null if type T is not present`() {
        // Arrange
        val barInstruction = BarInstruction("bar1")
        val group = AdaptiveInstructionGroup(listOf(barInstruction))

        // Act
        val foundFoo = group.firstOrNullIfInstance<FooInstruction>()

        // Assert
        assertNull(foundFoo, "Expected null because no FooInstruction is present")
    }

    @Test
    fun `firstOrNullIfInstance should return an instance from a nested group`() {
        // Arrange
        val fooInstruction = FooInstruction("nestedFoo")
        val nestedGroup = AdaptiveInstructionGroup(listOf(fooInstruction))
        val parentGroup = AdaptiveInstructionGroup(listOf(nestedGroup))

        // Act
        val foundFoo = parentGroup.firstOrNullIfInstance<FooInstruction>()

        // Assert
        assertEquals(fooInstruction, foundFoo, "Expected to find FooInstruction in nested group")
    }


    @Test
    fun `filter returns all instructions matching the predicate`() {
        // Arrange
        val instruction1 = TestInstruction("matchMe")
        val instruction2 = TestInstruction("notMatched")
        val instruction3 = TestInstruction("matchMeToo")
        val group = AdaptiveInstructionGroup(listOf(instruction1, instruction2, instruction3))

        // Act
        val matched = group.filter { it is TestInstruction && it.id.startsWith("match") }

        // Assert
        assertEquals(
            listOf(instruction1, instruction3), matched,
            "Expected only instructions that match the predicate to be returned"
        )
    }

    @Test
    fun `filter returns an empty list if no instructions match the predicate`() {
        // Arrange
        val instruction = TestInstruction("noMatch")
        val group = AdaptiveInstructionGroup(listOf(instruction))

        // Act
        val matched = group.filter { it is TestInstruction && it.id.startsWith("xyz") }

        // Assert
        assertTrue(matched.isEmpty(), "Expected an empty list if no instruction matches the predicate")
    }

    @Test
    fun `filter includes instructions in nested groups`() {
        // Arrange
        val childInstruction = TestInstruction("childMatch")
        val childGroup = AdaptiveInstructionGroup(listOf(childInstruction))
        val parentGroup = AdaptiveInstructionGroup(listOf(childGroup))

        // Act
        val matched = parentGroup.filter { it is TestInstruction && it.id == "childMatch" }

        // Assert
        assertEquals(
            listOf(childInstruction), matched,
            "Expected to find the matching instruction in the nested group"
        )
    }

    @Test
    fun `filter appends to a custom output list if provided`() {
        // Arrange
        val instruction1 = TestInstruction("foo")
        val instruction2 = TestInstruction("bar")
        val group = AdaptiveInstructionGroup(listOf(instruction1, instruction2))
        val preExistingList = mutableListOf<AdaptiveInstruction>(TestInstruction("alreadyHere"))

        // Act
        val matched = group.filter(preExistingList) { it is TestInstruction && it.id == "foo" }

        // Assert
        assertEquals(
            listOf(TestInstruction("alreadyHere"), instruction1),
            matched,
            "Expected matches to be appended to the provided 'out' list"
        )
        // Note: the list now contains both the pre-existing item and the matched instruction1.
    }

    @Test
    fun `filterIsInstance should return all instructions of the specified type`() {
        // Arrange
        val foo1 = FooInstruction("foo1")
        val foo2 = FooInstruction("foo2")
        val bar1 = BarInstruction("bar1")
        val group = AdaptiveInstructionGroup(listOf(foo1, bar1, foo2))

        // Act
        val fooInstructions = group.filterIsInstance<FooInstruction>()

        // Assert
        assertEquals(
            listOf(foo1, foo2), fooInstructions,
            "Expected filterIsInstance to return only FooInstruction items"
        )
    }

    @Test
    fun `filterIsInstance should return an empty list if no matching type is found`() {
        // Arrange
        val bar1 = BarInstruction("bar1")
        val bar2 = BarInstruction("bar2")
        val group = AdaptiveInstructionGroup(listOf(bar1, bar2))

        // Act
        val fooInstructions = group.filterIsInstance<FooInstruction>()

        // Assert
        assertTrue(
            fooInstructions.isEmpty(),
            "Expected an empty list when no FooInstruction is present"
        )
    }

    @Test
    fun `filterIsInstance should include instructions from nested groups`() {
        // Arrange
        val fooNested = FooInstruction("nestedFoo")
        val childGroup = AdaptiveInstructionGroup(listOf(fooNested))
        val parentGroup = AdaptiveInstructionGroup(listOf(childGroup))

        // Act
        val fooInstructions = parentGroup.filterIsInstance<FooInstruction>()

        // Assert
        assertEquals(
            listOf(fooNested), fooInstructions,
            "Expected to find the FooInstruction from nested group"
        )
    }

    @Test
    fun `filterIsInstance should return instructions from multiple nested levels`() {
        // Arrange
        val deeplyNestedFoo = FooInstruction("deeplyNestedFoo")
        val level2Group = AdaptiveInstructionGroup(listOf(deeplyNestedFoo))
        val level1Group = AdaptiveInstructionGroup(listOf(level2Group))
        val rootGroup = AdaptiveInstructionGroup(listOf(level1Group))

        // Act
        val fooInstructions = rootGroup.filterIsInstance<FooInstruction>()

        // Assert
        assertEquals(
            listOf(deeplyNestedFoo), fooInstructions,
            "Expected to find FooInstruction that is multiple levels deep"
        )
    }

    /**
     * A simple [AdaptiveInstruction] implementation for testing
     * that counts how many times it was "visited".
     */
    private data class VisitedInstruction(val id: String, var visits: Int = 0) : AdaptiveInstruction {
        override fun applyTo(subject: Any) {
            // No-op
        }

        override fun matchOrNull(predicate: (AdaptiveInstruction) -> Boolean): AdaptiveInstruction? {
            return if (predicate(this)) this else null
        }
    }

    @Test
    fun `forEach visits all instructions in a flat group`() {
        // Arrange
        val instr1 = VisitedInstruction("instr1")
        val instr2 = VisitedInstruction("instr2")
        val group = AdaptiveInstructionGroup(listOf(instr1, instr2))

        // Act
        group.forEach {
            if (it is VisitedInstruction) {
                it.visits ++
            }
        }

        // Assert
        assertEquals(1, instr1.visits, "Expected instr1 to be visited exactly once")
        assertEquals(1, instr2.visits, "Expected instr2 to be visited exactly once")
    }

    @Test
    fun `forEach visits instructions in nested groups`() {
        // Arrange
        val nestedInstr = VisitedInstruction("nested")
        val childGroup = AdaptiveInstructionGroup(listOf(nestedInstr))
        val rootInstr = VisitedInstruction("root")
        val parentGroup = AdaptiveInstructionGroup(listOf(rootInstr, childGroup))

        // Act
        parentGroup.forEach {
            if (it is VisitedInstruction) {
                it.visits ++
            }
        }

        // Assert
        assertEquals(1, rootInstr.visits, "Expected the root-level instruction to be visited once")
        assertEquals(1, nestedInstr.visits, "Expected the instruction in the nested group to be visited once")
    }

    @Test
    fun `toMutableList should flatten nested groups into a single list`() {
        // Arrange
        val leaf1 = TestInstruction("leaf1")
        val leaf2 = TestInstruction("leaf2")
        val leaf3 = TestInstruction("leaf3")
        val childGroup = AdaptiveInstructionGroup(listOf(leaf3))
        val parentGroup = AdaptiveInstructionGroup(listOf(leaf1, childGroup, leaf2))

        // Act
        val flattenedList = parentGroup.toMutableList()

        // Assert
        // We expect the flattened list to contain leaf1, leaf3 (from the child group), and leaf2
        assertEquals<List<AdaptiveInstruction>>(
            listOf(leaf1, leaf3, leaf2),
            flattenedList,
            "Expected nested instructions to be flattened in the resulting list"
        )
    }

    @Test
    fun `toMutableList appends to an existing list if provided`() {
        // Arrange
        val leaf1 = TestInstruction("leaf1")
        val leaf2 = TestInstruction("leaf2")
        val group = AdaptiveInstructionGroup(listOf(leaf1, leaf2))

        val preExisting = mutableListOf<AdaptiveInstruction>(TestInstruction("alreadyHere"))

        // Act
        val resultList = group.toMutableList(preExisting)

        // Assert
        // 'alreadyHere' should remain, followed by 'leaf1' and 'leaf2'
        assertEquals<List<AdaptiveInstruction>>(
            listOf(TestInstruction("alreadyHere"), leaf1, leaf2),
            resultList,
            "Expected the pre-existing list to be appended with instructions from the group"
        )
    }

    @Test
    fun `toMutableList should return an empty list for an empty group`() {
        // Arrange
        val emptyGroup = AdaptiveInstructionGroup(emptyList())

        // Act
        val resultList = emptyGroup.toMutableList()

        // Assert
        assertEquals(
            emptyList<AdaptiveInstruction>(),
            resultList,
            "Expected an empty list from an empty instruction group"
        )
    }

    class AdaptiveInstructionGroupTest {

        @Test
        fun `size should be 0 when group is empty`() {
            // Given
            val group = AdaptiveInstructionGroup(emptyList())

            // When
            val actualSize = group.size

            // Then
            assertEquals(0, actualSize, "Expected size to be 0 for empty group")
        }

        @Test
        fun `size should be 1 when group has a single instruction`() {
            // Given
            val instruction = TestInstruction("1")
            val group = AdaptiveInstructionGroup(listOf(instruction))

            // When
            val actualSize = group.size

            // Then
            assertEquals(1, actualSize, "Expected size to be 1 for group with one instruction")
        }

        @Test
        fun `size should match number of instructions in a flat list`() {
            // Given
            val instructions = listOf(
                TestInstruction("1"),
                TestInstruction("2"),
                TestInstruction("3")
            )
            val group = AdaptiveInstructionGroup(instructions)

            // When
            val actualSize = group.size

            // Then
            assertEquals(3, actualSize, "Expected size to be equal to the number of instructions in a flat list")
        }

        @Test
        fun `size should count nested groups' instructions`() {
            // Given
            val nestedGroup1 = AdaptiveInstructionGroup(listOf(TestInstruction("1"), TestInstruction("2")))
            val nestedGroup2 = AdaptiveInstructionGroup(listOf(TestInstruction("3")))
            val topLevelGroup = AdaptiveInstructionGroup(listOf(
                TestInstruction("4"),
                nestedGroup1,
                nestedGroup2,
                TestInstruction("5")
            ))

            // When
            val actualSize = topLevelGroup.size

            // Then
            assertEquals(5, actualSize, "Expected size to be the sum of all instructions, including nested groups")
        }

        @Test
        fun `size should handle multiple nested subgroups`() {
            // Given
            val deeplyNestedGroup = AdaptiveInstructionGroup(
                listOf(
                    TestInstruction("1"),
                    AdaptiveInstructionGroup(listOf(TestInstruction("2"), TestInstruction("3")))
                )
            )
            val topLevelGroup = AdaptiveInstructionGroup(
                listOf(
                    TestInstruction("4"),
                    deeplyNestedGroup,
                    TestInstruction("5")
                )
            )

            // When
            val actualSize = topLevelGroup.size

            // Then
            assertEquals(5, actualSize, "Expected size to recursively include all nested instructions")
        }
    }

}