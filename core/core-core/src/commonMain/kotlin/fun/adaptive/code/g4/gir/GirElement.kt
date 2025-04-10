package `fun`.adaptive.code.g4.gir

abstract class GirElement {
    abstract val label : GirIdentifier?
    abstract val additional : Boolean
    abstract val options : List<GirOption>
    abstract val suffix : GirSuffix
}