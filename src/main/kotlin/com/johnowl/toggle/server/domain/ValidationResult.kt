package com.johnowl.toggle.server.domain

import com.fasterxml.jackson.annotation.JsonIgnore

data class ValidationResult(val errors: List<ValidationError>) {
    @JsonIgnore
    fun isNotValid() = this.errors.isNotEmpty()
}