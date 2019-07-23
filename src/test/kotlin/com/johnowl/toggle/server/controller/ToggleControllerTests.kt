package com.johnowl.toggle.server.controller

import com.johnowl.toggle.server.domain.FeatureToggle
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
import java.util.*


@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ToggleControllerTests {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Test
    fun `should return true when toggle is enabled and has variables to satisfy rule`() {

        val toggleId = UUID.randomUUID().toString()
        val toggle = FeatureToggle(toggleId, true, "Number(version) > 5")
        testRestTemplate.postForEntity("/toggles/", toggle, FeatureToggle::class.java)

        val variables = mapOf("version" to 10)

        val response = testRestTemplate.postForEntity("/toggles/$toggleId/check", variables, String::class.java)

        MatcherAssert.assertThat(response.statusCode, CoreMatchers.equalTo(HttpStatus.OK))
        MatcherAssert.assertThat(response.body, CoreMatchers.equalTo("true"))
    }

    @Test
    fun `should return false when toggle is enabled and has variables that does not satisfy rule`() {

        val toggleId = UUID.randomUUID().toString()
        val toggle = FeatureToggle(toggleId, true, "Number(version) > 5")
        testRestTemplate.postForEntity("/toggles/", toggle, FeatureToggle::class.java)

        val variables = mapOf("version" to 1)

        val response = testRestTemplate.postForEntity("/toggles/$toggleId/check", variables, String::class.java)

        MatcherAssert.assertThat(response.statusCode, CoreMatchers.equalTo(HttpStatus.OK))
        MatcherAssert.assertThat(response.body, CoreMatchers.equalTo("false"))
    }

    @Test
    fun `should return false when toggle is disabled and has variables to satisfy rule`() {

        val toggleId = UUID.randomUUID().toString()
        val toggle = FeatureToggle(toggleId, false, "Number(version) > 5")
        testRestTemplate.postForEntity("/toggles/", toggle, FeatureToggle::class.java)

        val variables = mapOf("version" to 1)

        val response = testRestTemplate.postForEntity("/toggles/$toggleId/check", variables, String::class.java)

        MatcherAssert.assertThat(response.statusCode, CoreMatchers.equalTo(HttpStatus.OK))
        MatcherAssert.assertThat(response.body, CoreMatchers.equalTo("false"))
    }

    @Test
    fun `should return true when toggle is enabled and has empty rules`() {

        val toggleId = UUID.randomUUID().toString()
        val toggle = FeatureToggle(toggleId, true, "")
        testRestTemplate.postForEntity("/toggles/", toggle, FeatureToggle::class.java)

        val variables = mapOf("version" to 1)

        val response = testRestTemplate.postForEntity("/toggles/$toggleId/check", variables, String::class.java)

        MatcherAssert.assertThat(response.statusCode, CoreMatchers.equalTo(HttpStatus.OK))
        MatcherAssert.assertThat(response.body, CoreMatchers.equalTo("true"))
    }

    @Test
    fun `should return true when check toggle by toggleId and userId and userId has variables`() {

        val toggleId = UUID.randomUUID().toString()
        val userId = UUID.randomUUID().toString()

        // save variables
        val variables = mapOf<String, Any>("name" to "John", "roles" to arrayListOf("beta", "admin"), "version" to 10)
        testRestTemplate.postForEntity("/variables/$userId", variables, String::class.java)

        // save rule
        val toggle = FeatureToggle(toggleId, true, "Number(version) > 5")
        testRestTemplate.postForEntity("/toggles/", toggle, FeatureToggle::class.java)

        // execute
        val response = testRestTemplate.getForEntity("/toggles/$toggleId/check/$userId", String::class.java)

        MatcherAssert.assertThat(response.statusCode, CoreMatchers.equalTo(HttpStatus.OK))
        MatcherAssert.assertThat(response.body, CoreMatchers.equalTo("true"))
    }

    @Test
    fun `should return false when check toggle by toggleId and userId and userId has variables that does not satify rule`() {

        val toggleId = UUID.randomUUID().toString()
        val userId = UUID.randomUUID().toString()

        // save variables
        val variables = mapOf<String, Any>("name" to "John", "roles" to arrayListOf("beta", "admin"), "version" to 1)
        testRestTemplate.postForEntity("/variables/$userId", variables, String::class.java)

        // save rule
        val toggle = FeatureToggle(toggleId, true, "Number(version) >= 3")
        testRestTemplate.postForEntity("/toggles/", toggle, FeatureToggle::class.java)

        // execute
        val response = testRestTemplate.getForEntity("/toggles/$toggleId/check/$userId", String::class.java)

        MatcherAssert.assertThat(response.statusCode, CoreMatchers.equalTo(HttpStatus.OK))
        MatcherAssert.assertThat(response.body, CoreMatchers.equalTo("false"))
    }
}