# Server

Adaptive provides a server-side adapter `AdaptiveServerAdapter` and few server side fragments for building servers.

There are a few important benefits of using Adaptive on the server side:

- lifecycle management is provided by Adaptive
- server settings are state variables
- state changes are applied to the server configuration

```kotlin
import hu.simplexion.z2.adaptive.adaptive

fun main() {
    adaptive {

        settings { environment() }
        settings { propertyFile("./etc/email.properties") }

        module("sql") {
            worker { HikariWorker() }
        }

        module("email") {
            table { EmailTable() }
            table { EmailQueueTable() }
            service { EmailService() }
            worker { EmailWorker() }
        }

    }
}

class HikariWorker : AdaptiveWorkerFragment<HikariWorker> {

    val driver = setting<String>()
    val url = setting<String>()
    val user = setting<String>()
    val password = setting<Secret>()

    fun start() {

    }

    fun stop() {

    }
}

class EmailTable : Table("email") {
    val name = text("name").uniqueIndex()
}

class EmailService : EmailApi, AdaptiveServiceImpl<EmailServiceImpl> {

}

class EmailWorker : AdaptiveWorkerImpl<EmailWorker> {

    val host by setting<String>()
    val port by setting<Int>()

    val emailTable by table<EmailTable>()
    val emailQueueTable by table<EmailQueueTable>()

    /* ... */
}
```