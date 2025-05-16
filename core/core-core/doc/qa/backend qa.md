# Question

How do I get a [worker implementation](def://) from a [service implementation](def://)?

# Answer

Use the [workerImpl](function://BackendFragmentImpl) function.

[GetWorkerImplFromServiceExample](example://backendExamples)

---

# Question

How do I get the id of a [session](def://) from a [service context](def://)?

# Answer

Use one of these properties:

- [sessionId](property://ServiceContext) - throws IllegalStateException when there is no session
- [sessionIdOrNull](property://ServiceContext) - returns with `null` when there is no session

[GetSessionIdExample](example://backendExamples)

---

# Question

How do I get a [worker implementation](def://) from another [worker implementation](def://)?

# Answer

Use the [workerImpl](function://BackendFragmentImpl) function.

[GetWorkerImplFromWorkerExample](example://backendExamples)
