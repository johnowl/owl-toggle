package com.johnowl.toggle.server.repository

import com.johnowl.toggle.server.domain.FeatureToggle
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface JpaToggleRepositoryAdapter : JpaRepository<FeatureToggle, String>