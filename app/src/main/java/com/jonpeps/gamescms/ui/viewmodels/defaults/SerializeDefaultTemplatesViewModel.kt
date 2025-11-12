package com.jonpeps.gamescms.ui.viewmodels.defaults

import android.content.res.AssetManager
import androidx.lifecycle.ViewModel
import com.jonpeps.gamescms.data.viewmodels.InputStreamTableTemplateVm
import javax.inject.Inject

interface ISerializeDefaultTemplatesViewModel {
    fun serialize(names: List<String>, fileNames: List<String>)
}

class SerializeDefaultTemplatesViewModel@Inject constructor(
    private val assetManager: AssetManager,
    private val inputStreamTableTemplateVm: InputStreamTableTemplateVm
) :
    ViewModel(), ISerializeDefaultTemplatesViewModel {
    override fun serialize(names: List<String>, fileNames: List<String>) {

    }
}