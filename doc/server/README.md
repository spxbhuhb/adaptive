# Server

Adaptive provides a server-side adapter `AdaptiveServerAdapter` and few server side fragments for building servers.

There are a few important benefits of using Adaptive on the server side:

- lifecycle management works out of the box
- server settings are state variables
- state changes are applied to the server configuration

```kotlin
fun main() {
    
    adaptive(AdaptiveServerAdapter()) {

        settings { 
            environment()
            propertyFile("./etc/email.properties") 
        }

        module("sql") {
            worker { HikariWorker() }
        }

        module("email") {
            store { EmailTable() }
            store { EmailQueueTable() }
            service { EmailService() }
            worker { EmailWorker() }
        }

    }
    
}
```