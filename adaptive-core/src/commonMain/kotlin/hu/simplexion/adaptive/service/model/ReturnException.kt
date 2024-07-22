package hu.simplexion.adaptive.service.model

/**
 * Exceptions that are not actual software errors but more like out-of-order return values
 * should extend this class to have proper logging.
 *
 * An example use case is AccessDenied which usually does not indicate an actual software error
 * and does not need investigation. In contrast, NullPointerException should be investigated.
 *
 * [ReturnException] classes are logged as INFO rather than WARN or ERROR.
 */
abstract class ReturnException(message: String? = null) : Exception(message)