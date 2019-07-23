package com.johnowl.toggle.server.domain

import org.springframework.stereotype.Service

@Service
class VariablesService  {

    val repository: VariablesRepository

    constructor(repository: VariablesRepository) {
        this.repository = repository
    }

    fun add(userId: String, variables: Map<String, Any>) : Map<String, Any> {
        return repository.add(userId, variables)
    }

    fun getByUserId(userId: String) : Map<String, Any>  {
        return repository.getByUserId(userId) ?: throw VariablesNotFoundException()
    }
}