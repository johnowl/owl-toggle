package com.johnowl.toggle.server.domain

import java.lang.Exception

class ValidationException(val validationResult: ValidationResult) : Exception()