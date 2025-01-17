import `fun`.adaptive.adat.encodeToJson
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.grove.hydration.lfm.LfmConst
import `fun`.adaptive.grove.hydration.lfm.LfmDescendant
import `fun`.adaptive.grove.hydration.lfm.LfmMapping
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Position
import `fun`.adaptive.utility.UUID
import kotlin.test.Test

class TestSandbox {

    @Test
    fun test() {

        LfmDescendant(
            UUID<LfmDescendant>(),
            "aui:text",
            listOf(
                LfmMapping(
                    dependencyMask = 0,
                    LfmConst("T", "Hello World!")
                ),
                LfmMapping(
                    dependencyMask = 0,
                    LfmConst(
                        "Lfun.adaptive.foundation.instruction.AdaptiveInstructionGroup;",
                        AdaptiveInstructionGroup(
                            listOf(
                                Position(10.dp, 10.dp)
                            )
                        )
                    )
                )
            )
        )

        AdaptiveInstructionGroup(
            listOf(
                Position(10.dp, 10.dp)
            )
        ).encodeToJson()
    }
}