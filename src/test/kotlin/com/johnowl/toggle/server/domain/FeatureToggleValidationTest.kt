package com.johnowl.toggle.server.domain

import org.junit.Assert.assertFalse
import org.junit.Test

class FeatureToggleValidationTest {

    @Test
    fun `should return valid when toggleId is valid`() {
        val validation = FeatureToggleValidation()
        val result = validation.validate(FeatureToggle("Lorem-ipsum", false, ""))
        assertFalse(result.isNotValid())
    }

    @Test
    fun `should return invalid when toggleId is Empty`() {
        val validation = FeatureToggleValidation()
        val result = validation.validate(FeatureToggle("", false, ""))
        assert(result.isNotValid())
    }

    @Test
    fun `should return invalid when the field rules size is bigger than 2048`() {
        val validation = FeatureToggleValidation()
        val result = validation.validate(FeatureToggle("", false, "a".repeat(2049)))
        assert(result.isNotValid())
    }

    @Test
    fun `should return valid when the field rules size is equals 2048`() {
        val validation = FeatureToggleValidation()
        val result = validation.validate(FeatureToggle("", false, "a".repeat(2049)))
        assert(result.isNotValid())
    }
}