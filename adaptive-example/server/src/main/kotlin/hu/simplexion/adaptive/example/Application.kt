package hu.simplexion.adaptive.example

import hu.simplexion.adaptive.base.adaptive
import hu.simplexion.adaptive.example.service.CounterService
import hu.simplexion.adaptive.example.worker.CounterWorker
import hu.simplexion.adaptive.ktor.KtorWorker
import hu.simplexion.adaptive.server.AdaptiveServerAdapter
import hu.simplexion.adaptive.server.builtin.service
import hu.simplexion.adaptive.server.builtin.worker

fun main() {

   adaptive(AdaptiveServerAdapter<Any>(true)) {

       service { CounterService() }
       worker { CounterWorker() }

       worker { KtorWorker() }

   }

}
