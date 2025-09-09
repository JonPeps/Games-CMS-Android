package com.jonpeps.gamescms.data

class DataConstants {
    companion object {
        const val JSON_EXTENSION = ".json"
        const val PROJECT_DIR = "/GamesCMSApp/"
        const val PROJECTS_DIR = "projects/"
        const val PROJECT_LIST_CACHE_NAME = "project_list"
        const val PROJECTS_LIST = PROJECT_LIST_CACHE_NAME + JSON_EXTENSION

        class Debug {
            companion object {
                const val DEBUG_LOAD = false
                const val DEBUG_PROJECTS_LIST = "dummy_projects_list.json"
            }
        }
    }
}