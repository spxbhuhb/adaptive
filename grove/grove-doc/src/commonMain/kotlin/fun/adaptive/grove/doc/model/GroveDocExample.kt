package `fun`.adaptive.grove.doc.model

import `fun`.adaptive.adat.Adat

@Adat
class GroveDocExample(
    val name : String,
    val explanation : String,
    val repoPath : String,
    val fragmentKey : String,
    val fullCode : String,
    val exampleCode : String
)