# What is a service transport

[Service transports](def://) are the mechanism used to exchange data between service consumers
(usually clients) and service providers (usually servers).

They are responsible for:

- Sending call arguments from the consumer to the provider.
- Returning results (or exceptions) from the provider to the consumer.
- Encoding and decoding data using a `WireFormat` like `Proto` or `Json`.

## Creating service transports

[Service transports](def://) can be created manually but are usually handled automatically by
[lib-app](def://) or [lib-test](def://).

To create a [service transport](def://) manually, use the [getTransport](function://) function.

[getTransport](function://) chooses the actual transport implementation to use based on the schema
of the [url](parameter://getTransport):

- `direct` creates a [DirectServiceTransport](class://), useful for testing
- `http` creates a [ClientWebSocketServiceCallTransport](class://)
- `https` creates a [ClientWebSocketServiceCallTransport](class://)

Once created, you have to start the [start](function://ServiceCallTransport) to actually start the
transport.

[finish updating doc](todo://)

### Client-Side (Browser)

WebSocket transports are the most common transport for browser-based clients. 

```kotlin
webSocketTransport(window.location.origin, wireFormatProvider = Proto).also { it.start() }
```

### Behavior

- The transport connects to the server using WebSocket.
- It uses the specified `wireFormatProvider` (e.g., `Proto`) to encode and decode messages.
- The wire format used by the client must match the one expected by the server.

Most applications don’t need to instantiate this manually—Adaptive’s framework handles it by default.

## Server-Side (JVM with Ktor)

To enable WebSocket-based communication on the server, configure the Ktor module.

### Example

```kotlin
settings {
    inline(
        "KTOR_WIREFORMAT" to "proto",
        "KTOR_SERVICE_ROUTE" to "/adaptive/service-ws",
    )
}

jvmServer {
    module { KtorJvmServerModule() }
}
```

### Behavior

- `KtorJvmServerModule` initializes the WebSocket server endpoint.
- It listens on `/adaptive/service-ws` and matches clients using the specified wire format (`proto` in this case).
- You can configure the server using:
  - Inline settings (as shown),
  - Property files (`.properties`),
  - Or environment variables.

## Notes

- Mismatched `WireFormat` configurations between client and server will prevent successful communication.
- While WebSocket is the most common transport type, others may be supported depending on the environment and modules used.
- Manual setup is optional in most Adaptive applications, but the ability to configure transports is important for debugging, custom deployment, or integration scenarios.

## See Also

- [How to write a service](guide://)
- [What is a service context](guide://)