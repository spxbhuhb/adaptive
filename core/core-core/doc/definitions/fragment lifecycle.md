# Fragment lifecycle

Lifecycle of a [fragments](def://) refers to a series of function calls that
happens during the life of the fragment.

The fragment is created, mounted, patched (maybe many times), unmounted and lastly disposed.

Each of these steps has a corresponding function in [fragment](def://) classes. 
The [adapter](def://) or the parent [fragment](def://) calls these functions when needed.

# See also

- [fragment](def://)
- [adapter](def://)
- [lifecycle-bound](def://)
- [AdaptiveFragment](class://)
