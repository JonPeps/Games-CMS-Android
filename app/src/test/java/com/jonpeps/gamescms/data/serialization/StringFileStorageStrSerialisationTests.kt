package com.jonpeps.gamescms.data.serialization

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class StringFileStorageStrSerialisationTests {
    @Mock
    private lateinit var stringSerialization: StringSerialization
    @Mock
    private lateinit var strFileStorageWriteContents: IStrFileStorageWriteContents
    @Mock
    private lateinit var strFileStorageReadContents: IStrFileStorageReadContents
    @Mock
    private lateinit var file: File
    @Mock
    private lateinit var exception: Exception

    private lateinit var stringFileStorageStrSerialisation: StringFileStorageStrSerialisation

    @Before
    fun setup() {
        stringFileStorageStrSerialisation = StringFileStorageStrSerialisation(stringSerialization)
    }

    @Test
    fun `test write to file success when directory and file path don't initially exist`() {

    }
}