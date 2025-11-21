package com.jonpeps.gamescms.data.serialization

import com.jonpeps.gamescms.ui.tabletemplates.serialization.SerializeTableTemplateHelpers
import org.junit.Before
import org.junit.Test

class SerializeTableTemplateHelpersTests {
    private lateinit var sut: SerializeTableTemplateHelpers

    @Before
    fun setup() {
        sut = SerializeTableTemplateHelpers()
    }

    @Test
    fun `get file name from template name SUCCESS`() {
        val filename = sut.getFilename("test template")
        assert(filename == "test_template.json")
    }
}