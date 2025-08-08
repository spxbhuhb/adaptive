# Documentation system

This document describes the documentation system of Adaptive and the documentation guidelines to follow.

## Understanding markdown links

Documentation of Adaptive uses special schemas in Markdown links. These are smart references 
that are resolved by the documentation compiler and/or by the application during runtime
(in case of `actualize://`).

- `def://` references to a Markdown file in one of the `doc/definitions` directories
- `guide://` references to a Markdown file in one of the `doc/guides` directories`
- `internals://` references to a Markdown file in one of the `doc/internals` directories
- `class://` references to a Kotlin class, turns into a link to the source code on GitHub
- `function://` references to a Kotlin function, turns into a link to the source code on GitHub
- `property://` references to a Kotlin property, turns into a link to the source code on GitHub
- `example://` references to a Kotlin file, the source code will be included as a code fence
- `fragment://` references to a Kotlin function with the `@Adaptive` or `@AdaptiveExpect` annotation
- `actualize://` means to include an actual, running code when the Markdown is rendered, see also [Example Code](#example-code)

## Guidelines

### Directory structure

1. Top level documentation (such as this one) is in the [doc-main](def://) subproject.
2. [Subproject](def://) documentation is in the `doc` directory of the given subproject.

`doc` directories may contain these subdirectories, each containing the given type of documents:

1. `definitions`
2. `guides`
3. `internals`

All non-class file names related to documentation **SHALL** be unique through the whole repository in 
respect of the document type.

This was a design decision for multiple reasons:

- I really like to know what I'm talking about,
- I want to force myself to call things what they are,
- I hate when an import is ambiguous.

### Metadata

Put metadata into the Markdown files by adding a YAML header:

```text
---
status: planning
markers: marker1, marker2
---
```

Statuses:

- planning
- todo
- review
- outdated

### Example code

1. **DO NOT** put Kotlin code examples into Markdown (except this document).
2. Put example code into `commonTest` of the given module when possible (except the cookbook).
3. Put example code into [doc-example](def://) if you cannot put it into a given module.
4. Put each example into a separate file, **DO NOT** put more than one example into a file.
5. Follow the pattern: `<order>_<group>_<name>_example.kt` where
    1. `<order>` is used to set the ordering of the examples in the group
    2. `<group>` is the name of the example group
    3. `<name>` is the name of the example, unique in the group
    4. the `example` suffix lets the documentation system collect the examples

The first documented declaration in the file marks the start of the actual example code.

The documentation of the example code should start with a Markdown header that
is the name of the example, followed by the explanation of the example.

```kotlin
/**
 * # With name and icon
 *
 * - Pass an icon in the `icon` parameter.
 */
```

Reference example code from Markdown like this:

```markdown
[examples](actualize://example-group?name=<group>)
```

The documentation system:

- collects all files named according to the example pattern above
- extracts information from the collected examples (name, explanation, code without imports)
- for human-readable documentation
  - keeps the link above as-is
  - puts the examples into the value store of [site-app](def://) with a [GroveDocExampleGroupSpec](class://)
  - when someone browses the documentation [exampleGroup](function://) fetches this value and renders example panes
- for AI training documentation
  - replaces the link with:
    - the name and explanation are lifted out into Markdown
    - the example code is put into a code fence

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
[Value](guide://)
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
* Output directories: 
  * `build/adaptive/doc`
  * `site/site-app/var/values`