package it.marcointroini.exceptions

class DBOperationException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)
