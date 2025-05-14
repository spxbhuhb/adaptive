# Documentation system

This document describes the documentation system of Adaptive and the documentation guidelines to follow.

## Guidelines

### Directory structure

1. Top level documentation (such as this one) is in the [doc-main](def://) subproject.
2. [Subproject](def://) documentation is in the `doc` directory of the given subproject.

`doc` directories may contain these subdirectories:

1. `definitions`
2. `guides`
3. `internals`

All non-class file names related to documentation **SHALL** be unique through the whole repository. 
This includes file names, class names, everything.

This was a design decision for multiple reasons:

- I really like to know what I'm talking about,
- I want to force myself to call things what they are,
- I hate when an import is ambiguous.

### Example code

1. **DO NOT** put Kotlin code fences into Markdown.
2. Example code that can be placed inside the module shall be in `commonTest`.
3. Example code that cannot be placed inside the module shall be in [doc-example](def://)
4. Reference full file examples with the `[FileName](example://)` shorthand.
5. Reference function examples with the `[FunctionName](example://file-name)` shorthand

### Todos

When you do not have time to finish up all the documentation use `[explanation about what to write](todo://)`

### Shorthands

The documentation **SHALL** use shorthands whenever possible.

Shorthands are Markdown links in the following format:

```markdown
[name](type://scope)
```

Code shorthand is a shorthand that references to a class, function or property.

Examples of code shorthands:

```markdown
[AvValue](class://)
[AvTestSubscriberTest](example://)
[getSiblingIds](function://AvComputeContext)
[spec](property://AvValue)
```

Non-code shorthand is a shorthand that references to a Markdown file or an image.

Examples of non-code shorthands:

```markdown
[value](def://)
[what is a value](guide://)
[markers](guide://what is a value)
[some image](image://)
```

The documentation system resolves these shorthands by the `name`, `type` and `scope`.

- When there is no scope:
  - the name determines the file referenced by the shorthand.
- When there is a scope:
  - the scope determines the file referenced by the shorthand.

For non-code shorthands the following heuristics are applied during resolution:
   1. Names and scopes are converted to lowercase and trimmed. 
   2. If there is a question mark at the end, it is removed.
   3. If the target cannot be found and the name or scope seems to be a plural, the non-plural version will be tried (see [PluralHandler](class://))

### Definitions

1. Put all [definitions](def://) into the `definitions` directory.
2. One file per definition.
3. The name of the file is the name of the definition.
4. Do not use images in definitions.

These shall be short, exact descriptions, with references to [guides](def://) that explain the 
concepts, usage and other details.

When referencing a definition from the documentation, use the following format:

```markdown
[Name of the definition](def://)
```

### Guides

1. Put all [guides](def://) into the `guides` directory.
2. One file per guide.
3. The name of the file is the name of the guide.

These are longer documents, explaining concepts, use cases, code patterns in detail.

When referencing a guide from the documentation, use the following format:

```markdown
[Name of the guide](guide://)
```

To reference a section in a guide, use the scoped format:

```markdown
[Section](guide://Name of the guide)
```

### Images

1. Put the images to the `guides` directory.
2. The name of the file is the name of the image.

```markdown
[Some picture](image://)
```

### Classes, properties and functions

When referencing a class, a property or a function, use these formats:

```markdown
[FileName](class://)
[functionName](function://FileName)
[propertyName](property://FileName)
```

To reference classes that do not have a unique name, pass the fully qualified
file name in the scope:

```markdown
[FileName](class://my.example.util)
[functionName](function://my.example.util)
[propertyName](property://my.example.util)
```

### Inline code

To include inline example code, use one of these formats:

```markdown
[FileName](example://)
[functionName](example://FileName)
```

The example code will be included as a code fence.

## Compilation

The documentation has to be compiled to resolve the links.

* The `grove-doc` project contains the documentation compiler.
* The`compileAdaptiveDocumentation` Gradle task compiles all the documentation.
* Output directory: `build/adaptive/doc`

The output directory contains **ALL** the compiled documentation in one directory.

> [!NOTE]
> This might change in the future, but I would like to avoid a semantic directory
> structure because it is a pain to manage. Most probably I'll just use a value
> store and put all the files into it.