package com.jonpeps.gamescms.data.serialization

import com.jonpeps.gamescms.data.serialization.string.IStrFileStorageReadContents
import com.jonpeps.gamescms.data.serialization.string.IStrFileStorageWriteContents
import com.jonpeps.gamescms.data.serialization.string.StringFileStorageStrSerialisation
import com.jonpeps.gamescms.data.serialization.string.StringSerialization
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.io.File
import java.io.FileWriter

@RunWith(MockitoJUnitRunner::class)
class StringFileStorageStrSerialisationTests {
    @Mock
    private lateinit var stringSerialization: StringSerialization
    @Mock
    private lateinit var toWrite: IStrFileStorageWriteContents
    @Mock
    private lateinit var toRead: IStrFileStorageReadContents
    @Mock
    private lateinit var fileWriter: FileWriter
    @Mock
    private lateinit var directory: File
    @Mock
    private lateinit var file: File
    @Mock
    private lateinit var securityException: SecurityException

    @Test
    fun `test write to file success when directory, file path and create file don't initially exist`() {
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization)
        assert(strFileStorageStrSerialisation.getErrorMsg() == "")

        commonFileReturnScenariosForWriteStr(
            directoryReturn = false,
            fileReturn = false,
            createFileReturn = true,
            makeDirectoryReturn = true,
        )

        val result = strFileStorageStrSerialisation.write(toWrite)
        assert(result)
    }

    @Test
    fun `test write to file success when directory, file path and create file exist`() {
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization)
        assert(strFileStorageStrSerialisation.getErrorMsg() == "")

        commonFileReturnScenariosForWriteStr(
            directoryReturn = true,
            fileReturn = true,
            createFileReturn = true,
            makeDirectoryReturn = true,
        )
        val result = strFileStorageStrSerialisation.write(toWrite)
        assert(result)
    }

    @Test
    fun `test write to file failure when directory doesn't exist`() {
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization)
        assert(strFileStorageStrSerialisation.getErrorMsg() == "")

        commonFileReturnScenariosForWriteStr(
            directoryReturn = false,
            fileReturn = false,
            createFileReturn = false,
            makeDirectoryReturn = false,
        )
        val result = strFileStorageStrSerialisation.write(toWrite)
        assert(strFileStorageStrSerialisation.getErrorMsg() == StringFileStorageStrSerialisation.FAILED_TO_CREATE_DIRECTORY + directory.name)
        assert(!result)
    }

    @Test
    fun `test write to file failure when file doesn't exist`() {
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization)
        assert(strFileStorageStrSerialisation.getErrorMsg() == "")

        commonFileReturnScenariosForWriteStr(
            directoryReturn = true,
            fileReturn = false,
            createFileReturn = false,
            makeDirectoryReturn = true,
        )
        val result = strFileStorageStrSerialisation.write(toWrite)
        assert(strFileStorageStrSerialisation.getErrorMsg() == StringFileStorageStrSerialisation.FAILED_TO_CREATE_FILE + file.name)
        assert(!result)
    }

    @Test
    fun `test write to file failure when file creation fails`() {
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization)
        assert(strFileStorageStrSerialisation.getErrorMsg() == "")

        commonFileReturnScenariosForWriteStr(
            directoryReturn = true,
            fileReturn = false,
            createFileReturn = false,
            makeDirectoryReturn = true,
        )
        val result = strFileStorageStrSerialisation.write(toWrite)
        assert(strFileStorageStrSerialisation.getErrorMsg() == StringFileStorageStrSerialisation.FAILED_TO_CREATE_FILE + file.name)
        assert(!result)
    }

    @Test
    fun `test write to file failure when directory creation fails due to an exception thrown`() {
        `when`(toWrite.getDirectory()).thenReturn(directory)
        `when`(toWrite.getFile()).thenReturn(file)
        `when`(directory.exists()).thenReturn(false)
        `when`(directory.mkdir()).thenThrow(securityException)
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization)
        val result = strFileStorageStrSerialisation.write(toWrite)
        assert(strFileStorageStrSerialisation.getErrorMsg() == securityException.message.toString())
        assert(!result)
    }

    @Test
    fun `read from file success when file exists`() {
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization)
        assert(strFileStorageStrSerialisation.getErrorMsg() == "")
        `when`(toRead.getAbsoluteFile()).thenReturn(file)
        `when`(file.exists()).thenReturn(true)
        `when`(stringSerialization.read(toRead.getBufferedReader())).thenReturn(true)
        `when`(stringSerialization.getContents()).thenReturn("test")
        val result = strFileStorageStrSerialisation.read(toRead)
        verify(stringSerialization).getContents()
        assert(strFileStorageStrSerialisation.getContents() == "test")
        assert(result)
    }

    @Test
    fun `read from file failure when file doesn't exist`() {
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization)
        assert(strFileStorageStrSerialisation.getErrorMsg() == "")
        `when`(toRead.getAbsoluteFile()).thenReturn(file)
        `when`(file.exists()).thenReturn(false)

        val result = strFileStorageStrSerialisation.read(toRead)
        assert(strFileStorageStrSerialisation.getErrorMsg() == StringFileStorageStrSerialisation.FILE_DOES_NOT_EXIST + file.name)
        assert(!result)
    }

    @Test
    fun `read from file failure when file exist check throws an exception`() {
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization)
        assert(strFileStorageStrSerialisation.getErrorMsg() == "")
        `when`(toRead.getAbsoluteFile()).thenReturn(file)
        `when`(file.exists()).thenThrow(securityException)

        val result = strFileStorageStrSerialisation.read(toRead)
        assert(strFileStorageStrSerialisation.getErrorMsg() == securityException.message.toString())
        assert(!result)
    }

    @Test
    fun `read from file failure when read file contents returns false`() {
        val strFileStorageStrSerialisation
                = StringFileStorageStrSerialisation(stringSerialization)
        assert(strFileStorageStrSerialisation.getErrorMsg() == "")
        `when`(toRead.getAbsoluteFile()).thenReturn(file)
        `when`(file.exists()).thenReturn(true)
        `when`(stringSerialization.read(toRead.getBufferedReader())).thenReturn(false)
        val result = strFileStorageStrSerialisation.read(toRead)
        verify(stringSerialization).getErrorMsg()
        assert(!result)
    }

    private fun commonFileReturnScenariosForWriteStr(directoryReturn: Boolean,
                                                     fileReturn: Boolean,
                                                     createFileReturn: Boolean,
                                                     makeDirectoryReturn: Boolean) {
        `when`(toWrite.getDirectory()).thenReturn(directory)
        `when`(toWrite.getFile()).thenReturn(file)
        `when`(directory.exists()).thenReturn(directoryReturn)
        `when`(directory.mkdir()).thenReturn(makeDirectoryReturn)
        `when`(file.exists()).thenReturn(fileReturn)
        `when`(file.createNewFile()).thenReturn(createFileReturn)
        `when`(file.name).thenReturn("test.txt")
        `when`(toWrite.getFileWriter()).thenReturn(fileWriter)
        `when`(toWrite.getContents()).thenReturn("")
        `when`(stringSerialization.write(file.name, fileWriter, toWrite.getContents())).thenReturn(true)
    }
}