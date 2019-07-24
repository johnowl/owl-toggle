package com.johnowl.toggle.server.repository

import com.johnowl.toggle.server.domain.VariablesRepository
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.core.HashOperations
import org.springframework.stereotype.Repository
import javax.annotation.Resource

@Repository
@Profile("stage", "prod")
class RedisVariablesRepository : VariablesRepository {

    @Resource(name = "redisTemplate")
    private lateinit var operations: HashOperations<String, String, Map<String, Any>>

    companion object {
        private const val KEY = "variables"
    }

    override fun add(userId: String, variables: Map<String, Any>): Map<String, Any> {
        operations.put(KEY, userId, variables)
        return variables
    }

    override fun getByUserId(userId: String): Map<String, Any>? = operations.get(KEY, userId)
}