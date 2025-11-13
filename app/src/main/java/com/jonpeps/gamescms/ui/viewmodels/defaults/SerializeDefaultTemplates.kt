package com.jonpeps.gamescms.ui.viewmodels.defaults

import android.content.res.AssetManager
import com.jonpeps.gamescms.data.helpers.InputStreamTableTemplate
import com.jonpeps.gamescms.data.repositories.MoshiTableTemplateStatusListRepository
import javax.inject.Inject

interface ISerializeDefaultTemplates {
    suspend fun serialize(assetPath: String,
                          templatesStatusFile: String,
                          templateNames: List<String>,
                          fileNames: List<String>)
}

class SerializeDefaultTemplates@Inject constructor(
    private val assetManager: AssetManager,
    private val inputStreamTableTemplate: InputStreamTableTemplate,
    private val moshiTableTemplateStatusListRepository: MoshiTableTemplateStatusListRepository
) : ISerializeDefaultTemplates {
    override suspend fun serialize(assetPath: String,
                                   templatesStatusFile: String,
                                   templateNames: List<String>,
                                   fileNames: List<String>) {
        // 1) Load a template, check if valid. Store result in list.
        // 2) Save template names to file to load in templates screen later.
        // 3) Save template status to file.

        
    }
}