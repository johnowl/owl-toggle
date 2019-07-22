package com.johnowl.toggle.server.controller


import com.johnowl.toggle.server.domain.FeatureToggle
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ToggleControllerOrderedTests {

	@Autowired
	lateinit var testRestTemplate: TestRestTemplate

	@Test
	fun `001 should return error 404 and error in body when feature toggle does not exist`() {
		val expectedJson = "{\"code\":\"toggle_not_found\",\"message\":\"Feature toggle not found.\"}"

		val response = testRestTemplate.getForEntity("/toggles/1", String::class.java)

		assertThat(response.statusCode, equalTo(HttpStatus.NOT_FOUND))
		assertThat(response.body, equalTo(expectedJson))
	}

	@Test
	fun `002 should return status 200 and feature toggle in body when insert valid feature toggle`() {
		val toggle = FeatureToggle("my_toggle", true, "")
		val expectedJson = "{\"toggleId\":\"my_toggle\",\"enabled\":true,\"rules\":\"\"}"

		val response = testRestTemplate.postForEntity("/toggles/", toggle, String::class.java)
		assertThat(response.statusCode, equalTo(HttpStatus.OK))
		assertThat(response.body, equalTo(expectedJson))
	}

	@Test
	fun `003 should return status 200 and feature toggle in body when get toggle by id`() {
		val expectedJson = "{\"toggleId\":\"my_toggle\",\"enabled\":true,\"rules\":\"\"}"

		val response = testRestTemplate.getForEntity("/toggles/my_toggle", String::class.java)
		assertThat(response.statusCode, equalTo(HttpStatus.OK))
		assertThat(response.body, equalTo(expectedJson))
	}

	@Test
	fun `004 should return status 200 and feature toggle in body when update toggle by id`() {
		val toggle = FeatureToggle("my_toggle", false, "true")
		val expectedJson = "{\"toggleId\":\"my_toggle\",\"enabled\":false,\"rules\":\"true\"}"

		val response = testRestTemplate.exchange("/toggles/my_toggle", HttpMethod.PUT, HttpEntity(toggle), String::class.java)
		assertThat(response.statusCode, equalTo(HttpStatus.OK))
		assertThat(response.body, equalTo(expectedJson))
	}

	@Test
	fun `005 should return status 200 and feature toggle list in body when get all`() {
		val expectedJson = "[{\"toggleId\":\"my_toggle\",\"enabled\":false,\"rules\":\"true\"}]"

		val response = testRestTemplate.getForEntity("/toggles/", String::class.java)
		assertThat(response.statusCode, equalTo(HttpStatus.OK))
		assertThat(response.body, equalTo(expectedJson))
	}

	@Test
	fun `006 should return status 200 and feature toggle in body when delete toggle by id`() {
		val expectedJson = "{\"toggleId\":\"my_toggle\",\"enabled\":false,\"rules\":\"true\"}"

		val response = testRestTemplate.exchange("/toggles/my_toggle", HttpMethod.DELETE, null, String::class.java)
		assertThat(response.statusCode, equalTo(HttpStatus.OK))
		assertThat(response.body, equalTo(expectedJson))
	}

	@Test
	fun `007 should return status 404 and json error in body when try to delete non existing toggle`() {
		val expectedJson = "{\"code\":\"toggle_not_found\",\"message\":\"Feature toggle not found.\"}"

		val response = testRestTemplate.exchange("/toggles/my_toggle999", HttpMethod.DELETE, null, String::class.java)
		assertThat(response.statusCode, equalTo(HttpStatus.NOT_FOUND))
		assertThat(response.body, equalTo(expectedJson))
	}

}
