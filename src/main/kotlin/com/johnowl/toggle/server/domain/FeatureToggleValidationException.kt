package com.johnowl.toggle.server.domain

import java.lang.Exception

class FeatureToggleValidationException(val validationResult: ValidationResult) : Exception()