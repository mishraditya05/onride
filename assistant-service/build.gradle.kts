plugins {
	java
	id("org.springframework.boot") version "4.1.1"
	id("io.spring.dependency-management") version "1.1.7"
	id("com.google.cloud.tools.jib") version "3.5.4"
}

group = "com.onride"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(26)
	}
}

repositories {
	mavenLocal()
	mavenCentral()
}

extra["springAiVersion"] = "2.0.1"
extra["springCloudVersion"] = "2025.1.2"

dependencies {
	implementation("com.onride:onride-common-web:0.2.0")
	implementation("com.onride:onride-observability:0.2.0")
	implementation("org.springframework.ai:spring-ai-starter-model-google-genai")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.1.0")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-validation-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testCompileOnly("org.projectlombok:lombok")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testAnnotationProcessor("org.projectlombok:lombok")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
		mavenBom("org.springframework.ai:spring-ai-bom:${property("springAiVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

jib {
	from {
		image = "eclipse-temurin:26-jre"
	}
	to {
		image = "docker.io/${project.findProperty("dockerHubUsername")}/onride-assistant-service"
		auth {
			username = project.findProperty("dockerHubUsername") as String?
			password = project.findProperty("dockerHubToken") as String?
		}
		tags = setOf("latest")
	}
}
