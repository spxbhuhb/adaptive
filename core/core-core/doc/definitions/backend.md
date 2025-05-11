# Backend

Backends are parts of the applications that perform some background work. There are backends
**both** for clients and servers.

Server backends provide services for clients and may perform background tasks such as sending emails.

Client backends provide services for one actual client and run locally on that client. For example,
a browser client typically has a frontend, which handles the UI, and a backend that handles data
received asynchronously from the server.

In Adaptive terminology both client-side and server-side backends are called backends. The reason for
this is that they use the very same technology and structure (but different [fragments](def://)), no matter 
where they run.

## See Also

- [backend fragment implementation](def://)
- [BackendAdapter](class://)