# Backend fragment implementation

Backend fragment implementations provide the actual application functionality for 
[backend](def://) [fragments](def://).

[BackendService](class://) and [BackendWorker](class://) are wrappers
around these implementations. They contain the boilerplate needed by the framework, so implementations
can focus on application logic.

- [BackendService](class://) wraps [service implementations](def://)
- [BackendWorker](class://) wraps [worker implementations](def://)