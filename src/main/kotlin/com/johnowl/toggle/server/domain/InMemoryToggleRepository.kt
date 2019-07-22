package com.johnowl.toggle.server.domain

import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class InMemoryToggleRepository : ToggleRepository {

    private val db = ConcurrentHashMap<String, FeatureToggle>()

    override fun getById(toggleId: String) = db[toggleId]

    override fun add(featureToggle: FeatureToggle): FeatureToggle {
        db.put(featureToggle.toggleId, featureToggle)
        return featureToggle
    }

    override fun getAll() = db.values.toList()

    override fun update(toggle: FeatureToggle): FeatureToggle {
        db[toggle.toggleId] = toggle
        return toggle
    }

    override fun delete(toggleId: String) {
        db.remove(toggleId)
    }
}
