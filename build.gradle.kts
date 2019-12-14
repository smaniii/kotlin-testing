import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
	id("org.springframework.boot") version "2.2.2.RELEASE"
	id ("com.google.cloud.tools.jib") version ("1.8.0")
	kotlin("jvm") version "1.3.61"
	kotlin("plugin.spring") version "1.3.61"
}

apply(plugin = "io.spring.dependency-management")

springBoot {
	buildInfo {
		properties {
			artifact = "project"
			version = "0.0.1-SNAPSHOT"
			group = "com.start.up"
			name = "my project"
		}
	}
}

val developmentOnly by configurations.creating
configurations {
	runtimeClasspath {
		extendsFrom(developmentOnly)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.bouncycastle:bcprov-jdk15on:1.64")
	implementation("com.auth0:java-jwt:3.8.3")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
	testRuntimeOnly("com.h2database:h2")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
	testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.jibDockerBuild {
	setTargetImage("project")
}