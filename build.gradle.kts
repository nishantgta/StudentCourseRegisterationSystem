plugins {
    id("java")
    id("jacoco")
}

group = "org.iitrpr"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.test{
    useJUnitPlatform()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation("org.postgresql:postgresql:42.5.3")
    implementation("com.opencsv:opencsv:5.7.1")
    testImplementation("org.jacoco:org.jacoco.agent:0.8.8")
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}
tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}