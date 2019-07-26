package com.johnowl.toggle.server.domain

import org.springframework.stereotype.Service

@Service
class FeatureToggleValidation {

    companion object {
        private const val TOGGLE_ID = "toggleId"
        private const val RULES = "rules"
    }

    fun validate(toggle: FeatureToggle): ValidationResult {
        val errors = mutableListOf<ValidationError>()

        if (toggle.toggleId.isBlank() || toggle.toggleId.isEmpty()) {
            errors.add(ValidationError(TOGGLE_ID, "Toggle id can't be blank."))
        }

        if (toggle.toggleId.length > 255) {
            errors.add(ValidationError(TOGGLE_ID, "Toggle id size can't be greater than 255 characters."))
        }

        if (toggle.rules.length > 2048) {
            errors.add(ValidationError(RULES, "Rules size can't be greater than 2048 characters."))
        }

        return ValidationResult(errors)
    }
}