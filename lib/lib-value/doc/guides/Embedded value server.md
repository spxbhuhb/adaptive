# Embedded value server

[EmbeddedValueServer](class://) creates an isolated value server, ready to use locally
without any server connection.

## Create and initialize

Use the [embeddedValueServer](function://EmbeddedValueServer) function to create a new
embedded value server:

[createExample](example://embedded_value_server)

Perform initialization of values by adding initialization steps to the
[initFun](parameter://embeddedValueServer) parameter of [embeddedValueServer](function://EmbeddedValueServer).

[initFun](parameter://embeddedValueServer) runs within a [compute context](def://) and
has access to all the value modification operations the context provides.

[initExample](example://embedded_value_server)

To create an embedded value server with [persistence](def://), pass a [value persistence provider](def://)
to the [embeddedValueServer](function://EmbeddedValueServer) function.

See [built-in persistence providers](guide://) for the available persistence providers.

[persistenceExample](example://embedded_value_server)

## Access

To access the [value worker](def://) inside an embedded value server, use the
[serverWorker](property://EmbeddedValueServer) property. This gives you full access
to the worker:

[accessExample](example://embedded_value_server)

Use the [execute](function://EmbeddedValueServer) shorthand to execute some code
with the worker. Note that this function is `suspend` and has a timeout of 5 seconds.

[executeExample](example://embedded_value_server)

## Testing with an embedded value server

You have two options
Use the [withEmbeddedValueServer](function://EmbeddedValueServer) function inside
`runTest` or any other suspend function to create an embedded value server for testing.

This makes writing unit tests that use values trivial.

[testExample](example://embedded_value_server)