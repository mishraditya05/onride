plugins {
	java
	id("org.springframework.boot") version "4.1.1"
	id("io.spring.dependency-management") version "1.1.7"
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
	maven { url = uri("https://packages.confluent.io/maven/") }
}

extra["springCloudVersion"] = "2025.1.2"

dependencies {
	implementation("com.onride:onride-common-web:0.2.0")
	implementation("com.onride:onride-observability:0.1.0")
	implementation("com.onride:onride-event-schemas:0.1.0")
	implementation("com.onride:onride-grpc-contracts:0.1.0")
	implementation("net.devh:grpc-client-spring-boot-starter:3.1.0.RELEASE")
	implementation("org.springframework.boot:spring-boot-starter-kafka")
	implementation("io.confluent:kafka-avro-serializer:8.2.0")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.boot:spring-boot-starter-flyway")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.1.0")
	// Feign is no longer used for inter-service calls (migrated to gRPC), kept for reference
	// implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
	implementation("org.flywaydb:flyway-database-postgresql")
	implementation("org.mapstruct:mapstruct:1.6.3")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
	annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
	testImplementation("org.springframework.boot:spring-boot-starter-flyway-test")
	testImplementation("org.springframework.boot:spring-boot-starter-validation-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testCompileOnly("org.projectlombok:lombok")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testAnnotationProcessor("org.projectlombok:lombok")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
