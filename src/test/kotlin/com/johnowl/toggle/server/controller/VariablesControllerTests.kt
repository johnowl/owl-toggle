package com.johnowl.toggle.server.controller

import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class VariablesControllerTests {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Test
    fun `should add variables to userId`() {
        val variables = mapOf<String, Any>("name" to "John", "roles" to arrayListOf("beta", "admin"), "salary" to 100000)
        val expectedJson = "{\"name\":\"John\",\"roles\":[\"beta\",\"admin\"],\"salary\":100000}"

        val response = testRestTemplate.postForEntity("/variables/user123", variables, String::class.java)
        MatcherAssert.assertThat(response.statusCode, CoreMatchers.equalTo(HttpStatus.OK))
        MatcherAssert.assertThat(response.body, CoreMatchers.equalTo(expectedJson))
    }

    @Test
    fun `should get variables from userId`() {
        val variables = mapOf<String, Any>("name" to "John", "roles" to arrayListOf("beta", "admin"), "salary" to 100000)
        val expectedJson = "{\"name\":\"John\",\"roles\":[\"beta\",\"admin\"],\"salary\":100000}"

        testRestTemplate.postForEntity("/variables/user999", variables, String::class.java)
        val response = testRestTemplate.getForEntity("/variables/user999", String::class.java)

        MatcherAssert.assertThat(response.statusCode, CoreMatchers.equalTo(HttpStatus.OK))
        MatcherAssert.assertThat(response.body, CoreMatchers.equalTo(expectedJson))
    }
}