package com.jonpeps.gamescms.data.serialization

import com.jonpeps.gamescms.data.repositories.MoshiStringListRepository
import com.jonpeps.gamescms.data.repositories.MoshiTableTemplateRepository
import com.jonpeps.gamescms.ui.tabletemplates.serialization.ISerializeTableTemplateUpdateCore
import com.jonpeps.gamescms.ui.tabletemplates.serialization.SerializeTableTemplateHelpers
import com.jonpeps.gamescms.ui.tabletemplates.serialization.SerializeTableTemplateUpdateCore
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.Before

class SerializeTableTemplateUpdateCoreTests {
    @MockK
    private lateinit var serializeTableTemplateHelpers: SerializeTableTemplateHelpers
    @MockK
    private lateinit var stringListRepository: MoshiStringListRepository
    @MockK
    private lateinit var moshiTableTemplateRepository: MoshiTableTemplateRepository
    @MockK
    private lateinit var commonSerializationRepoHelper: CommonSerializationRepoHelper

    private lateinit var sut: ISerializeTableTemplateUpdateCore



    @Before
    fun setup() {
        MockKAnnotations.init(this)

        sut = SerializeTableTemplateUpdateCore(
            serializeTableTemplateHelpers,
            stringListRepository,
            moshiTableTemplateRepository,
            commonSerializationRepoHelper
        )
    }


}