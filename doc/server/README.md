# Server

Adaptive provides a `server` entry point (with `AdaptiveServerAdapter`) and few server side fragments 
for building servers.

Server is actually a bit of a misnomer because you can use these components anywhere, it is common code.

Built-in fragments store a so-called "implementation" which you typically pass to the fragment as 
a result of a builder function.

| Fragment | Class                                                                                                            | Module | Description                                                                           |
|----------|------------------------------------------------------------------------------------------------------------------|--------|---------------------------------------------------------------------------------------|
| service  | [AdaptiveService](/adaptive-core/src/commonMain/kotlin/hu/simplexion/adaptive/server/builtin/AdaptiveService.kt) | core   | client request handler, new instance for each request                                 |
| worker   | [AdaptiveWorker](/adaptive-core/src/commonMain/kotlin/hu/simplexion/adaptive/server/builtin/AdaptiveWorker.kt)   | core   | background worker, one (or few) instances per server                                  |
| store    | [AdaptiveStore](/adaptive-core/src/commonMain/kotlin/hu/simplexion/adaptive/server/builtin/AdaptiveStore.kt)     | core   | a store of data, you can use any kind of data storage (SQL, No-SQL, whatever, really) |

A server main looks like this (but of course you can use `server` anywhere).

Settings are also available in common code, but providers may be platform-dependent. See [Settings](settings.md) for details.

```kotlin
fun main() {
    
   server {

        settings { 
            environment()
            propertyFile("./etc/email.properties") 
        }

        worker { HikariWorker() }

        store { EmailTable() }
        store { EmailQueueTable() }
        service { EmailService() }
        worker { EmailWorker() }

    }
    
}
```