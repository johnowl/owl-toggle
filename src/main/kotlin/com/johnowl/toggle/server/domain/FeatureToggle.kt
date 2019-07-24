package com.johnowl.toggle.server.domain

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "feature_toggle")
data class FeatureToggle(

    @Id
    @Column(name = "toggle_id")
    val toggleId: String,

    @Column
    val enabled: Boolean,

    @Column
    val rules: String
)