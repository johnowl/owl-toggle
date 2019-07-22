package com.johnowl.toggle.server.controller

import com.johnowl.toggle.server.domain.VariablesService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/variables")
class VariablesController {

    private val service: VariablesService

    constructor(service: VariablesService) {
        this.service = service
    }

    @PostMapping("/{userId}")
    fun add(@PathVariable("userId") userId: String, @RequestBody values: Map<String, Any>) : ResponseEntity<Map<String, Any>> {
        return ResponseEntity(service.add(userId, values), HttpStatus.OK)
    }

    @GetMapping("/{userId}")
    fun add(@PathVariable("userId") userId: String) : ResponseEntity<Map<String, Any>> {
        return ResponseEntity(service.getBySessionId(userId), HttpStatus.OK)
    }

}