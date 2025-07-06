# Producer

Producers provide data for [fragments](def://) by setting an [internal state variable](def://).

The exact mechanism how the producer gets or creates the data depends on the implementation of
the producer. There are producers that repeatedly generate data, others fetch from some remote
data store, some produce a new value whenever an [observable](def://) updates.

Each producer is bound to an [internal state variable](def://). Whenever it produces a new
value, it sets the state variable and starts [patching](def://) the fragment.

## See also

- [fragment state](def://)
- [state variable](def://)
- [patching](def://)