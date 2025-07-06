package `fun`.adaptive.grove.doc.ui

import `fun`.adaptive.document.ui.direct.h3
import `fun`.adaptive.document.ui.direct.markdown
import `fun`.adaptive.document.ui.direct.markdownHint
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.producer.fetch
import `fun`.adaptive.grove.doc.model.GroveDocValue
import `fun`.adaptive.grove.doc.model.groveDocDomain
import `fun`.adaptive.markdown.compiler.MarkdownCompiler
import `fun`.adaptive.markdown.model.MarkdownElement
import `fun`.adaptive.markdown.model.MarkdownElementGroup
import `fun`.adaptive.markdown.model.MarkdownHeader
import `fun`.adaptive.markdown.model.MarkdownParagraph
import `fun`.adaptive.markdown.transform.MarkdownAstDumpVisitor.Companion.dump
import `fun`.adaptive.markdown.transform.MarkdownToMarkdownVisitor.Companion.toMarkdown
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.badge.BadgeTheme
import `fun`.adaptive.ui.badge.badge
import `fun`.adaptive.ui.generated.resources.arrow_right
import `fun`.adaptive.ui.icon.icon
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.loading.loading
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace.Companion.wsToolOrNull
import `fun`.adaptive.ui.mpw.fragments.contentPaneHeader
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.ui.viewbackend.viewBackend

@Adaptive
fun inlineDefinition(
    args: Map<String, String>
): AdaptiveFragment {

    val viewBackend = viewBackend(DocContentViewBackend::class)
    val valueOrNull = fetch { viewBackend.getDefinition(args["name"] ?: "<no-name>") }

    loading(valueOrNull) { value ->
        column {
            maxWidth .. backgrounds.surface .. borders.outline .. cornerRadius { 4.dp }

            row {
                maxWidth .. borderBottom(colors.outline)
                paddingHorizontal { 16.dp } .. paddingVertical { 4.dp }
                backgroundGradient(position(0.5.dp, 0.dp), position(1.dp, 1.dp), colors.surface, colors.successSurface)
                cornerTopRadius { 4.dp }
                spaceBetween
                alignItems.center

                h3(value.nameLike)
                badge(groveDocDomain.definition, theme = BadgeTheme.success)
            }

            column {
                padding { 16.dp }
                markdown(extractDef(value.spec.content))
            }
        }
    }

    return fragment()
}

fun extractDef(content: String): String {
    val fullAst = MarkdownCompiler.ast(content)

    val seeAlso = fullAst.indexOfLast { it is MarkdownHeader }
    if (seeAlso == - 1) return content

    val ast = fullAst.subList(0, seeAlso)

    val last = ast.last()
    if (last is MarkdownParagraph) {
        last.closed = true
    }

    return ast.toMarkdown()
}