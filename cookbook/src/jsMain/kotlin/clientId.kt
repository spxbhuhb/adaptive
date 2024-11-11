import io.ktor.client.fetch.*
import kotlinx.coroutines.await

suspend fun clientId() {
    fetch("/adaptive/client-id").await()
}