package com.johnowl.toggle.server.domain

interface VariablesRepository {
    fun add(userId: String, variables: Map<String, Any>) : Map<String, Any>
    fun getByUserId(userId: String) : Map<String, Any>?
}