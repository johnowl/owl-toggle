package com.johnowl.toggle.server.domain

data class FeatureToggle(
        val toggleId: String,
        val enabled: Boolean,
        val rules: String
) {
}