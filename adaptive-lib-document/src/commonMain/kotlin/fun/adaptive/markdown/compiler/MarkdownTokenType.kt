package `fun`.adaptive.markdown.compiler

enum class MarkdownTokenType {
    Spaces,
    Text,
    NewLine,
    Header,
    BulletList,
    NumberedList,
    Quote,
    CodeLanguage,
    CodeFence,
    Asterisks,
    Underscores,
    Hyphens,
    CodeSpan,
    InlineLink,
    ReferenceLink,
    ReferenceDef
}