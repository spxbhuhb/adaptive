# Visitors and transforms

For many tree-like structures such as JSON, Markdown and such, Adaptive provides visitors and transforms
to traverse and transform the tree.

These follow the same concept I've shamelessly copied from the Kotlin compiler.

For example, JSON offers these classes:

* [JsonVisitor](class://)
* [JsonTransformer](class://)
* [JsonTransformerVoid](class://)
* [JsonTransformerVoidWithContext](class://)

The first three are typical for all data structures that support the visitor/transformer pattern, the last
is specific as the context is usually domain-dependent.

## Example

[visitor and transform example](todo://)