# Changelog

## New UI fragments

`doc*`

- Render complex documents.
- [Recipe](aui://cookbook:recipe:markdown:demo)

`wrap*`

- Add wrappers around fragments that are rendered with unbound sizes.
- [Recipe](aui://cookbook:recipe:wrap)

`codeFence`

- Render source codes.
- [Recipe](aui://cookbook:recipe:codefene)

`tab` (work in progress)

- Tab container with multiple, switchable tabs.
- [Recipe](aui://cookbook:recipe:tab)

## New functionality

### Markdown

- `MarkdownCompiler` provides easy access to Markdown parsing, transforming and inspection
- `MarkdownVisitor` can be used to traverse Markdown AST trees

### Document

- abstract document model
- `DocVisitor` can be used to traverse the document model

### Layout

- `fill.constraint` instruction for `row` and `column`

## Improvements

- `box`, `column` and `row` now proposes unbound size when scroll is enabled (direction specific)
- `SplitPane` now handles fragment wrapping
- `SVG` resource caching
