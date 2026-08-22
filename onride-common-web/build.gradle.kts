plugins {
	`java-library`
	`maven-publish`
}

group = "com.onride"
version = "0.1.0"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(26)
	}
}

repositories {
	mavenCentral()
}


dependencies {
	compileOnly(platform("org.springframework.boot:spring-boot-dependencies:4.1.1"))

	compileOnly("org.springframework:spring-web")
	compileOnly("org.springframework:spring-webmvc")
	compileOnly("org.springframework.boot:spring-boot-autoconfigure")
	compileOnly("jakarta.servlet:jakarta.servlet-api")
	compileOnly("jakarta.validation:jakarta.validation-api")
	compileOnly("org.slf4j:slf4j-api")
}

publishing {
	publications {
		create<MavenPublication>("maven") {
			from(components["java"])
		}
	}
}