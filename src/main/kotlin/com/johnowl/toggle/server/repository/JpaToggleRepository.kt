package com.johnowl.toggle.server.repository

import com.johnowl.toggle.server.domain.FeatureToggle
import com.johnowl.toggle.server.domain.ToggleRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository

@Repository
@Profile("stage", "prod")
class JpaToggleRepository : ToggleRepository {

    private val jpaToggleRepositoryAdapter: JpaToggleRepositoryAdapter

    constructor(jpaToggleRepositoryAdapter: JpaToggleRepositoryAdapter) {
        this.jpaToggleRepositoryAdapter = jpaToggleRepositoryAdapter
    }

    override fun getById(toggleId: String) = jpaToggleRepositoryAdapter.findById(toggleId).orElse(null)
    override fun add(featureToggle: FeatureToggle) = jpaToggleRepositoryAdapter.save(featureToggle)
    override fun getAll(): List<FeatureToggle> = jpaToggleRepositoryAdapter.findAll()
    override fun update(toggle: FeatureToggle) = jpaToggleRepositoryAdapter.save(toggle)
    override fun delete(toggleId: String) = jpaToggleRepositoryAdapter.deleteById(toggleId)

}