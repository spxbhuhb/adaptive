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
    fileName: String,
    packageName: String = "",
    block: KwFile.() -> Unit
): KwFile {
    val kwFile = KwFile(this, fileName, packageName)
    files += kwFile
    kwFile.block()
    return kwFile
}

fun KwFile.kwImport(
    symbol : KwSymbol,
    alias: String? = null
): KwImport {
    val kwImport = KwImport(symbol, alias)
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

fun KwProperty.kwInitializer(builder: KwStatementContainer.() -> KwExpression): KwFunction {
    val kwFunction = KwFunction("<init-$name>")
    initializer = kwFunction
    kwFunction.kwBlockBody { builder() }
    return kwFunction
}

fun KwProperty.kwGetter(builder: KwStatementContainer.() -> KwExpression): KwFunction {
    val kwFunction = KwFunction("<get-$name>")
    getter = kwFunction
    kwFunction.kwBlockBody { builder() }
    return kwFunction
}

fun KwFunction.kwBlockBody(builder: KwBlockBody.() -> Unit): KwBlockBody {
    val kwBlockBody = KwBlockBody()
    body = kwBlockBody
    kwBlockBody.builder()
    return kwBlockBody
}

fun KwFunction.kwExpressionBody(builder: KwStatementContainer.() -> KwExpression): KwExpressionBody {
    val kwExpressionBody = KwExpressionBody()
    kwExpressionBody.statements += kwExpressionBody.builder()
    body = kwExpressionBody
    return kwExpressionBody
}

fun kwSymbol(name: String) = KwSymbol(name)

fun KwStatementContainer.kwReturn(builder: KwStatementContainer.() -> KwExpression): KwReturn {
    val kwReturn = KwReturn(builder())
    statements += kwReturn
    return kwReturn
}

fun KwStatementContainer.kwGetValue(name: String, receiver: (() -> KwExpression)? = null): KwGetValue {
    val kwGetValue = KwGetValue(name, receiver?.invoke())
    statements += kwGetValue
    return kwGetValue
}

fun KwExpressionScope.kwGetValue(name: String, receiver: (() -> KwExpression)? = null): KwGetValue {
    val kwGetValue = KwGetValue(name, receiver?.invoke())
    return kwGetValue
}

fun kwGetObject(symbol: KwSymbol): KwGetObject {
    return KwGetObject(symbol)
}

fun kwGetObject(name: String): KwGetObject {
    return KwGetObject(KwSymbol(name))
}

fun KwStatementContainer.kwCall(symbol: KwSymbol, block: KwCall.() -> Unit = { }): KwCall {
    val kwCall = KwCall(symbol)
    kwCall.block()
    statements += kwCall
    return kwCall
}

fun KwExpressionScope.kwCall(symbol: KwSymbol, block: KwCall.() -> Unit = { }): KwCall {
    val kwCall = KwCall(symbol)
    kwCall.block()
    return kwCall
}

fun KwCall.kwValueArgument(name: String? = null, value: KwExpressionScope.() -> KwExpression): KwValueArgument {
    val kwValueArgument = KwValueArgument(name, value())
    valueArguments += kwValueArgument
    return kwValueArgument
}

fun kwConst(value: String): KwConst {
    val kwConst = KwConst(KwConstKind.String, value)
    return kwConst
}

fun KwStatementContainer.kwConst(value: Int): KwConst {
    val kwConst = KwConst(KwConstKind.Number, value.toString())
    statements += kwConst
    return kwConst
}

fun KwExpressionScope.kwConst(value: Int): KwConst {
    val kwConst = KwConst(KwConstKind.Number, value.toString())
    return kwConst
}

fun KwProperty.kwDelegation(delegate: KwExpressionBody.() -> Unit): KwDelegation {
    val kwDelegation = KwDelegation(this)
    kwDelegation.delegate.body = KwExpressionBody().apply { delegate() }
    initializer = kwDelegation
    return kwDelegation
}