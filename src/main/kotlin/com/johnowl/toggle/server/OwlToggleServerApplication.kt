package com.johnowl.toggle.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.filter.CommonsRequestLoggingFilter



@SpringBootApplication
class OwlToggleServerApplication

fun main(args: Array<String>) {
    runApplication<OwlToggleServerApplication>(*args)
}
