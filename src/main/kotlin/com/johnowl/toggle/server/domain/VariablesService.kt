package com.johnowl.toggle.server.domain

import org.springframework.stereotype.Service

@Service
class VariablesService  {

    val repository: VariablesRepository

    constructor(repository: VariablesRepository) {
        this.repository = repository
    }

    fun add(sessionId: String, variables: Map<String, Any>) : Map<String, Any> {
        return repository.add(sessionId, variables)
    }

    fun getBySessionId(sessionId: String) : Map<String, Any>  {
        return repository.getByUserId(sessionId) ?: throw VariablesNotFoundException()
    }
}