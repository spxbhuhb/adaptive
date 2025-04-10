package `fun`.adaptive.code.g4.gir

class GirAtomElement(
    override val label : GirIdentifier?,
    override val additional : Boolean,
    override val options : List<GirOption>,
    override val suffix : GirSuffix,
    val atom: GirAtom
) : GirElement()