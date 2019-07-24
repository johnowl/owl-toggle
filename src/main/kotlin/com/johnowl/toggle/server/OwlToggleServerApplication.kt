package com.johnowl.toggle.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OwlToggleServerApplication

fun main(args: Array<String>) {
    runApplication<OwlToggleServerApplication>(*args)
}