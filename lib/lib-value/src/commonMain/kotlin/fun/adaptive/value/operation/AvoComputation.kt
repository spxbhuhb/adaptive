package `fun`.adaptive.value.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.value.AvComputeFun

@Adat
class AvoComputation<T>() : AvValueOperation() {

    var computation: AvComputeFun<T>? = null

}