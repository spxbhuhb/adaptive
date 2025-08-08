package `fun`.adaptive.doc.support

import `fun`.adaptive.doc.example.generated.resources.code
import `fun`.adaptive.doc.example.generated.resources.full_code
import `fun`.adaptive.doc.example.generated.resources.hideCode
import `fun`.adaptive.doc.example.generated.resources.showCode
import `fun`.adaptive.doc.example.generated.resources.showFullCode
import `fun`.adaptive.document.ui.direct.h3
import `fun`.adaptive.document.ui.direct.markdownHint
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.api.actualize
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.producer.fetch
import `fun`.adaptive.grove.doc.api.GroveDocApi
import `fun`.adaptive.grove.doc.model.GroveDocExample
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.badge.BadgeTheme
import `fun`.adaptive.ui.badge.badge
import `fun`.adaptive.ui.codefence.CodeFenceTheme
import `fun`.adaptive.ui.codefence.codeFence
import `fun`.adaptive.ui.generated.resources.content_copy
import `fun`.adaptive.ui.generated.resources.copied
import `fun`.adaptive.ui.generated.resources.copyToClipboard
import `fun`.adaptive.ui.generated.resources.example
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.icon.denseIconTheme
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.loading.loading
import `fun`.adaptive.ui.misc.todo
import `fun`.adaptive.ui.platform.clipboard.copyToClipboard
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.colors

@Adaptive
fun exampleGroup(
    args: Map<String, String>
): AdaptiveFragment {

    val examplesOrNull = fetch { getService<GroveDocApi>(adapter().transport).getExampleGroup(args["name"] ?: "<no-name>") }

    val filter = args["filter"]

    loading(examplesOrNull) { examples ->
        examplePanes(
            args["name"],
            args["ui"]?.toBooleanStrict() ?: true,
            examples.filter { filter == null || it.repoPath.contains(filter, ignoreCase = true) }
        )
    }

    return fragment()
}

@Adaptive
fun examplePanes(
    groupName: String?,
    ui : Boolean,
    examples: List<GroveDocExample>
): AdaptiveFragment {
    when {
        groupName == null -> {
            todo("no example group name is provided, check the URL")
        }

        examples.isEmpty() -> {
            todo("no examples with $groupName group are defined")
        }

        else -> {
            column {
                gap { 32.dp }
                for (example in examples) {
                    examplePane(example)
                }
            }
        }
    }

    return fragment()
}

enum class CodeMode {
    Example,
    Full,
    Hidden;

    fun toggleTo(to: CodeMode): CodeMode =
        when (this) {
            Example -> if (to == Full) Full else Hidden
            Full -> if (to == Example) Example else Hidden
            Hidden -> to
        }

    fun hintFor(forMode: CodeMode) =
        when (this) {
            Example -> if (forMode == Example) Strings.hideCode else Strings.showFullCode
            Full -> if (forMode == Full) Strings.hideCode else Strings.showCode
            Hidden -> if (forMode == Example) Strings.showCode else Strings.showFullCode
        }

}

val topCodeFence = CodeFenceTheme().also {
    it.codeFenceContainer = it.codeFenceContainerBase + borderTop(colors.outline)
}

@Adaptive
fun examplePane(
    example: GroveDocExample
) {
    var codeMode = CodeMode.Hidden

    column {
        maxWidth .. borders.outline .. cornerRadius { 4.dp }

        row {
            maxWidth .. borderBottom(colors.outline)
            paddingHorizontal { 16.dp } .. paddingVertical { 4.dp }
            backgroundGradient(position(0.5.dp, 0.dp), position(1.dp, 1.dp), colors.surface, colors.infoSurface)
            cornerTopRadius { 4.dp }
            spaceBetween
            alignItems.center

            h3(example.name)
            badge(Strings.example.lowercase(), theme = BadgeTheme.info)
        }

        if (example.explanation.isNotEmpty()) {
            column {
                padding { 16.dp } .. borderBottom(colors.outline)
                markdownHint(example.explanation)
            }
        }

        column {
            maxWidth .. padding { 24.dp } .. backgrounds.surfaceVariant .. cornerRadius { 4.dp }
            actualize(example.fragmentKey, example)
        }

        column {
            borderTop(colors.outline) .. maxWidth

            row {
                maxWidth .. spaceBetween

                row {
                    paddingHorizontal { 8.dp } .. gap { 8.dp }

                    actionIcon(Graphics.code, codeMode.hintFor(CodeMode.Example), theme = denseIconTheme) {
                        codeMode = codeMode.toggleTo(CodeMode.Example)
                    }

                    actionIcon(Graphics.full_code, codeMode.hintFor(CodeMode.Full), theme = denseIconTheme) {
                        codeMode = codeMode.toggleTo(CodeMode.Full)
                    }
                }

                actionIcon(Graphics.content_copy, Strings.copyToClipboard, theme = denseIconTheme, actionFeedbackText = Strings.copied) {
                    copyToClipboard(if (codeMode == CodeMode.Full) example.fullCode else example.exampleCode)
                }
            }

            when (codeMode) {
                CodeMode.Example -> codeFence(code = example.exampleCode, language = "kotlin", theme = topCodeFence)
                CodeMode.Full -> codeFence(code = example.fullCode, language = "kotlin", theme = topCodeFence)
                CodeMode.Hidden -> {}
            }
        }
    }
}