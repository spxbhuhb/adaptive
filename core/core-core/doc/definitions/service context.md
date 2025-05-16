# Service context

A service context provides information for [service functions](def://) about the context of
an actual call.

The service context contains:

- [session](def://) data (if exists, there is no [session](def://) when the caller is not signed in)
- connection lifecycle management hooks (cleanup functions)
- capabilities to call [service functions](def://) provided by the client

## See Also

- [service](def://)
- [service function](def://)
- [service implementation](def://)
- [service context](def://)