package `fun`.adaptive.general

abstract class AbstractObservable<VT> : Observable<VT> {

    override val listeners = mutableListOf<ObservableListener<VT>>()

}