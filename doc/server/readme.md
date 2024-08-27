# Server

Adaptive provides a `backend` entry point (with `BackendAdapter`) and few backend fragments 
for building servers.

Built-in fragments store a so-called "implementation" which you typically pass to the fragment as 
a result of a builder function.

| Fragment | Class                                                                                                 | Module | Description                                                                           |
|----------|-------------------------------------------------------------------------------------------------------|--------|---------------------------------------------------------------------------------------|
| service  | [BackendService](/adaptive-core/src/commonMain/kotlin/fun/adaptive/backend/builtin/BackendService.kt) | core   | client request handler, new instance for each request                                 |
| worker   | [BackendWorker](/adaptive-core/src/commonMain/kotlin/fun/adaptive/backend/builtin/BackendWorker.kt)   | core   | background worker, one (or few) instances per backend                                 |
| store    | [BackendStore](/adaptive-core/src/commonMain/kotlin/fun/adaptive/backend/builtin/BackendStore.kt)     | core   | a store of data, you can use any kind of data storage (SQL, No-SQL, whatever, really) |

A server main looks like this (but of course you can use `backend` on clients as well).

Settings are also available in common code, but providers may be platform-dependent. See [Settings](settings.md) for details.

```kotlin
fun main() {
    
   backend {

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