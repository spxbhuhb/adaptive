package `fun`.adaptive.ui.codefence

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.text

@Adaptive
fun codeFence(
    code: String,
    theme: CodeFenceTheme = CodeFenceTheme.DEFAULT
): AdaptiveFragment {

    val lines = code.lines()
    val height = theme.height(fragment(), lines.size)

    grid(instructions()) {
        theme.codeFenceContainer .. height

        column {
            theme.lineNumberColumn

            for (index in lines.indices) {
                box {
                    theme.lineNumberContainer
                    text(index) .. theme.lineNumberText
                }
            }
        }

        column {
            theme.codeColumn

            for (line in lines) {
                text(line) .. theme.codeText
            }
        }
    }

    return fragment()
}