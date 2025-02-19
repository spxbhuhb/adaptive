package `fun`.adaptive.code.g4.gir

class GirParserRule(
    val name : GirIdentifier,
    val modifiers : List<GirRuleModifier>,
    val actionBlock : GirActionBlock?,
    val returns : GirActionBlock?,
    val throws : List<GirIdentifier>?,
    val locals : GirActionBlock?,
    val options : List<GirOption>?,
    val actions : List<GirAction>?,
    val block : GirRuleBlock,
    val exceptionGroup : GirExceptionGroup?
)