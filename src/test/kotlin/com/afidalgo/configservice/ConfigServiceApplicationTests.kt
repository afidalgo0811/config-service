package com.afidalgo.configservice

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.config.environment.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ConfigServiceApplicationTests {

  val restTemplate = RestTemplate()

  @Test fun contextLoads() {}

  @Test
  @Disabled("You can enable this test if your config-repo is publicly available")
  fun defaultConfigurationAvailable() {
    val responseEntity: ResponseEntity<Environment> =
        restTemplate.getForEntity("/application/default", Environment::class.java)
    assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.OK)
  }
}
