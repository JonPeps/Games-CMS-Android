package com.jonpeps.gamescms.ui.viewmodels.debug

import androidx.lifecycle.ViewModel

data class DebugScreenStatus(val success: Boolean, val exception: Exception?)

interface IDebugScreenViewModel {
    fun replenishDefaultTableTemplates()
    fun replenishDebugProjects()
}

class DebugScreenViewModel: ViewModel(), IDebugScreenViewModel {
    override fun replenishDefaultTableTemplates() {
        TODO("Not yet implemented")
    }

    override fun replenishDebugProjects() {
        TODO("Not yet implemented")
    }

}