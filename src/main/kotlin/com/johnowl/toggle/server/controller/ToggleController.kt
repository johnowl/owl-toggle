package com.johnowl.toggle.server.controller

import com.johnowl.toggle.server.domain.FeatureToggle
import com.johnowl.toggle.server.domain.FeatureToggleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/toggles")
class ToggleController {

    private val toggleService: FeatureToggleService

    @Autowired
    constructor(toggleService: FeatureToggleService) {
        this.toggleService = toggleService
    }

    @GetMapping("/")
    fun getAll() : ResponseEntity<List<FeatureToggle>> {
        return ResponseEntity(toggleService.getAll(), HttpStatus.OK)
    }

    @GetMapping("/{toggleId}")
    fun getById(@PathVariable("toggleId") toggleId: String) : ResponseEntity<FeatureToggle> {
        return ResponseEntity(toggleService.getById(toggleId), HttpStatus.OK)
    }

    @PostMapping("/")
    fun add(@RequestBody featureToggle: FeatureToggle) : ResponseEntity<FeatureToggle> {
        return ResponseEntity(toggleService.add(featureToggle), HttpStatus.OK)
    }

    @PutMapping("/{toggleId}")
    fun update(@PathVariable("toggleId") toggleId: String, @RequestBody featureToggle: FeatureToggle) : ResponseEntity<FeatureToggle> {
        return ResponseEntity(toggleService.update(toggleId, featureToggle), HttpStatus.OK)
    }

    @DeleteMapping("/{toggleId}")
    fun update(@PathVariable("toggleId") toggleId: String) : ResponseEntity<FeatureToggle> {
        return ResponseEntity(toggleService.delete(toggleId), HttpStatus.OK)
    }

    @PostMapping("/{toggleId}/check")
    fun add(@PathVariable("toggleId") toggleId: String, @RequestBody variables: Map<String, Any>) : ResponseEntity<Boolean> {
        return ResponseEntity(toggleService.check(toggleId, variables), HttpStatus.OK)
    }

    @GetMapping("/{toggleId}/check/{userId}")
    fun add(@PathVariable("toggleId") toggleId: String, @PathVariable("userId") userId: String) : ResponseEntity<Boolean> {
        return ResponseEntity(toggleService.check(toggleId, userId), HttpStatus.OK)
    }
}