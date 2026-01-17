package com.jonpeps.gamescms.data

import com.jonpeps.gamescms.data.dataclasses.ItemType
import com.jonpeps.gamescms.ui.tabletemplates.serialization.SerializeTableTemplateHelpers
import org.junit.Test

class SerializeTableTemplateHelpersTests  {
    private val sut = SerializeTableTemplateHelpers()
    @Test
    fun `test getFilename`() {
        assert(sut.getFilename("test") == "test.json")
        assert(sut.getFilename("Test") == "test.json")
        assert(sut.getFilename("TEST") == "test.json")
        assert(sut.getFilename("Test File") == "test_file.json")
    }

    @Test
    fun `test validateTableTemplateValue`() {
        assert(sut.validateTableTemplateValue("1", ItemType.INT))
        assert(!sut.validateTableTemplateValue("a", ItemType.INT))
        assert(sut.validateTableTemplateValue("true", ItemType.BOOLEAN))
        assert(sut.validateTableTemplateValue("false", ItemType.BOOLEAN))
        assert(sut.validateTableTemplateValue("1", ItemType.BOOLEAN))
        assert(sut.validateTableTemplateValue("0", ItemType.BOOLEAN))
        assert(sut.validateTableTemplateValue("test", ItemType.STRING))
        assert(sut.validateTableTemplateValue("2026-12-25", ItemType.UTC_DATE))
        assert(!sut.validateTableTemplateValue("25-12-2026", ItemType.UTC_DATE))
        assert(!sut.validateTableTemplateValue("Not a date", ItemType.UTC_DATE))
    }
}