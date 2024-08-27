# Services

When using services we work with:

* service APIs
* service consumers
* service providers
* service transports

## Service APIs

Service APIs describe the communication between a service consumer and a service provider.

To write one:

- create an interface
- annotate it with `@ServiceApi`
- define **suspend** functions the service provides

```kotlin
@ServiceApi
interface CounterApi {

    suspend fun incrementAndGet() : Int

}
```

## Service Consumers

The `getService` function:

- returns with a service consumer instance
- the compiler plugin generates all the code for the consumer side
- you simply call the functions

```kotlin
val counterService = getService<CounterApi>()

val counter = counterService.incrementAndGet()

println(counter)
```

## Service Providers

Service providers:

- implement a service API
- extend `ServiceImpl`
- added with `service` to the backend fragment tree
- **are instantiated for each call** (see below)

```kotlin
class CounterService : CounterApi, ServiceImpl<CounterService> {

  companion object {
    val counter = AtomicInteger(0)
  }

    override suspend fun incrementAndGet(): Int {
      return counter.incrementAndGet()
    }

}
```

Register the service during application startup, so it is known.

```kotlin
fun main() {
    backend(wait = true) {
        service { CounterService() }
    }
}
```

> [!IMPORTANT]
>
> A new service provider instance is created for each service call. This might seem a bit of an overkill, but it
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

## Service Context

Each service provider call gets the service context which is reachable in the `serviceContext` property and
is an instance of `ServiceContext`.

`ServiceContext` instances

* store session data on the server side
  * ID of the session
  * owner of the session, is known (user ID)
  * roles of the owner

* provide functions for publish/subscribe patterns
  * `send` - sends a message on the connection
  * `connectionCleanup` - register a connection cleanup function
  * `sessionCleanup` - register a session cleanup function

```kotlin
class HelloService : HelloServiceApi, ServiceImpl<HelloService> {

    override suspend fun hello(myName: String): String {
      publicAccess()

      if (serviceContext.isLoggedIn) {
            return "Sorry, I can talk only with clients I know."
        } else {
            return "Hello $myName! Your user id is: ${serviceContext.owner}."
        }            
    }

}
```

## Service Transports

Service transports:

- move the call arguments from the consumer to the provider
- move the return value from the provider to the consumer
- use [WireFormat](../wireformat/README)

Built-in transports:

**WebSocket**

- check [Ktor](../ktor/readme.md) for examples

**Stream** (JVM only)

- [StreamServiceCallTransport](/adaptive-core/src/jvmMain/kotlin/fun/adaptive/service/transport/StreamServiceCallTransport.kt)
- uses Java input and output streams

**Test**

- [TestServiceTransport](/adaptive-core/src/commonMain/kotlin/fun/adaptive/service/testing/TestServiceContext.kt)
- call services of a supplied template or from an implementation factory

### Disconnecting

The service transports may close the connection at specific events such as login and logout. When this
happens, all pending calls fail with `DisconnectException`.

## Calling client side functions

You can call client side functions from the server if you have a `ServiceContext`:

```kotlin
getService<DuplexApi>(serviceContext).process(value)
```

> [!NOTE]
>
> Be careful, it is easy to create infinite loops by calling a service from a service
> implementation.

## Supported Parameter and Return Types

Whatever [WireFormat](../wireformat/README.md) supports.

## Exceptions

Exception handling depends on the transport implementation, see [Service Transports](#service-transports).

With the Ktor implementation, provider side exceptions result in:

* for [Adat](../adat/readme.md) exception instances, instance of the same class is thrown with the same data as on the server (also see note)
* for non-Adat classes `ServiceCallException` is thrown

> [!NOTE]
>
> If there is no wireformat registered for the given Adat class on the client side, `ServiceCallException` is thrown.
>

```kotlin
@Adat
class OddNumberException : Exception()

@ServiceApi
interface NumberApi {
  suspend fun ensureEven(i : Int, illegal : Boolean)
}

// ----  SERVER SIDE  --------

class NumberService : NumberApi, ServiceImpl<NumberService> {

  override suspend fun ensureEven(i : Int, illegal : Boolean) {
    publicAccess()
    if (i % 2 == 1) {
      if (illegal) throw IllegalArgumentException() else throw OddNumberException()
    }
  }

}

// ----  CLIENT SIDE  --------

suspend fun checkNumber(i : Int, illegal : Boolean) : String {
  try {
    getService<NumberApi>().ensureEven(i, illegal)
    return "this is an even number"
  } catch (ex : OddNumberException) {
    return "this is an odd number"
  } catch (ex : ServiceCallException) {
    return "ServiceCallException"
  }
}
```

## Logging

The default server side transport implementation logs service access and service errors. These can be configured
in `logback.xml` as the example shows below. For a full example, check [logback.xml](../../site/src/jvmMain/resources/logback.xml).

Exceptions that extend `ReturnException` are logged as INFO rather than WARN or ERROR.

These are not actual software errors but more like out-of-order return values.

An example use case is `AccessDenied` which does not indicate an actual software error and does not need
investigation. In contrast, `NullPointerException` should be investigated.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!--
  ~ Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
-->
<configuration
        xmlns="http://ch.qos.logback/xml/ns/logback"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://ch.qos.logback/xml/ns/logback xsd/logback.xsd">
  
    <appender name="ErrorLogFile" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- appender configuration -->
    </appender>

    <appender name="AccessLogFile" class="ch.qos.logback.core.rolling.RollingFileAppender">
      <!-- appender configuration -->
    </appender>

    <root level="WARN">
        <appender-ref ref="ErrorLogFile"/>
    </root>

    <logger name="fun.adaptive.service.ServiceAccessLog" level="INFO" additivity="false">
        <appender-ref ref="AccessLogFile"/>
    </logger>
  
</configuration>
```