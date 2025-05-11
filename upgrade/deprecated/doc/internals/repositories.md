# Repositories

To find components we need repositories:

- adat classes
    - hard-coded (Adat functions and properties coded manually)
    - plugin transformed (Kotlin code with `@Adat` annotation)
    - no-code (loaded from metadata)
- service APIs
    - hard-coded
    - no-code (loaded from metadata)
- wire formats
    - hard coded (for primitive, Kotlin collections, datetime types)
    - enumerations
    - no code (from adat classes)
- resources
    - strings
    - files
    - fonts
    - images
    - graphics
    - navigation
    - themes
- fragments
    - hard-coded fragments (actual/expect, manual implementation)
    - plugin transformed (Kotlin code with `@Adaptive` annotation)
    - no-code (loaded from fragment design)
- instructions
    - hard-coded
- relations
    - adat class - fragment (editor or view)
    - fragment - instructions (the ones the fragment understands)