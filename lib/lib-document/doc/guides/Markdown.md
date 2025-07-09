# Markdown

[examples](actualize://example-group?name=markdown)

## Details

Supported Markdown structures:

- header
- paragraph
- link
- list (bullet and numbered)
- bold, italic, inline code
- code fence
- quote
- horizontal line
- image

## Compiler

[MarkdownCompiler](class://) provides parser and tokenizer functions for Markdown. With those
you can get an AST of [MarkdownElement](class://) instances and process them further with
visitors and transformers.

## Visitor and transformer

Markdown supports the standard visitor/transformer interface:

- [MarkdownVisitor](class://)
- [MarkdownTransformer](class://)
- [MarkdownTransformerVoid](class://)

Built-ins:

- [MarkdownAstDumpVisitor](class://)
- [MarkdownToDocVisitor](class://)
- [MarkdownToMarkdownVisitor](class://)
- [MarkdownToTreeVisitor](class://)