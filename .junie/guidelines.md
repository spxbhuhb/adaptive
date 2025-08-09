# Guidelines

This repository has extensive docs and many examples. These guidelines are optimized for AI code assistance and fast, safe navigation.

## Safe operations and constraints

- Do NOT start a Gradle build under any circumstances. The project is large, builds are slow; it is not practical for ad-hoc validation.
- Use repository-aware tools to explore code and docs:
  - To find files/symbols/text: search_project
  - To see a file’s structure (imports, symbols): get_file_structure
  - To open specific portions of a file: open (use line_number)
  - Avoid shell utilities for wide searches; do not rely on terminal find/grep for project-wide indexing.
- Make minimal, targeted changes. Prefer editing an existing file over adding new files unless necessary for clarity.

## Where to look first (5-minute map)

- Start here: /doc/junie-local/guides/Documentation.md
  - Foundation: Adapters, Fragments, Instructions, Producers, Local context
  - UI: Writing a UI fragment, Resources, Layout, Styles and themes, UI Fragments, Inputs and editors, Events
  - Data: Adat, Value, WireFormat
  - Application: Module definition, Built-in lib-app features, Navigation, Server/Services
  - Documents: Document/Markdown/Xlsx
- Common quick links:
  - Writing a UI fragment: /doc/junie-local/guides/Writing a UI fragment.md
  - Resources: /doc/junie-local/guides/Resources.md
  - Navigation: /doc/junie-local/guides/Navigation.md
  - Date input (example of input patterns): /doc/junie-local/guides/Date input.md
  - Document/Markdown/Xlsx: /doc/junie-local/guides/Document, Markdown and Xlsx.md
- For topic-specific docs, list files under /doc/junie-local/guides

## Fragments and Instructions (quick checklist)

Functions annotated with @Adaptive define fragments.

- Read first:
  - Foundation: /doc/junie-local/guides/Foundation.md
  - Instruction rules: /doc/junie-local/guides/Instruction.md
- Boundary: Keep state definition and rendering clearly separated as described in the Foundation and Instruction guides.
- Where instructions can appear:
  1) Immediately after the opening { of an Adaptive lambda
  2) As function parameters, if the called function supports it
  3) After a rendering statement that returns an AdaptiveFragment, using the .. operator
- Where instructions cannot appear:
  - Between separate fragments (you can’t interleave arbitrary instructions between siblings).
- Common pitfalls to avoid:
  - Mixing state mutations with render calls in the same step when Instruction guide says to keep them separate.
  - Passing instructions to functions that don’t accept them as parameters.

## Working method (for AI assistants)

- Search first, then open:
  1) search_project for symbols/files/text
  2) get_file_structure to preview contents
  3) open a narrow window around the relevant lines
- Prefer editing the smallest possible unit. If adding a file is necessary, keep it short and place it alongside related files.
- Respect existing patterns and naming. Follow example locations and file naming conventions.

## Tests and validation

- Do not trigger a Gradle build.
- If a behavior must be verified and there is no quick, local check, document the expected result and rationale instead of running a long test cycle.
- If tests are edited/added, make them skippable when long build steps would be required.

## Comments and documentation

- Do NOT add trivial comments such as:

```kotlin
// 1) Flatten all paths
val allPaths = collection.values.flatten()
```

- Do NOT add trivial KDoc like "Calculates the width" for a one-liner. If you don’t fully understand a function, don’t document it.
- Prefer meaningful, non-obvious clarifications:

```kotlin
// Using qualifier fallback: prefer density-specific image; default to base if absent.
val image = findQualifiedImage(preferred = "xhdpi") ?: findQualifiedImage()
```

- If documenting fragment usage, reference the relevant guide and the allowed instruction positions, e.g.:

```kotlin
/**
 * Applies a hover effect via decoration instructions after rendering.
 * See Instruction guide for allowed placement and .. operator semantics.
 */
```

## Quick links (by category)

- Foundation: Adapters, Fragments, Instructions, Producers, Local context, Fragment transform
- UI: Writing a UI fragment, Resources (+ Fonts, Resource tips), Layout (+ Scrolling, Layout instructions), Styles and themes (+ Decoration instructions, Colors, Themes), UI fragments (Common, Layout, Inputs and editors, High-level, Other), UI Producers (Focus, Hover, Media metrics, Value producers), UI Events (+ Event instructions), Values in the UI
- Data: Adat (classes, functions, metadata, writing), Value (store, domain, trees, embedded server, persistence providers, subscribers, execute blocks), WireFormat (low-level JSON, JSON transform, Protobuf details)
- Application: Module definition, Built-in lib-app features, Navigation (basic, MPW), Server/Services (+ Service context, transport), Workers
- Documents: Document, Markdown, Xlsx

Refer to /doc/junie-local/guides/Documentation.md for the full index.