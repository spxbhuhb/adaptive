# Adapter

Adapters should be closed boxes:
- we should be able to create as many adapters as we want in one application
- adapters in one application should not interfere with each other

Main adapter groups:
- backend adapters
- UI adapters
- test adapters

`AdaptiveAdapter.dispatcher` is set to:
- `Dispatchers.DEFAULT` for backend, test adapters and UI adapters when there is no main dispatcher
- `Dispatchers.MAIN` for UI adapters when applicable

`AdaptiveAdapter.scope` is a `CoroutineScope` created with the dispatcher of the adapter

`AdaptiveAdapter.transport`:
- a `ServiceCallTransport` that belongs to te adapter
- UI adapters use the transport of their backend adapter
- `AdaptiveAdapter.start` calls `AdaptiveAdapter.transport.start` in `AdaptiveAdapter.scope`
- `AdaptiveAdapter.stop` calls `AdaptiveAdapter.scope.cancel()` and that should stop the transport as well