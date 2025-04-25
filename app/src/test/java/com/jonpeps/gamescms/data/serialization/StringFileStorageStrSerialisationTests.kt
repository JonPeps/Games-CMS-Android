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
    fun `test write to file failure when directory doesn't exist`() = runTest(dispatcher) {
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
    fun `test write to file failure when directory creation fails`() = runTest(dispatcher) {
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
    fun `test write to file failure when main file make directory fails`() = runTest(dispatcher) {
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
    fun `test write to file success when create new file is achieved`() = runTest(dispatcher) {
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
    fun `test write to file success when file doesn't exist`() = runTest(dispatcher) {
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
    fun `test write to file failure when directory creation fails due to an exception thrown`() = runTest(dispatcher) {
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
    fun `read from file success when file exists`() = runTest(dispatcher) {
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization, dispatcher)
        assert(strFileStorageStrSerialisation.getErrorMsg() == "")
        every { directory.name } returns ""
        every { directory.exists() } returns true
        every { absoluteFile.exists() } returns true
        every { stringSerialization.read(bufferReader) } returns true
        every { stringSerialization.getContents() } returns "test"
        val result = strFileStorageStrSerialisation.read(absoluteFile, bufferReader)
        assert(strFileStorageStrSerialisation.getContents() == "test")
        assert(result)
    }

    @Test
    fun `read from file failure when absolute file does not exist`() = runTest(dispatcher) {
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization, dispatcher)
        assert(strFileStorageStrSerialisation.getErrorMsg() == "")
        every { absoluteFile.name } returns ""
        every { absoluteFile.exists() } returns false
        val result = strFileStorageStrSerialisation.read(absoluteFile, bufferReader)
        assert(strFileStorageStrSerialisation.getErrorMsg() == StringFileStorageStrSerialisation.FILE_DOES_NOT_EXIST + absoluteFile.name)
        assert(!result)
    }

    @Test
    fun `read from file failure when file doesn't exist`() = runTest(dispatcher) {
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization, dispatcher)
        assert(strFileStorageStrSerialisation.getErrorMsg() == "")
        every { file.name } returns ""
        every { absoluteFile.name } returns ""
        every { absoluteFile.exists() } returns false
        val result = strFileStorageStrSerialisation.read(absoluteFile, bufferReader)
        assert(strFileStorageStrSerialisation.getErrorMsg() == StringFileStorageStrSerialisation.FILE_DOES_NOT_EXIST + file.name)
        assert(!result)
    }

    @Test
    fun `read from file failure when file exist check throws an exception`() = runTest(dispatcher) {
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization, dispatcher)
        every { absoluteFile.name } returns ""
        every { securityException.message } returns ""
        every { absoluteFile.exists() } throws securityException
        val result = strFileStorageStrSerialisation.read(absoluteFile, bufferReader)
        assert(strFileStorageStrSerialisation.getErrorMsg() == securityException.message.toString())
        assert(!result)
    }

    @Test
    fun `read from file success when string serialisation read returns true`() = runTest(dispatcher) {
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization, dispatcher)
        every { absoluteFile.name } returns ""
        every { absoluteFile.exists() } returns true
        every { stringSerialization.read(bufferReader) } returns true
        every { stringSerialization.getContents() } returns "test"
        val result = strFileStorageStrSerialisation.read(absoluteFile, bufferReader)
        assert(result)
        assert(strFileStorageStrSerialisation.getContents() == "test")
    }

    @Test
    fun `read from file failure when string serialisation read returns false`() = runTest(dispatcher) {
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization, dispatcher)
        every { absoluteFile.name } returns ""
        every { absoluteFile.exists() } returns true
        every { stringSerialization.getErrorMsg() } returns ""
        every { stringSerialization.read(bufferReader) } returns false
        val result = strFileStorageStrSerialisation.read(absoluteFile, bufferReader)
        assert(!result)
        assert(strFileStorageStrSerialisation.getErrorMsg() == stringSerialization.getErrorMsg())
    }

    @Test
    fun `read from file failure when read file contents returns false`() = runTest(dispatcher) {
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization, dispatcher)
        assert(strFileStorageStrSerialisation.getErrorMsg() == "")
        val result = strFileStorageStrSerialisation.read(absoluteFile, bufferReader)
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