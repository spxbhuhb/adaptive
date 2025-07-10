# Value domain definition

A value domain definition is a structured Kotlin singleton or object that formally declares all 
identifiers—[markers](def://), [reference labels](def://), [value statuses](def://), [value trees](def://)—used
within a specific [value domain](def://) of an [Adaptive](def://) application.

The definition ensures:

- Centralized declaration of domain-specific constants.
- Type-safe and readable domain interaction throughout the codebase.
- Support for domain-aware utilities such as tree builders and structured value transformations.

It acts as the schema and DSL for a particular subdomain in the [value store](def://), 
encapsulating its semantic and structural identity.

## See also

- [value](def://)
- [Value](guide://)
- [Value store](guide://)
- [Value trees](guide://)
- [Value domain](guide://)