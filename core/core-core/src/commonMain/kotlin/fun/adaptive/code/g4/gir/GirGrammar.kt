package `fun`.adaptive.code.g4.gir

class GirGrammar(
    val type: String,
    val options : List<GirOption>,
    val imports: List<GirImport>,
    val tokens : List<GirIdentifier>,
    val channels : List<GirIdentifier>,
    val rules: List<GirRule>
)