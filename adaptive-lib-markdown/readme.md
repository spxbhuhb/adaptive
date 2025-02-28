# Markdown

## Markdown to LFM

`MarkdownToLfmTransform` transforms a list of `MarkdownAstEntry` into an `LfmFragment`.

Transformations:

| Markdown AST entry               | Fragment        | Notes                            |
|----------------------------------|-----------------|----------------------------------|
| `MarkdownCodeFenceAstEntry`      | `lib:codefence` |                                  | 
| `MarkdownHeaderAstEntry`         | `aui:paragraph` |                                  |
| `MarkdownHorizontalRuleAstEntry` | `aui:box`       | with decorations and max width   |
| `MarkdownParagraphAstEntry`      | `aui:paragraph` |                                  |
| `MarkdownInlineAstEntry`         |                 | `ParagraphItem` - not a fragment |
| `MarkdownListAstEntry`           | `lib:listWrap`  |                                  |
| `MarkdownQuoteAstEntry`          | `lib:quoteWarp` |                                  |

### Linearization

`MarkdownToLfmTransform` uses a sequence stack to linearize structured AST entries such as
quotes and lists.

When such an AST entry is processed, the entry:

* creates a new mutable list in the sequence stack
* processes the child entries
* removes the sequence from the sequence stack

When the transformation starts, the sequence stack contains the mutable list a that will
store all the descendants at the end of the transformation.

### Instruction sets

The transform builds a single instruction set which is passed to all transformed paragraphs.
To make instructions set matching efficient, the transform encodes the state of the markdown
AST entry into a mask which is then used to select/generate the instruction sets.