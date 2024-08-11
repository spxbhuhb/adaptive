# Ktor Integration

## Bootstrap

**frontend**

Web:

```kotlin
withProtoWebSocketTransport()
// ----  OR  ---
withJsonWebSocketTransport()
```

Android/iOS:

```kotlin
withProtoWebSocketTransport("https://your.host")
// ----  OR  ---
withJsonWebSocketTransport("https://your.host")
```

**backend**

> [!NOTE]
>
> The `settings` block here lists the default values, you don't need it if you are using
> these defaults or if you load the settings from other sources.
>
> Sessions depend on the `adaptive-lib-auth` module, so you have to add that to your
> bootstrap as well. See [auth](../auth/readme.md) for details.
>

```kotlin
server {

    settings {
        inline(
            "KTOR_PORT" to 8080,
            "KTOR_WIREFORMAT" to "proto", // set this to "json" if you want JSON
            "KTOR_STATIC_DIR" to "./var/static",
            "KTOR_SERVICE_ROUTE" to "/adaptive/service-ws",
            "SESSION_CLIENT_ID_ROUTE to " /adaptive/client-id",
            "SESSION_COOKIE_NAME" to "ADAPTIVE_CLIENT_ID"
        )
    }

    auth()
    ktor()
}
```

## Points of interest

### KtorWorker

* a basic Ktor (with Netty) server which provides the following paths:
    * `/adaptive/client-id` - get a client ID (sets a cookie with a UUID)
  * `/adaptive/service-ws` - web socket connection for services
    * everything else is mapped to `./var/static`
* sets `defaultWireFormatProvider` to the specified format
* is very simple, you can easily make a copy and customize it for your needs

### ClientWebSocketServiceCallTransport

* handles the client side of the service calls
* uses `defaultWireFormatProvider` to encode/decode messages
* `withProtoWebSocketTransport` / `withJsonWebSocketTransport`
    * sets `defaultWireFormatProvider` to the appropriate format
    * creates a new transport and assigns it to `defaultServiceCallTransport`

### TransactionWebSocketServiceCallTransport

* handles the server side of the service calls
* uses the client id set by `Routing.clientId` to identify the client
* uses `defaultWireFormatProvider` to encode/decode messages
* uses `AdaptiveServerAdapter.serviceCache` to find the service for the calls