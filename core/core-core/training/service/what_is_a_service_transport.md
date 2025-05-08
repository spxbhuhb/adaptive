---
title: What is a Service Transport
tags: [service, transport]
type: conceptual
---

# Summary

This guide explains what service transports are in Adaptive, how they function, 
and how they are configured on both client and server sides.

# Objective

Understand the role and behavior of service transports in Adaptive, including how they
move data and how to use WebSocket transports in both client and server environments.

# Key Concepts

Service transports are the mechanism used to exchange data between service consumers
(usually clients) and service providers (usually servers).

They are responsible for:

- Sending call arguments from the consumer to the provider.
- Returning results (or exceptions) from the provider to the consumer.
- Encoding and decoding data using a `WireFormat` like `Proto` or `Json`.

Service transports can be created manually but are usually handled automatically by 
Adaptive's application framework.

# Components and Configuration

## Client-Side (Browser)

WebSocket transports are the most common transport for browser-based clients.

### Example

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

# Notes

- Mismatched `WireFormat` configurations between client and server will prevent successful communication.
- While WebSocket is the most common transport type, others may be supported depending on the environment and modules used.
- Manual setup is optional in most Adaptive applications, but the ability to configure transports is important for debugging, custom deployment, or integration scenarios.

# See Also

- [How to Write a Service](how_to_write_a_service.md)
- [What is a Service Context](what_is_a_service_context.md)

# Conclusion

Service transports are a fundamental component of Adaptive’s service infrastructure. They handle the
bidirectional movement of service calls and responses, relying on consistent wire format encoding.
While usually managed automatically by the framework, understanding how to configure and override
them is useful for extending or troubleshooting client-server interactions.