package `fun`.adaptive.kotlin.writer

import `fun`.adaptive.kotlin.writer.model.*

const val ANONYMOUS = "<anonymous>"

fun kotlinWriter(block: KotlinWriter.() -> Unit) = KotlinWriter().apply(block)

fun KotlinWriter.kwModule(name: String = ANONYMOUS, block: KwModule.() -> Unit): KwModule {
    val kwModule = KwModule()
    kwModule.name = name
    modules += kwModule
    kwModule.block()
    return kwModule
}

fun KwModule.kwFile(
    name: String,
    pkg: String = "",
    block: KwFile.() -> Unit
): KwFile {
    val kwFile = KwFile(this, name, pkg)
    files += kwFile
    kwFile.block()
    return kwFile
}

fun KwFile.kwImport(
    name: String,
    alias: String? = null
): KwImport {
    val kwImport = KwImport(name, alias)
    imports += kwImport
    return kwImport
}

fun KwFile.kwImport(
    type : KwSymbol,
    alias: String? = null
): KwImport {
    val kwImport = KwImport(type.name, alias)
    imports += kwImport
    return kwImport
}


fun KwDeclarationContainer.kwObject(name: String, declarations: KwClass.() -> Unit): KwClass {
    val kwClass = KwClass(
        name = name,
        visibility = KwVisibility.PRIVATE,
        modality = KwModality.FINAL,
        kind = KwClassKind.OBJECT
    )
    kwClass.declarations()
    this.declarations += kwClass
    return kwClass
}

fun KwDeclarationContainer.kwProperty(name: String, builder: KwProperty.() -> Unit): KwProperty {
    val kwProperty = KwProperty(name)
    declarations += kwProperty
    kwProperty.builder()
    return kwProperty
}

fun KwDeclarationContainer.kwFunction(symbol: KwSymbol, builder: KwFunction.() -> Unit): KwFunction {
    val kwFunction = KwFunction(symbol.name)
    declarations += kwFunction
    kwFunction.builder()
    return kwFunction
}

fun kwLambda(body: KwBody.() -> Unit): KwFunction {
    val kwFunction = KwFunction(ANONYMOUS)
    kwFunction.body = KwBlockBody().apply { body() }
    return kwFunction
}

fun KwProperty.kwGetter(builder: KwBody.() -> KwExpression): KwFunction {
    val kwFunction = KwFunction("<get-$name>")
    getter = kwFunction
    kwFunction.kwBlockBody {
        statements += builder()
    }
    return kwFunction
}

fun KwFunction.kwBlockBody(builder: KwBlockBody.() -> Unit): KwBlockBody {
    val kwBlockBody = KwBlockBody()
    body = kwBlockBody
    kwBlockBody.builder()
    return kwBlockBody
}

fun KwFunction.kwExpressionBody(builder: KwBody.() -> KwExpression): KwExpressionBody {
    val kwExpressionBody = KwExpressionBody()
    kwExpressionBody.statements += kwExpressionBody.builder()
    body = kwExpressionBody
    return kwExpressionBody
}

fun kwSymbol(name: String) = KwSymbol(name)

fun KwStatementContainer.kwReturn(builder: () -> KwExpression): KwReturn {
    val kwReturn = KwReturn(builder())
    statements += kwReturn
    return kwReturn
}

fun kwGetValue(name: String, expression: () -> KwExpression): KwGetValue {
    return KwGetValue(name, expression())
}

fun kwGetObject(name: String): KwGetObject {
    return KwGetObject(name)
}

fun KwStatementContainer.kwCall(symbol: KwSymbol, block: KwCall.() -> Unit = { }): KwCall {
    val kwCall = KwCall(symbol)
    kwCall.block()
    statements += kwCall
    return kwCall
}

fun KwCall.kwCall(symbol: KwSymbol, block: KwCall.() -> Unit = { }): KwCall {
    val kwCall = KwCall(symbol)
    kwCall.block()
    return kwCall
}

fun KwCall.kwValueArgument(name: String? = null, value: () -> KwExpression): KwValueArgument {
    val kwValueArgument = KwValueArgument(name, value())
    valueArguments += kwValueArgument
    return kwValueArgument
}

fun kwConst(value: String): KwConst {
    val kwConst = KwConst(KwConstKind.String, value)
    return kwConst
}

fun KwProperty.kwDelegation(delegate: KwExpressionBody.() -> Unit): KwDelegation {
    val kwDelegation = KwDelegation(this)
    kwDelegation.delegate.body = KwExpressionBody().apply { delegate() }
    initializer = kwDelegation
    return kwDelegation
}