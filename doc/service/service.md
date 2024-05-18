## Getting started

When using services we work with:

* service APIs
* service consumers
* service implementations
* service transports

### Service APIs

Service APIs describe the communication between the client and the server. They are pretty straightforward:
create an interface, annotate it with `@ServiceApi` and define the functions the service provides:

```kotlin
@ServiceApi
interface CounterApi {

    suspend fun incrementAndGet() : Int

}
```

Service functions must be `suspend`.

You can define non-suspend functions in service APIs. Those are **NOT** sent to the service implementations on
the server side.

### Service Consumers

Use the `getService` function to get a service consumer instance. The compiler plugin generates all the code for the 
client side, you simply call the functions.

```kotlin
val counterService = getService<CounterApi>()
```

Call example (this uses the default service transport, more about that later):

```kotlin
@Adaptive
fun counter() {

    val counter = poll(1.seconds, 0) { counterService.incrementAndGet() }

    text("$counter")

}
```

### Service Implementations

On the server side create a service implementation that does whatever this service should do.

This service below calls uses a worker to increment and get the value of the counter.

```kotlin
class CounterService : CounterApi, ServiceImpl<CounterService> {

    val worker by worker<CounterWorker>()

    override suspend fun incrementAndGet(): Int {
        return worker.counter.incrementAndGet()
    }

}
```

Register the service during application startup, so the server knows that it is available.

See [server main](https://github.com/spxbhuhb/adaptive-example/blob/main/server/src/main/kotlin/Application.kt) for a complete example.

```kotlin
fun main() {
    server(wait = true) {
        service { CounterService() }
    }
}
```

> [!IMPORTANT]
> 
> A new service implementation instance is created for each service call. This might seem a bit of an overkill, but it
> makes the handling of the [service context](#Service-Context) very straightforward.
> 

> [!CAUTION]
> 
> **IMPORTANT** This **DOES NOT WORK**. As each call gets a new instance, `counter` will be 0 all the time.
> 
> ```kotlin
> class CounterService : CounterApi, ServiceImpl<CounterService> {
>     
>    val counter = AtomicInteger(0)
>
>    override suspend fun incrementAndGet(): Int {
>        TODO("WRONG, THIS CODE DOES NOT WORK, RTFM")
>        return counter.incrementAndGet()
>    }
> }
> ```

If you don't use a worker, use a companion object or other static structure. Don't forget to pay attention
to synchronization.

```kotlin
class CounterService : CounterApi, ServiceImpl<CounterService> {
    
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

> [!NOTE]
> 
> This example is a bit outdated and the service context functions are not merged into the project yet.
>

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

Transports move the call arguments and the return values between the client and the server. The library 
uses [WireFormat](../wireformat/wireformat.md) for transport.

There is a very basic transport implementation for Ktor in `adaptive-lib` which is automatically
included with `KtorWorker` and can be added on the client side with `withWebSocketTransport()`

## Supported Parameter and Return Types

Whatever [WireFormat](../wireformat/wireformat.md) supports.