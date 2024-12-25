package `fun`.adaptive.kotlin.writer.model

abstract class KwBody : KwElement, KwStatementContainer {

    override val statements: MutableList<KwStatement> = mutableListOf()

}