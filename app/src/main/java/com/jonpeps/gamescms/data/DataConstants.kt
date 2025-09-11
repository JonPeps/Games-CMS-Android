package com.jonpeps.gamescms.data

class DataConstants {
    companion object {
        const val JSON_EXTENSION = ".json"
        const val PROJECT_DIR = "/GamesCMSApp/"
        const val PROJECTS_DIR = "projects/"
        const val TEMPLATES_DIR = "templates/"
        const val TEMPLATES_LIST_CACHE_NAME = "template_list"
        const val PROJECT_LIST_CACHE_NAME = "project_list"
        const val MAIN_MENU_PROJECTS_ITEM = "Projects"
        const val MAIN_MENU_TEMPLATES_ITEM = "Templates"

        class Debug {
            companion object {
                const val DEBUG_LOAD = true
                const val DEBUG_TEMPLATES_LIST = "dummy_templates_list.json"
                const val DEBUG_PROJECTS_LIST = "dummy_projects_list.json"
            }
        }
    }
}