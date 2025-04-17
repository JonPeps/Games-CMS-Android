package com.jonpeps.gamescms.data.serialization

import com.jonpeps.gamescms.data.serialization.string.StringFileStorageStrSerialisation
import com.jonpeps.gamescms.data.serialization.string.StringSerialization
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.io.BufferedReader
import java.io.File
import java.io.FileWriter

@RunWith(MockitoJUnitRunner::class)
class StringFileStorageStrSerialisationTests {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()
    @Mock
    private lateinit var stringSerialization: StringSerialization
    @Mock
    private lateinit var bufferReader: BufferedReader
    @Mock
    private lateinit var fileWriter: FileWriter
    @Mock
    private lateinit var directory: File
    @Mock
    private lateinit var absoluteFile: File
    @Mock
    private lateinit var file: File
    @Mock
    private lateinit var securityException: SecurityException

    @Test
    fun `test write to file failure when directory doesn't exist`() = runTest(dispatcher) {
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization, dispatcher)
        assert(strFileStorageStrSerialisation.getErrorMsg() == "")

        commonFileReturnScenariosForWriteStr(
            directoryReturn = false,
            fileReturn = false,
            createFileReturn = false,
            makeDirectoryReturn = false,
        )
        val result = strFileStorageStrSerialisation.write(directory, file, fileWriter, "test")
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
            fileReturn = false,
            createFileReturn = false,
            makeDirectoryReturn = false,
        )
        val result = strFileStorageStrSerialisation.write(directory, file, fileWriter, "test")
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
            fileReturn = false,
            createFileReturn = false,
            makeDirectoryReturn = false,
        )
        val result = strFileStorageStrSerialisation.write(directory, file, fileWriter, "test")
        assert(strFileStorageStrSerialisation.getErrorMsg()
                == StringFileStorageStrSerialisation.FAILED_TO_CREATE_DIRECTORY + directory.name)
        assert(!result)
    }

    @Test
    fun `test write to file success when create new file is achieved`() = runTest(dispatcher) {
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization, dispatcher)
        `when`(directory.name).thenReturn("")
        `when`(file.name).thenReturn("")

        commonFileReturnScenariosForWriteStr(
            directoryReturn = false,
            fileReturn = false,
            createFileReturn = true,
            makeDirectoryReturn = true,
        )
        val result = strFileStorageStrSerialisation.write(directory, file, fileWriter, "test")
        assert(result)
    }

    @Test
    fun `test write to file success when file doesn't exist`() = runTest(dispatcher) {
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization, dispatcher)
        assert(strFileStorageStrSerialisation.getErrorMsg() == "")

        commonFileReturnScenariosForWriteStr(
            directoryReturn = true,
            fileReturn = true,
            createFileReturn = true,
            makeDirectoryReturn = true,
        )
        `when`(stringSerialization.write(fileWriter, "test")).thenReturn(true)
        val result = strFileStorageStrSerialisation.write(directory, file, fileWriter, "test")
        assert(result)
    }

    @Test
    fun `test write to file failure when directory creation fails due to an exception thrown`() = runTest(dispatcher) {
        `when`(directory.exists()).thenReturn(false)
        `when`(directory.mkdir()).thenThrow(securityException)
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization, dispatcher)
        val result = strFileStorageStrSerialisation.write(directory, file, fileWriter, "test")
        assert(strFileStorageStrSerialisation.getErrorMsg() == securityException.message.toString())
        assert(!result)
    }

    @Test
    fun `read from file success when file exists`() = runTest(dispatcher) {
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization, dispatcher)
        assert(strFileStorageStrSerialisation.getErrorMsg() == "")
        `when`(absoluteFile.exists()).thenReturn(true)
        `when`(stringSerialization.read(bufferReader)).thenReturn(true)
        `when`(stringSerialization.getContents()).thenReturn("test")
        val result = strFileStorageStrSerialisation.read(absoluteFile, bufferReader)
        assert(strFileStorageStrSerialisation.getContents() == "test")
        assert(result)
    }

    @Test
    fun `read from file failure when absolute file does not exist`() = runTest(dispatcher) {
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization, dispatcher)
        assert(strFileStorageStrSerialisation.getErrorMsg() == "")
        `when`(absoluteFile.exists()).thenReturn(false)
        val result = strFileStorageStrSerialisation.read(absoluteFile, bufferReader)
        assert(strFileStorageStrSerialisation.getErrorMsg() == StringFileStorageStrSerialisation.FILE_DOES_NOT_EXIST + absoluteFile.name)
        assert(!result)
    }

    @Test
    fun `read from file failure when file doesn't exist`() = runTest(dispatcher) {
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization, dispatcher)
        assert(strFileStorageStrSerialisation.getErrorMsg() == "")
        val result = strFileStorageStrSerialisation.read(absoluteFile, bufferReader)
        assert(strFileStorageStrSerialisation.getErrorMsg() == StringFileStorageStrSerialisation.FILE_DOES_NOT_EXIST + file.name)
        assert(!result)
    }

    @Test
    fun `read from file failure when file exist check throws an exception`() = runTest(dispatcher) {
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization, dispatcher)
        `when`(absoluteFile.exists()).thenThrow(securityException)

        val result = strFileStorageStrSerialisation.read(absoluteFile, bufferReader)
        assert(strFileStorageStrSerialisation.getErrorMsg() == securityException.message.toString())
        assert(!result)
    }

    @Test
    fun `read from file success when string serialisation read returns true`() = runTest(dispatcher) {
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization, dispatcher)
        `when`(absoluteFile.exists()).thenReturn(true)
        `when`(stringSerialization.read(bufferReader)).thenReturn(true)
        `when`(stringSerialization.getContents()).thenReturn("test")
        val result = strFileStorageStrSerialisation.read(absoluteFile, bufferReader)
        assert(result)
        assert(strFileStorageStrSerialisation.getContents() == "test")
    }

    @Test
    fun `read from file failure when string serialisation read returns false`() = runTest(dispatcher) {
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization, dispatcher)
        `when`(absoluteFile.exists()).thenReturn(true)
        `when`(stringSerialization.read(bufferReader)).thenReturn(false)
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
                                                     fileReturn: Boolean,
                                                     createFileReturn: Boolean,
                                                     makeDirectoryReturn: Boolean) {
        `when`(directory.exists()).thenReturn(directoryReturn)
        `when`(directory.mkdir()).thenReturn(makeDirectoryReturn)
        //`when`(file.exists()).thenReturn(fileReturn)
        //`when`(file.createNewFile()).thenReturn(createFileReturn)
        `when`(file.name).thenReturn("test.txt")
        `when`(stringSerialization.write(fileWriter, "test")).thenReturn(true)
    }
}