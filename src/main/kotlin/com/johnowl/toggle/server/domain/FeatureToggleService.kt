package com.johnowl.toggle.server.domain

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FeatureToggleService(
        private val toggleRepository: ToggleRepository,
        private val rulesEngineService: RulesEngineService,
        private val variablesService: VariablesService,
        private val toggleValidation: FeatureToggleValidation
) {
    fun getById(toggleId: String) = toggleRepository.getById(toggleId) ?: throw FeatureToggleNotFoundException()

    fun getAll() = toggleRepository.getAll()

    fun add(featureToggle: FeatureToggle): FeatureToggle {

        val result = toggleValidation.validate(featureToggle)
        if (result.isNotValid()) {
            throw ValidationException(result)
        }

        if (toggleRepository.getById(featureToggle.toggleId) != null) {
            throw FeatureToggleAlreadyExistsException()
        }

        return toggleRepository.add(featureToggle)
    }

    fun update(toggleId: String, featureToggle: FeatureToggle): FeatureToggle {

        val result = toggleValidation.validate(featureToggle)
        if (result.isNotValid()) {
            throw ValidationException(result)
        }

        if (toggleRepository.getById(featureToggle.toggleId) == null) {
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

    fun check(toggleId: String, variables: Map<String, Any>): Boolean {

        val toggle = this.getById(toggleId)

        if (!toggle.enabled)
            return false

        if (toggle.rules.isEmpty())
            return toggle.enabled

        return rulesEngineService.check(toggle.rules, variables)
    }

    fun check(toggleId: String, userId: String): Boolean {
        val variables = variablesService.getByUserId(userId)
        return check(toggleId, variables)
    }
}