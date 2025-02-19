package `fun`.adaptive.code.g4.gir

class GirBlockElement(
    override val label : GirIdentifier?,
    override val additional : Boolean,
    override val options : List<GirOption>,
    override val suffix : GirSuffix,
    val block: GirActionBlock,
    val predicateOptions : List<GirOption>
) : GirElement()