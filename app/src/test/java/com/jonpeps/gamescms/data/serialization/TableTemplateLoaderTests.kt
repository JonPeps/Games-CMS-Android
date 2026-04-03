package com.jonpeps.gamescms.data.serialization

import com.jonpeps.gamescms.data.dataclasses.ItemType
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemMoshi
import com.jonpeps.gamescms.data.repositories.base.ISingleItemMoshiJsonRepository
import com.jonpeps.gamescms.ui.tabletemplates.serialization.TableTemplateLoader
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.ITableTemplateGroupVmChangesCache
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test

class TableTemplateLoaderTests {
    @MockK
    private lateinit var mockRepository: ISingleItemMoshiJsonRepository<TableTemplateItemListMoshi>
    @MockK
    private lateinit var mockCommonSerializationRepoHelper: CommonSerializationRepoHelper
    @MockK
    private lateinit var mockCache: ITableTemplateGroupVmChangesCache

    private lateinit var sut: TableTemplateLoader

    private val arg = TableTemplateItemListMoshi(
        templateName = "test",
        items = arrayListOf(
            TableTemplateItemMoshi(
                name = "test1",
                dataType = ItemType.STRING,
                isPrimary = true,
                value = "test1",
                editable = true,
                isSortKey = true
            )))

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        sut = TableTemplateLoader(
            mockRepository,
            mockCommonSerializationRepoHelper,
            mockCache
        )
    }

    @Test
    fun `PARSE VALUE maps values as expected`() {
        val result = sut.getParsedValue(arg)
        assert(result?.size == 1)
        assert(result?.get(0)?.name == "test1")
        assert(result?.get(0)?.dataType == ItemType.STRING)
        assert(result?.get(0)?.isPrimary == true)
        assert(result?.get(0)?.value == "test1")
        assert(result?.get(0)?.editable == true)
        assert(result?.get(0)?.isSortKey == true)
    }
}