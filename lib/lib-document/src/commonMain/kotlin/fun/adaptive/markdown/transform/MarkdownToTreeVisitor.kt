package `fun`.adaptive.markdown.transform

import `fun`.adaptive.markdown.compiler.parseInternal
import `fun`.adaptive.markdown.compiler.tokenizeInternal
import `fun`.adaptive.markdown.model.*
import `fun`.adaptive.markdown.visitor.MarkdownVisitor
import `fun`.adaptive.ui.tree.TreeItem

/**
 * Transforms a markdown AST into a tree of TreeItems.
 * The top nodes of the tree are the headers in the markdown, and the lists are the children of them.
 *
 * @property ast The markdown AST to transform
 */
class MarkdownToTreeVisitor(
    val ast: List<MarkdownElement>,
    val makeFun: (title: String, link: String, parent: TreeItem<String>?) -> TreeItem<String>?
) : MarkdownVisitor<TreeItem<String>?, TreeItem<String>?>() {

    /**
     * Constructor that takes a markdown string and parses it into an AST.
     *
     * @param source The markdown string to parse
     */
    constructor(source: String, makeFun: ((title: String, link: String, parent: TreeItem<String>?) -> TreeItem<String>?)? = null) :
        this(
            parseInternal(tokenizeInternal(source)),
            makeFun ?: { title, link, parent ->
                TreeItem<String>(
                    icon = null,
                    title = title,
                    data = link,
                    open = false,
                    selected = false,
                    parent = parent
                )
            }
        )

    // Current header being processed
    private var currentHeader: TreeItem<String>? = null

    // Root items of the tree
    private val rootItems = mutableListOf<TreeItem<String>>()

    /**
     * Transforms the markdown AST into a tree of TreeItems.
     *
     * @return The list of root TreeItems
     */
    fun transform(): List<TreeItem<String>> {
        ast.forEach { it.accept(this, null) }
        return rootItems
    }

    override fun visitElement(element: MarkdownElement, data: TreeItem<String>?): TreeItem<String>? {
        // Default implementation, should be overridden by specific element visitors
        return null
    }

    override fun visitHeader(header: MarkdownHeader, context: TreeItem<String>?): TreeItem<String>? {
        // Extract the header text from the children
        val headerText = header.children.filterIsInstance<MarkdownInline>()
            .joinToString("") { it.text }

        // Create a new TreeItem for the header
        val headerItem = makeFun(headerText, headerText, null) ?: return null

        // Add the header to the root items
        rootItems.add(headerItem)

        // Set as the current header for subsequent list items
        currentHeader = headerItem

        return headerItem
    }

    override fun visitList(list: MarkdownList, context: TreeItem<String>?): TreeItem<String>? {
        // Process each list item
        val listItems = mutableListOf<TreeItem<String>>()

        list.items.forEach { item ->
            val treeItem = item.accept(this, context)
            if (treeItem != null) {
                listItems.add(treeItem)
            }
        }

        // If we have a current header, add the list items as its children
        if (currentHeader != null && listItems.isNotEmpty()) {
            currentHeader?.children = listItems
        }

        return null
    }

    override fun visitListItem(listItem: MarkdownListItem, context: TreeItem<String>?): TreeItem<String>? {
        // Extract the list item text from the content
        val item = extract(listItem.content) ?: return null

        // Create a TreeItem for the list item
        val treeItem = makeFun(item.first, item.second, context) ?: return null

        // Process sublists if any
        listItem.subList?.let { subList ->
            val subItems = mutableListOf<TreeItem<String>>()

            subList.items.forEach { subItem ->
                val subTreeItem = visitListItem(subItem, treeItem)
                if (subTreeItem != null) {
                    subItems.add(subTreeItem)
                }
            }

            if (subItems.isNotEmpty()) {
                treeItem.children = subItems
            }
        }

        return treeItem
    }

    override fun visitInline(inline: MarkdownInline, context: TreeItem<String>?): TreeItem<String>? {
        // We don't create TreeItems for inline elements directly
        return null
    }

    /**
     * Helper method to extract text from a markdown element.
     */
    private fun extract(element: MarkdownElement): Pair<String, String>? {
        return when (element) {

            is MarkdownInline -> {
                // Handle link syntax [text](url)
                val linkRegex = "\\[([^\\[]+)\\]\\(([^)]+)\\)".toRegex()
                val match = linkRegex.matchEntire(element.text)
                if (match != null) {
                    match.groupValues[1] to match.groupValues[2]
                } else {
                    element.text to element.text
                }
            }

            is MarkdownParagraph -> element.children.firstOrNull()?.let { extract(it) }
            is MarkdownElementGroup -> element.children.firstOrNull()?.let { extract(it) }
            else -> null
        }
    }
}
