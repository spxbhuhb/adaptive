# Application workspace

An application workspace is the runtime context or state of an [application](def://). It is
typically an instance of a class that is descendant of [AbstractWorkspace](class://).

Each [application](def://) (server and client) has a frontend application workspace and
a backend application workspace.

In servers the frontend application workspace is typically empty as they do not handle
actual [user interfaces](def://).

Clients typically have both a frontend and a backend workspace, the backend performing
data handling and update processing.

## See also

- [Application](guide://)
- [AbstractApplication](class://)
- [AbstractWorkspace](class://)
- [BackendWorkspace](class://)
- [FrontendWorkspace](class://)
- [MultiPaneWorkspace](class://)