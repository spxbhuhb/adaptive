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

All file names related to documentation **MUST BE UNIQUE** through the whole repository. 
This includes file names, class names, everything.

This was a design decision for multiple reasons:

- I really like to know what I'm talking about,
- I want to force myself to call things what they are,
- I hate when an import is ambiguous.

### Example code

1. **DO NOT** put Kotlin code fences into Markdown.
2. Example code that can be placed inside the module shall be in the `commonTest/example` directory.
3. Example code that cannot be placed inside the module shall be in the `doc-example`
4. Reference example code with the `[FileName](example://)` shorthand.

### Todos

When you do not have time to finish up all the documentation use `[explanation about what to write](todo://)`

### Shorthands

Shorthands are Markdown links in the following format:

```markdown
[name](type://)
```

1. The documentation **SHALL** use shorthands whenever possible.
2. The documentation system resolves these shorthands by the `name` and `type`.
3. There must be nothing after `://`
4. Name resolution rules:
   1. Names are case-insensitive and trimmed. 
   2. If there is a question mark at the end of the name, it is removed.
   3. If a name cannot be found but ends with a `s`, the name without the `s` is tried.

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

The `def://` link tells the documentation building system to look up the definition by name.

### Guides

1. Put all [guides](def://) into the `guides` directory.
2. One file per guide.
3. The name of the file is the name of the guide.
4. You can use images in guides.

These are longer documents, explaining concepts, use cases, code patterns in detail.

When referencing a guide from the documentation, use the following format:

```markdown
[Name of the guide](guide://)
```

### Other shorthands

The documentation system supports these lookups in addition to `def://` and `guide://`:

- `[AdaptiveFragment](class://)` a link to a class (on GitHub)
- `[jsonTransformExample](example://)` example code (replaced with a code fence)
- `[Some picture](image://)` link to the image file

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

### Shorthand resolution

Shorthands (such as `[guides](def://)`) are resolved by the documentation compiler.

There are two different resolution methods:

1. Close for `def`, `guide` and `image`.
2. Far for `class`.

The difference is that close resolution simply puts the name of the file into
the Markdown link, while far resolution points somewhere deep into the project.