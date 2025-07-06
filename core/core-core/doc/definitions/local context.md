# Local context

Local contexts are [fragments](def://) that provide data for their descendant
[fragments](def://) in a [fragment trees](def://).

Each local context contains an arbitrary instance of data that descendant fragments 
can find, read and update by traversing up the [fragment tree](def://) until they find the
appropriate local context.

This technique makes it possible to avoid manually passing data through each fragment.
Fragments between the one that uses the context and the one that provides the context 
can be unaware of the existence of the context.

## See also

- [Local context](guide://)