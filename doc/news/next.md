# Changelog

## New UI fragments

`doc*`

- Render complex documents.
- [Documentation](/doc/document/readme.md)
- Recipe: Demos / Markdown

`wrap*`

- Add wrappers around fragments that are rendered with unbound sizes.
- Recipe: Layout / Wrap

`codeFence`

- Render source codes.
- Recipe: Textual fragments / Code Fence

`tab` (work in progress)

- Tab container with multiple, switchable tabs.
- Recipe: Layout / Tab

## New functionality

### UI

- `AuiImage` in browser now resizes the image according to instructions after it is actually loaded
- `box`, `column` and `row` now proposes unbound size when scroll is enabled (direction specific)
- `SplitPane` now handles fragment wrapping
- `SVG` resource caching

### Markdown

- `MarkdownCompiler` provides easy access to Markdown parsing, transforming and inspection
- `MarkdownVisitor` can be used to traverse Markdown AST trees

### Document

- abstract document model
- `DocVisitor` can be used to traverse the document model

### Resource

- `DocumentResourceSet` to have documents as application resources
- inline and remote resource handling
  - `remoteImage`
  - `remoteDocument`
  - `inlineDocument`

### Layout

- `fill.constraint` instruction for `row` and `column`

### Util

- `uuid7`
- `monotonicUuid7`
- `ByteArrayQueue`
- `TemporalRecordStore`

## Fixes

- function reference type conversion bugfix in plugin
