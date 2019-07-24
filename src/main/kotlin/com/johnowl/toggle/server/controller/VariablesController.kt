package com.johnowl.toggle.server.controller

import com.johnowl.toggle.server.domain.VariablesService
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
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

    @ApiResponses(
            ApiResponse(code = 200, message = "Variables added successfully.")
    )
    @PostMapping("/{userId}")
    fun saveVariablesToUserId(@PathVariable("userId") userId: String, @RequestBody values: Map<String, Any>): ResponseEntity<Map<String, Any>> {
        return ResponseEntity(service.add(userId, values), HttpStatus.OK)
    }

    @ApiResponses(
            ApiResponse(code = 200, message = "Variables added successfully."),
            ApiResponse(code = 404, message = "Variables not bound.", response = Error::class)
    )
    @GetMapping("/{userId}")
    fun getVariablesByUserId(@PathVariable("userId") userId: String): ResponseEntity<Map<String, Any>> {
        return ResponseEntity(service.getByUserId(userId), HttpStatus.OK)
    }
}