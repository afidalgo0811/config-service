import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.springframework.boot") version "3.1.1"
  id("io.spring.dependency-management") version "1.1.0"
  kotlin("jvm") version "1.8.22"
  kotlin("plugin.spring") version "1.8.22"
  id("com.diffplug.spotless") version "6.19.0"
}

group = "com.afidalgo"

version = "0.0.1-SNAPSHOT"

description = "Provides functionality for centralizing the application configuration."

java { sourceCompatibility = JavaVersion.VERSION_17 }

repositories { mavenCentral() }

extra["springCloudVersion"] = "2022.0.3"

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.springframework.cloud:spring-cloud-config-server")
  implementation("org.yaml:snakeyaml:2.0")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
}

configure<com.diffplug.gradle.spotless.SpotlessExtension> {
  kotlin {
    // by default the target is every '.kt' and '.kts` file in the java sourcesets
    ktfmt() // has its own section below
  }
  kotlinGradle {
    target("*.gradle.kts")
    ktfmt()
  }
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootBuildImage>("bootBuildImage") {
  imageName.set(project.name)
  environment.set(environment.get() + mapOf("BP_JVM_VERSION" to "17"))
  docker {
    publishRegistry {
      username.set(project.findProperty("registryUsername").toString())
      password.set(project.findProperty("registryToken").toString())
      url.set(project.findProperty("registryUrl").toString())
    }
  }
}

dependencyManagement {
  imports {
    mavenBom(
        "org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
  }
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = freeCompilerArgs + "-Xjsr305=strict"
    jvmTarget = "17"
  }
}

tasks.withType<Test> { useJUnitPlatform() }