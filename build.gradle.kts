import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
	id("org.springframework.boot") version "2.3.4.RELEASE"
	id ("com.google.cloud.tools.jib") version ("1.8.0")
	kotlin("jvm") version "1.4.10"
	kotlin("plugin.spring") version "1.4.10"
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
	implementation("me.alidg:errors-spring-boot-starter:1.4.0")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
	testRuntimeOnly("com.h2database:h2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
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