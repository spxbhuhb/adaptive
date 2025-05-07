---
title: Training Documentation Style Guide
tags: [documentation, guide, kotlin, adaptive, ai, training]
language: markdown
---

# Training Documentation Style Guide 

This guide outlines the conventions and best practices for writing technical documentation for training AI for Adaptive.
It is intended to ensure clarity, consistency, and training-readiness for both humans and AI systems.

---

## Overall Structure

Each guide should follow this high-level structure:

1. **Summary** – One-paragraph overview of what the guide covers.
2. **Objective** – A clear statement of what is to be implemented or understood.
3. **Steps** – Ordered sections for each main implementation component.
4. **Usage Example** – Real-world code showing integration.
5. **Notes** – Important implementation or conceptual details.
6. **See Also** – Links to related documentation.
7. **Conclusion** – High-level summary and intent reinforcement.

---

## Step-by-Step Section Structure

Each implementation step (e.g., module class) should follow this pattern:

### Header

Start with the file path:

```markdown
**File**: `src/commonMain/kotlin/your/package/filename.kt`
```

### Purpose

A short paragraph explaining **why** the file exists and what it does.

### Code

Use a fenced code block with language annotation:

```kotlin
class YourModule : BaseModule() {
    // logic here
}
```

Code should:
- Use real identifiers and imports.
- Be executable when possible (no incomplete stubs).
- Include clear Kotlin idioms like `+ Type()` where applicable in DSLs.

### Explanation

Follow each code block with a paragraph that:
- Describes what the code does.
- Clarifies context like imports, generated packages, or DSL macros.
- Notes when and how the class/module is used at runtime.

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

- Use H1 (`#`) for document title.
- Use H2 (`##`) for sections like Summary, Objective, Steps, etc.
- Use H3 (`###`) for subsections: Purpose, Code, Explanation.
- Use fenced code blocks with language specifiers (e.g., `kotlin`, `markdown`).
- Use list formatting for tasks or step-by-step breakdowns.
- Set front-matter at the top with `language`, `title`, and relevant `tags`.

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