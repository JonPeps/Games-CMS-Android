package com.jonpeps.gamescms.data.serialization

import com.jonpeps.gamescms.data.serialization.string.StringFileStorageStrSerialisation
import com.jonpeps.gamescms.data.serialization.string.StringSerialization
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.BufferedReader
import java.io.File
import java.io.FileWriter

@OptIn(ExperimentalCoroutinesApi::class)
class StringFileStorageStrSerialisationTests {
    private val dispatcher = UnconfinedTestDispatcher()
    @MockK
    private lateinit var stringSerialization: StringSerialization
    @MockK
    private lateinit var bufferReader: BufferedReader
    @MockK
    private lateinit var fileWriter: FileWriter
    @MockK
    private lateinit var directory: File
    @MockK
    private lateinit var absoluteFile: File
    @MockK
    private lateinit var file: File
    @MockK
    private lateinit var securityException: SecurityException

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `WRITE to file FAILS WHEN directory DOES NOT EXIST`() = runTest(dispatcher) {
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization, dispatcher)
        assert(strFileStorageStrSerialisation.getErrorMsg() == "")

        commonFileReturnScenariosForWriteStr(
            directoryReturn = false,
            absFileExists = false,
            createFileReturn = false,
            makeDirectoryReturn = false,
        )
        val result = strFileStorageStrSerialisation.write(directory, file, absoluteFile, fileWriter, "test")
        assert(strFileStorageStrSerialisation.getErrorMsg()
                == StringFileStorageStrSerialisation.FAILED_TO_CREATE_DIRECTORY + directory.name)
        assert(!result)
    }

    @Test
    fun `WRITE to file FAILS WHEN directory CREATION FAILS`() = runTest(dispatcher) {
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization, dispatcher)
        assert(strFileStorageStrSerialisation.getErrorMsg() == "")

        commonFileReturnScenariosForWriteStr(
            directoryReturn = false,
            absFileExists = false,
            createFileReturn = false,
            makeDirectoryReturn = false,
        )
        val result = strFileStorageStrSerialisation.write(directory, file, absoluteFile, fileWriter, "test")
        assert(strFileStorageStrSerialisation.getErrorMsg()
                == StringFileStorageStrSerialisation.FAILED_TO_CREATE_DIRECTORY + directory.name)
        assert(!result)
    }

    @Test
    fun `WRITE to file FAILS WHEN main file MAKE directory FAILS`() = runTest(dispatcher) {
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization, dispatcher)
        assert(strFileStorageStrSerialisation.getErrorMsg() == "")

        commonFileReturnScenariosForWriteStr(
            directoryReturn = false,
            absFileExists = false,
            createFileReturn = false,
            makeDirectoryReturn = false,
        )
        val result = strFileStorageStrSerialisation.write(directory, file, absoluteFile, fileWriter, "test")
        assert(strFileStorageStrSerialisation.getErrorMsg()
                == StringFileStorageStrSerialisation.FAILED_TO_CREATE_DIRECTORY + directory.name)
        assert(!result)
    }

    @Test
    fun `WRITE to file SUCCESS WHEN CREATE new file is SUCCESS`() = runTest(dispatcher) {
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization, dispatcher)

        commonFileReturnScenariosForWriteStr(
            directoryReturn = false,
            absFileExists = false,
            createFileReturn = true,
            makeDirectoryReturn = true,
        )
        val result = strFileStorageStrSerialisation.write(directory, file, absoluteFile, fileWriter, "test")
        assert(result)
    }

    @Test
    fun `WRITE to file SUCCESS WHEN file DOES NOT EXIST`() = runTest(dispatcher) {
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization, dispatcher)
        assert(strFileStorageStrSerialisation.getErrorMsg() == "")

        commonFileReturnScenariosForWriteStr(
            directoryReturn = true,
            absFileExists = false,
            createFileReturn = true,
            makeDirectoryReturn = true
        )

        val result = strFileStorageStrSerialisation.write(directory, file, absoluteFile, fileWriter, "test")
        assert(result)
    }

    @Test
    fun `WRITE to file FAILS WHEN file CREATION FAILS`() = runTest(dispatcher) {
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization, dispatcher)
        assert(strFileStorageStrSerialisation.getErrorMsg() == "")

        commonFileReturnScenariosForWriteStr(
            directoryReturn = true,
            absFileExists = false,
            createFileReturn = false,
            makeDirectoryReturn = true)

        val result = strFileStorageStrSerialisation.write(directory, file, absoluteFile, fileWriter, "test")

        assert(strFileStorageStrSerialisation.getErrorMsg() == StringFileStorageStrSerialisation.FAILED_TO_CREATE_FILE + file.name)
        assert(!result)
    }

    @Test
    fun `WRITE to file SUCCESS WHEN file CREATION SUCCESS AS WELL AS it DOES NOT EXIST`() = runTest(dispatcher) {
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization, dispatcher)
        assert(strFileStorageStrSerialisation.getErrorMsg() == "")
        commonFileReturnScenariosForWriteStr(
            directoryReturn = true,
            absFileExists = true,
            createFileReturn = true,
            makeDirectoryReturn = true)
        val result = strFileStorageStrSerialisation.write(directory, file, absoluteFile, fileWriter, "test")
        assert(result)
    }

    @Test
    fun `WRITE to file FAILS WHEN directory CREATION FAILS due to an EXCEPTION thrown`() = runTest(dispatcher) {
        every { directory.name } returns ""
        every { directory.exists() } returns false
        every { securityException.message } returns ""
        every { directory.mkdir() } throws securityException
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization, dispatcher)
        val result = strFileStorageStrSerialisation.write(directory, file, absoluteFile, fileWriter, "test")
        assert(strFileStorageStrSerialisation.getErrorMsg() == securityException.message.toString())
        assert(!result)
    }

    @Test
    fun `READ SUCCESS WHEN buffer is populated`() = runTest(dispatcher) {
        every { stringSerialization.read(bufferReader) } returns true
        every { stringSerialization.getContents() } returns "test"
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization, dispatcher)
        val result = strFileStorageStrSerialisation.read(bufferReader)
        assert(result)
    }

    @Test
    fun `READ FAILS WHEN buffer is NOT populated`() = runTest(dispatcher) {
        every { stringSerialization.read(bufferReader) } returns false
        every { stringSerialization.getErrorMsg() } returns "An error occurred!"
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization, dispatcher)
        val result = strFileStorageStrSerialisation.read(bufferReader)
        assert(!result)
    }

    private fun commonFileReturnScenariosForWriteStr(directoryReturn: Boolean,
                                                     absFileExists: Boolean,
                                                     createFileReturn: Boolean,
                                                     makeDirectoryReturn: Boolean,
                                                     stringSerializationReturn: Boolean = true) {
        every { file.name } returns ""
        every { directory.name } returns ""
        every { directory.exists() } returns directoryReturn
        every { directory.mkdir() } returns makeDirectoryReturn
        every { absoluteFile.exists() } returns absFileExists
        every { absoluteFile.createNewFile() } returns createFileReturn
        every { stringSerialization.write(fileWriter, "test") } returns stringSerializationReturn
    }
}