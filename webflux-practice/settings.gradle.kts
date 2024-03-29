pluginManagement {
    repositories {
        gradlePluginPortal()

        maven { setUrl("https://repo.spring.io/milestone") } // Spring milestones
        maven { setUrl("https://repo.spring.io/snapshot") } // Spring snapshots
    }

    plugins {
        id("de.fayard.refreshVersions") version "0.51.0"
    }
}

plugins {
    id("de.fayard.refreshVersions")
}

rootProject.name = "webflux-practice"

include("practice:completable-future")
include("practice:completable-future")
findProject(":practice:completable-future")?.name = "completable-future"
include("practice:reactive-streams")
findProject(":practice:reactive-streams")?.name = "reactive-streams"
include("practice:spring-webflux")
findProject(":practice:spring-webflux")?.name = "spring-webflux"
