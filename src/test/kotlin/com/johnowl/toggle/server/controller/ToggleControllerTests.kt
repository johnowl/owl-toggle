package com.johnowl.toggle.server.controller

import com.johnowl.toggle.server.domain.FeatureToggle
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
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

        testRestTemplate.exchange("/toggles/$toggleId", HttpMethod.DELETE, null, String::class.java)
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
        testRestTemplate.exchange("/toggles/$toggleId", HttpMethod.DELETE, null, String::class.java)
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
        testRestTemplate.exchange("/toggles/$toggleId", HttpMethod.DELETE, null, String::class.java)
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
        testRestTemplate.exchange("/toggles/$toggleId", HttpMethod.DELETE, null, String::class.java)
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
        testRestTemplate.exchange("/toggles/$toggleId", HttpMethod.DELETE, null, String::class.java)
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
        testRestTemplate.exchange("/toggles/$toggleId", HttpMethod.DELETE, null, String::class.java)
        MatcherAssert.assertThat(response.statusCode, CoreMatchers.equalTo(HttpStatus.OK))
        MatcherAssert.assertThat(response.body, CoreMatchers.equalTo("false"))
    }

    @Test
    fun `should return error 404 and error in body when feature toggle does not exist`() {
        val toggleId = UUID.randomUUID().toString()
        val expectedJson = "{\"code\":\"toggle_not_found\",\"message\":\"Feature toggle not found.\"}"

        val response = testRestTemplate.getForEntity("/toggles/$toggleId", String::class.java)

        MatcherAssert.assertThat(response.statusCode, CoreMatchers.equalTo(HttpStatus.NOT_FOUND))
        MatcherAssert.assertThat(response.body, CoreMatchers.equalTo(expectedJson))
    }

    @Test
    fun `should return status 200 and feature toggle in body when insert valid feature toggle`() {
        val toggleId = UUID.randomUUID().toString()
        val toggle = FeatureToggle(toggleId, true, "")
        val expectedJson = "{\"toggleId\":\"$toggleId\",\"enabled\":true,\"rules\":\"\"}"

        val response = testRestTemplate.postForEntity("/toggles/", toggle, String::class.java)
        testRestTemplate.exchange("/toggles/$toggleId", HttpMethod.DELETE, null, String::class.java)
        MatcherAssert.assertThat(response.statusCode, CoreMatchers.equalTo(HttpStatus.OK))
        MatcherAssert.assertThat(response.body, CoreMatchers.equalTo(expectedJson))
    }

    @Test
    fun `should return status 200 and feature toggle in body when get toggle by id`() {
        val toggleId = UUID.randomUUID().toString()
        val toggle = FeatureToggle(toggleId, true, "")
        val expectedJson = "{\"toggleId\":\"$toggleId\",\"enabled\":true,\"rules\":\"\"}"
        testRestTemplate.postForEntity("/toggles/", toggle, String::class.java)

        val response = testRestTemplate.getForEntity("/toggles/$toggleId", String::class.java)
        testRestTemplate.exchange("/toggles/$toggleId", HttpMethod.DELETE, null, String::class.java)
        MatcherAssert.assertThat(response.statusCode, CoreMatchers.equalTo(HttpStatus.OK))
        MatcherAssert.assertThat(response.body, CoreMatchers.equalTo(expectedJson))
    }

    @Test
    fun `should return status 200 and feature toggle in body when update toggle by id`() {
        val toggleId = UUID.randomUUID().toString()
        testRestTemplate.postForEntity("/toggles/", FeatureToggle(toggleId, false, ""), String::class.java)

        val toggle = FeatureToggle(toggleId, false, "true")
        val expectedJson = "{\"toggleId\":\"$toggleId\",\"enabled\":false,\"rules\":\"true\"}"

        val response = testRestTemplate.exchange("/toggles/$toggleId", HttpMethod.PUT, HttpEntity(toggle), String::class.java)
        testRestTemplate.exchange("/toggles/$toggleId", HttpMethod.DELETE, null, String::class.java)
        MatcherAssert.assertThat(response.statusCode, CoreMatchers.equalTo(HttpStatus.OK))
        MatcherAssert.assertThat(response.body, CoreMatchers.equalTo(expectedJson))
    }

    @Test
    fun `should return status 200 and feature toggle list in body when get all`() {
        val toggleId = UUID.randomUUID().toString()
        val toggle = FeatureToggle(toggleId, false, "true")
        val expectedJson = "[{\"toggleId\":\"$toggleId\",\"enabled\":false,\"rules\":\"true\"}]"
        testRestTemplate.postForEntity("/toggles/", toggle, String::class.java)

        val response = testRestTemplate.getForEntity("/toggles/", String::class.java)
        testRestTemplate.exchange("/toggles/$toggleId", HttpMethod.DELETE, null, String::class.java)
        MatcherAssert.assertThat(response.statusCode, CoreMatchers.equalTo(HttpStatus.OK))
        MatcherAssert.assertThat(response.body, CoreMatchers.equalTo(expectedJson))
    }

    @Test
    fun `should return status 200 and feature toggle in body when delete toggle by id`() {
        val toggleId = UUID.randomUUID().toString()
        val toggle = FeatureToggle(toggleId, false, "true")
        testRestTemplate.postForEntity("/toggles/", toggle, String::class.java)

        val expectedJson = "{\"toggleId\":\"$toggleId\",\"enabled\":false,\"rules\":\"true\"}"

        val response = testRestTemplate.exchange("/toggles/$toggleId", HttpMethod.DELETE, null, String::class.java)
        MatcherAssert.assertThat(response.statusCode, CoreMatchers.equalTo(HttpStatus.OK))
        MatcherAssert.assertThat(response.body, CoreMatchers.equalTo(expectedJson))
    }

    @Test
    fun `should return status 404 and json error in body when try to delete non existing toggle`() {
        val toggleId = UUID.randomUUID().toString()
        val expectedJson = "{\"code\":\"toggle_not_found\",\"message\":\"Feature toggle not found.\"}"

        val response = testRestTemplate.exchange("/toggles/$toggleId", HttpMethod.DELETE, null, String::class.java)
        MatcherAssert.assertThat(response.statusCode, CoreMatchers.equalTo(HttpStatus.NOT_FOUND))
        MatcherAssert.assertThat(response.body, CoreMatchers.equalTo(expectedJson))
    }
}