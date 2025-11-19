package com.jonpeps.gamescms.data

class DataConstants {
    companion object {
        const val JSON_EXTENSION = ".json"
        const val MAIN_DIR = "/GamesCMSApp/"
        const val PROJECTS_DIR = "projects/"
        const val TEMPLATES_DIR = "templates/"
        const val FILE_EXTENSION = ".json"
        const val TEMPLATES_LIST_FILENAME = "table_templates_list.json"
        const val DEFAULT_TEMPLATES_STATUS_FILENAME = "default_templates_status.json"
        const val TEMPLATES_LIST_CACHE_NAME = "template_list"
        const val PROJECT_LIST_CACHE_NAME = "project_list"
        const val COMMON_SPLIT_PER_STRING_ITEM = ":"
    }

    class KnownScreens {
        companion object {
            const val START = "Start"
            const val PROJECTS = "Projects"
            const val TABLE_TEMPLATES = "Table Templates"

            const val ADD_DEFAULTS = "Add Defaults"
            const val INSERT_DEFAULT_PROJECTS = "Insert Default Project"
            const val INSERT_DEFAULT_TEMPLATES = "Insert Default Templates"
            const val INSERT_TEMPLATES = "Insert Templates"
        }

        class BundleKeys {
            companion object {
                const val EXT_STORAGE_PATH = "ext_storage_path"
                const val FILE_PATH_KEY = "file_path"
                const val CACHED_NAME_KEY = "cached_name"
                const val DEBUG_FILENAME_KEY = "debug_filename"
                const val COLOUR_R = "colour_r"
                const val COLOUR_G = "colour_g"
                const val COLOUR_B = "colour_b"
            }
        }
    }
}