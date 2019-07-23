package com.johnowl.toggle.server.domain

import com.johnowl.rules.RulesEngine
import org.springframework.stereotype.Service

@Service
class RulesEngineService {

    private val engine = RulesEngine()

    fun check(expression: String, variables: Map<String, Any>) = engine.check(expression, variables)

}