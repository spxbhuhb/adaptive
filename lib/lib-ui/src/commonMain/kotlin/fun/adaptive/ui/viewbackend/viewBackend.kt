package `fun`.adaptive.ui.viewbackend

import `fun`.adaptive.foundation.api.findContext
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.producer.Producer
import `fun`.adaptive.general.Observable
import kotlin.reflect.KClass

@Producer
fun <BACKEND_TYPE : Observable<BACKEND_TYPE>> viewBackend(
    backendClass : KClass<BACKEND_TYPE>,
    binding: AdaptiveStateVariableBinding<BACKEND_TYPE>? = null
): BACKEND_TYPE {
    checkNotNull(binding)

    val backend = binding.targetFragment.findContext(backendClass)
    checkNotNull(backend) { "cannot find backend for class: ${backendClass.simpleName}" }

    binding.targetFragment.addProducer(
        ViewBackendProducer(binding, backend)
    )

    return backend
}