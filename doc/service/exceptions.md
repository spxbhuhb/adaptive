# Exceptions

## ReturnException

> [!NOTE]
> `ReturnException` is available on JVM only at the moment.
>

Exceptions that extend `ReturnException` are not actual software errors but more like out-of-order return values
should extend this class to have proper logging.

An example use case is `AccessDenied` which does not indicate an actual software error and does not need
investigation. In contrast, NullPointerException should be investigated.

`ReturnException` classes are logged as INFO rather than WARN or ERROR.