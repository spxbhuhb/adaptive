---
title: Training Documentation Style Guide
tags: [documentation, adaptive, ai, training]
type: conceptual
---

# Purpose

This guide outlines the conventions and best practices for writing technical documentation for training AI for Adaptive.
It is intended to ensure clarity, consistency, and training-readiness for both humans and AI systems.

---

# Objective

Define a common set of rules that govern the format of Adaptive AI training documentation.

---

# Key Concepts

## Guide formats

There are two main guide formats: procedural and conceptual.

Use **procedural** format when:

- The document explains *how to implement* or set up a feature or module.
- You want to guide the reader through file-by-file, class-by-class, or step-by-step configuration and construction.
- You are explaining how to build something from start to finish using concrete code and structure.

Use **conceptual** format when:
- The document explains *how something works*, not *how to implement it*.
- You want to describe **runtime behavior**, **data flow**, **communication mechanisms**, or **non-class-based patterns**.
- You are explaining *what the system does* and *why* rather than *how to set it up*.

---

## Overall Structure

Both guide formats should follow this high-level structure:

1. **Summary** – One-paragraph overview of what the guide covers.
2. **Objective** – A clear statement of what is to be implemented or understood.
3. Procedural-specific or conceptual-specific sections, depending on the guide type.
4. **Usage Example** (optional) – Real-world code showing integration.
5. **Notes** (optional) – Important implementation or conceptual details.
6. **See Also** (optional) – Links to related documentation.
7. **Conclusion** – High-level summary and intent reinforcement.

---

## Procedural-Specific Sections

The format-specific part of procedural guides contains one section:

1. **Steps** - Ordered sections for each main implementation step.

### Steps Section Structure

Each implementation step should follow this structure:

1. **Header** - Meta information about the step (such as file or directory name).
2. **Purpose** - A short paragraph explaining **why** the output (such as a file, code, etc.) of the step exists and what it does.
3. **Code** - A fenced code block with language annotation.
4. **Explanation** - Explanation of the step and the code.

Example:

``````markdown
## 1. Write an example class

**File**: `src/commonMain/kotlin/your/package/filename.kt`

### Purpose

This file contains the code needed for the example.

### Code

```kotlin
class YourModule : BaseModule() {
    // logic here
}
```

### Explanation

This is the explanation of the code above.
``````

Code should:
- Use real identifiers and imports.
- Be executable when possible (no incomplete stubs).
- Include clear Kotlin idioms like `+ Type()` where applicable in DSLs.

Explanation follows each code block with a paragraph that:
- Describes what the code does.
- Clarifies context like imports, generated packages, or DSL macros.
- Notes when and how the class/module is used at runtime.

---

## Conceptual-Specific Sections

The format-specific part of conceptual guides contains these sections:

1. **Key Concepts** – Describe what the mechanism is, what terms it involves, and the core principles it follows.
2. **Components and Configuration** (optional)– Break down the relevant classes, interfaces, modules, or settings involved.
3. **Behavior or Flow** (optional) – Describe how the mechanism behaves in runtime, or the logical/data flow involved.

---

## Do's and Don'ts

### Do

- Use full import paths where relevant.
- Add build-related config (e.g., Gradle snippets) to link documentation to build behavior.
- Clarify domain-specific terms (e.g., Adat classes).
- Mention reusability patterns and testability when relevant.

### Don't

- Leave placeholders like `TODO()` or `// register here` without explanation.
- Use generic names (`MyClass`, `Example`) unless the document is explicitly about templates.
- Skip usage context for classes.

---

## Section Metadata and Formatting

- Use H1 (`#`) for major content sections like Summary, Objective, Steps, Key Concepts, etc.
  - Do not use H1 for the document title; that belongs in the front matter.
- Use H2 (`##`) for procedural steps or for main concepts.
- Use H3 (`###`) for subsections: Purpose, Code, Explanation.
- Use fenced code blocks with language specifiers (e.g., `kotlin`, `markdown`).
- Set front-matter at the top with `language`, `title`, and relevant `tags`.
- Prefer **bullets or short paragraphs**.
- Apply **front-matter metadata**:
  ```markdown
  ---
  title: Service Transport
  tags: [service, transport]
  type: conceptual
  ---
  ```
---

## Linking and Modularity

- Add a “See also” section linking to other guides.
- Use relative links when integrating into a doc site.
- Consider modular breakdown (e.g., one doc for directory layout, one for module design, etc.)

---

## AI and Developer Considerations

This format is optimized for:

- Fine-tuning LLMs on Kotlin code and architectural patterns.
- Developer onboarding and project documentation.
- Static documentation sites like MkDocs or Docusaurus.