## Implementation Status

| Platform | Status                                                                                   |
|----------|------------------------------------------------------------------------------------------|
| JVM      | **experimental**                                                                         |
| JS       | **experimental**                                                                         |
| Android  | **unverified** probably works                                                            |
| iOS      | **not yet** `fourRandomInt` should be implemented, no idea if IR works on iOS or not.    |
| Native   | **not yet** `fourRandomInt` should be implemented, no idea if IR works on Native or not. |

## Getting started

If you refresh the plugin version you have to run `gradle clean` to force code refresh.

Compressed example:

```kotlin
// commonMain - client and server
interface HelloService : Service {
    suspend fun hello(myName : String) : String
}

// jsMain or jvmMain - client
val hello = getService<HelloService>()

// jvmMain - server
class HelloServiceImpl : HelloService, ServiceImpl {
    override suspend fun hello(myName: String): String {
      return "Hello $myName!"
    }
}

// testing - this one for jvmMain
fun main() {
    defaultServiceImplFactory += HelloServiceImpl()
    runBlocking {
        println(Hello.hello("World"))
    }
}
```

## Overview

When using services we work with:

* service definitions (the API)
* service consumers
* service implementations
* service transports

### Service Definitions

Service definitions describe the communication between the client and the server. They are pretty straightforward:
create an interface that extends `Service` and define the functions the service provides.

```kotlin
interface HelloService : Service {
    
    suspend fun hello(myName : String) : String

}
```

You can define local functions in service definitions. These are **NOT** sent to the service implementations on
the server side. In the example below `hello` is sent to the server, `helloWorld` is not. When you call `helloWorld`
it calls `hello` locally on the client side.

These are like API extensions which are implemented locally.

```kotlin
interface HelloService : Service {
    
    suspend fun hello(myName : String) : String

    suspend fun helloWorld() : String {
        return hello("World")
    }
}
```

### Service Consumers

Clients use the service by creating service consumers. Use the `getService` function to
get a consumer instance.

The compiler plugin generates all the code for the client side, you simply call the functions.

Definition:

```kotlin
val helloService = getService<HelloService>()
```

Call example (this uses the default service transport, more about that later):

```kotlin
fun main() {
    runBlocking {
        println(helloService.hello("World"))
    }
}
```

### Service Implementations

On the server side create a service implementation that does whatever this service should do:

```kotlin
class HelloServiceImpl : HelloService, ServiceImpl<HelloServiceImpl> {

    override suspend fun hello(myName: String): String {
        return "Hello $myName!"
    }

}
```

Register the service implementation during application startup, so the server knows that they are available.

Use [defaultServiceImplFactory](/adaptive-core/src/commonMain/kotlin/hu/simplexion/adaptive/services/globals.kt)
or implement your own way to store the services. These factories are used by the transports to find the service.

```kotlin
defaultServiceImplFactory += HelloServiceImpl()
```

It is very important that a new service implementation instance is created for each service call. This might seem
a bit of an overkill, but it makes the handling of the [service context](#Service-Context) very straightforward.

**IMPORTANT** This **DOES NOT WORK**. As each call gets a new instance `clicked` will be 0 all the time.

```kotlin
class ClickServiceImpl : ClickService, ServiceImpl<ClickServiceImpl> {
    
    val clicked = AtomicInteger(0)

    override suspend fun click(): Int {
        TODO("WRONG, THIS CODE DOES NOT WORK, RTFM")
        return clicked.incrementAndGet()
    }
}
```

Replace the code above with the one below, this one works:

```kotlin

class ClickServiceImpl : ClickService, ServiceImpl<ClickServiceImpl> {
    
    companion object {
        val clicked = AtomicInteger(0)
    }
    
    override suspend fun click(): Int {
        return clicked.incrementAndGet()
    }
    
}
```

#### Service Context

Most cases you need authorization and session data on the server side. Services provide a `serviceContext`.
This context may contain the identity of the user, along with other information.

Each service implementation call gets the service context which is reachable in the `serviceContext` property:

```kotlin
class HelloServiceImpl : HelloService, ServiceImpl<HelloServiceImpl> {

    override suspend fun hello(myName: String): String {
        if (serviceContext.isAnonymous) {
            return "Sorry, I can talk only with clients I know."
        } else {
            return "Hello $myName! Your user id is: ${serviceContext.owner}."
        }            
    }
    
    override suspend fun login(email : String, password : String) : String {
        if (authenticate(email, password)) serviceContext.owner = myId
    }

    override suspend fun logout() : String {
        serviceContext.owner = null
    }

}
```

Type of `serviceContext` is `ServiceContext`:

```kotlin
interface ServiceContext {
    val uuid: UUID<ServiceContext>
    var data : MutableMap<Any,Any?>
}
```

Your transport may use your choice of class for the actual service context. There is a `BasicServiceContext` which is the
barest implementation:

```kotlin
data class BasicServiceContext(
    override val uuid: UUID<ServiceContext> = UUID(),
    override var data : MutableMap<Any,Any?> = mutableMapOf()
) : ServiceContext
```

### Service Transports

Transports move the call arguments and the return values between the client and the server. The library uses Protocol Buffers as
transport format, but it does not really care about how the packets reach the other side.

There is a very basic transport implementation for Ktor in `adaptive-lib`.

#### Client Side

The [defaultServiceCallTransport](../adaptive-core/src/commonMain/kotlin/hu/simplexion/adaptive/services/globals.kt)
global variable contains the transport. Set this during application startup as the example shows below.

```kotlin
defaultServiceCallTransport = BasicWebSocketServiceCallTransport(
    window.location.hostname,
    window.location.port.toInt(),
    "/Adaptive/services"
).also {
    it.start()
}
```

Each service consumer has a `serviceCallTransport` property which may be set to use specific call transport for the service.

```kotlin
val service = getService<TestApi>().also { it.serviceCallTransport = LocalServiceCallTransport() }
```

[BasicWebSocketServiceCallTransport](../adaptive-lib/src/commonMain/kotlin/hu/simplexion/adaptive/ktor/BasicWebSocketServiceCallTransport.kt)
from `adaptive-lib` provides a basic web socket transport implementation for clients.

[LocalServiceCallTransport](../adaptive-core/src/commonMain/kotlin/hu/simplexion/adaptive/services/transport/LocalServiceCallTransport.kt)
from `adaptive-core` provides a basic web socket transport implementation for clients.

#### Server Side

[Routing.basicWebSocketServiceDispatcher](adaptive-service-ktor/src/jvmMain/kotlin/hu/simplexion/adaptive/service/ktor/server/basic.kt)
from `adaptive-service-ktor` provides a basic web socket dispatcher implementation for Ktor.

With other servers you can write your own service provider based on `basicWebSocketServiceDispatcher`.

Full example of basic server setup:

```kotlin
fun main() {
    embeddedServer(Netty, port = 8080, module = Application::module).start(wait = true)
}

fun Application.module() {

    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(20)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    defaultServiceImplFactory += HelloServiceImpl()

    routing {
        basicWebsocketServiceCallTransport("/adaptive/services")
    }
}
```

## Supported Data Types

Whatever [WireFormat](../wireformat/README.md) supports.