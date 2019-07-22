package com.johnowl.toggle.server.domain

import org.springframework.stereotype.Service

@Service
class FeatureToggleService {

    private val toggleRepository: ToggleRepository

    constructor(toggleRepository: ToggleRepository) {
        this.toggleRepository = toggleRepository
    }

    fun getById(toggleId: String) = toggleRepository.getById(toggleId) ?: throw FeatureToggleNotFoundException()

    fun getAll() = toggleRepository.getAll()

    fun add(featureToggle: FeatureToggle): FeatureToggle {

        if(toggleRepository.getById(featureToggle.toggleId) != null) {
            throw FeatureToggleAlreadyExistsException()
        }

        return toggleRepository.add(featureToggle)
    }

    fun update(toggleId: String, featureToggle: FeatureToggle): FeatureToggle {

        if(toggleRepository.getById(featureToggle.toggleId) == null) {
            throw FeatureToggleNotFoundException()
        }

        val toggleWithId = featureToggle.copy(toggleId = toggleId)
        return toggleRepository.update(toggleWithId)
    }

    fun delete(toggleId: String): FeatureToggle {

        val toggle = toggleRepository.getById(toggleId) ?: throw FeatureToggleNotFoundException()

        toggleRepository.delete(toggleId)

        return toggle
    }


}