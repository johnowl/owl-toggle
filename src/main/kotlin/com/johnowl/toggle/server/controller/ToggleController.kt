package com.johnowl.toggle.server.controller

import com.johnowl.toggle.server.domain.FeatureToggle
import com.johnowl.toggle.server.domain.FeatureToggleService
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
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

    @ApiResponses(
            ApiResponse(code = 200, message = "Return all feature toggles or empty array.")
    )
    @GetMapping("/")
    fun getAll() : ResponseEntity<List<FeatureToggle>> {
        return ResponseEntity(toggleService.getAll(), HttpStatus.OK)
    }

    @ApiResponses(
            ApiResponse(code = 200, message = "Feature toggle returned successfully."),
            ApiResponse(code = 404, message = "Feature toggle not found.", response = Error::class)
    )
    @GetMapping("/{toggleId}")
    fun getById(@PathVariable("toggleId") toggleId: String) : ResponseEntity<FeatureToggle> {
        return ResponseEntity(toggleService.getById(toggleId), HttpStatus.OK)
    }

    @ApiResponses(
            ApiResponse(code = 200, message = "Feature toggle added successfully."),
            ApiResponse(code = 400, message = "Feature toggle already exists.", response = Error::class)
    )
    @PostMapping("/")
    fun add(@RequestBody featureToggle: FeatureToggle) : ResponseEntity<FeatureToggle> {
        return ResponseEntity(toggleService.add(featureToggle), HttpStatus.OK)
    }

    @ApiResponses(
            ApiResponse(code = 200, message = "Feature toggle added successfully."),
            ApiResponse(code = 404, message = "Feature toggle does not exist.", response = Error::class)
    )
    @PutMapping("/{toggleId}")
    fun update(@PathVariable("toggleId") toggleId: String, @RequestBody featureToggle: FeatureToggle) : ResponseEntity<FeatureToggle> {
        return ResponseEntity(toggleService.update(toggleId, featureToggle), HttpStatus.OK)
    }

    @ApiResponses(
            ApiResponse(code = 200, message = "Feature toggle added successfully."),
            ApiResponse(code = 404, message = "Feature toggle does not exist.", response = Error::class)
    )
    @DeleteMapping("/{toggleId}")
    fun delete(@PathVariable("toggleId") toggleId: String) : ResponseEntity<FeatureToggle> {
        return ResponseEntity(toggleService.delete(toggleId), HttpStatus.OK)
    }

    @ApiResponses(
            ApiResponse(code = 200, message = "Response successful."),
            ApiResponse(code = 404, message = "Feature toggle does not exist.", response = Error::class)
    )
    @PostMapping("/{toggleId}/check")
    fun checkToggle(@PathVariable("toggleId") toggleId: String, @RequestBody variables: Map<String, Any>) : ResponseEntity<Boolean> {
        return ResponseEntity(toggleService.check(toggleId, variables), HttpStatus.OK)
    }

    @ApiResponses(
            ApiResponse(code = 200, message = "Response successful."),
            ApiResponse(code = 404, message = "Feature toggle does not exist or variables not found.", response = Error::class)
    )
    @GetMapping("/{toggleId}/check/{userId}")
    fun checkToggleByUserId(@PathVariable("toggleId") toggleId: String, @PathVariable("userId") userId: String) : ResponseEntity<Boolean> {
        return ResponseEntity(toggleService.check(toggleId, userId), HttpStatus.OK)
    }
}