package com.johnowl.toggle.server.domain

interface ToggleRepository {
    fun getById(toggleId: String): FeatureToggle?
    fun add(featureToggle: FeatureToggle): FeatureToggle
    fun getAll(): List<FeatureToggle>
    fun update(featureToggle: FeatureToggle): FeatureToggle
    fun delete(toggleId: String)
}