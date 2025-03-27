package com.jonpeps.gamescms.io

import com.jonpeps.gamescms.data.core.SerializeString
import com.jonpeps.gamescms.data.core.SerializeStringStatus
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import java.io.BufferedReader
import java.io.FileWriter
import java.io.IOException
import java.io.UncheckedIOException
import java.util.stream.Stream

@RunWith(JUnit4::class)
class SerializeStringTests {
    @Test
    fun `success when string written to file`() {
        val mockSerializeString = SerializeString()
        val mockFileWriter = Mockito.mock(FileWriter::class.java)
        mockSerializeString.write("testFile.txt", mockFileWriter, "123")
        Mockito.verify(mockFileWriter).write("123")
        Mockito.verify(mockFileWriter).close()
        val status = mockSerializeString.getStatus()
        assert(status?.status == SerializeStringStatus.SUCCESS)
        assert(status?.message == "")
    }

    @Test
    fun `failure when string written to file throws exception`() {
        val mockSerializeString = SerializeString()
        val mockFileWriter = Mockito.mock(FileWriter::class.java)
        Mockito.`when`(mockFileWriter.write("123")).thenThrow(IOException("Write failed!"))
        mockSerializeString.write("testFile.txt", mockFileWriter, "123")
        Mockito.verify(mockFileWriter).write("123")
        Mockito.verify(mockFileWriter).close()
        val status = mockSerializeString.getStatus()
        assert(status?.status == SerializeStringStatus.WRITE_FAILED)
        assert(status?.message == "Write failed!")
    }

    @Test
    fun `success when reading string from file`() {
        val mockSerializeString = SerializeString()
        val mockBufferedReader = Mockito.mock(BufferedReader::class.java)
        Mockito.`when`(mockBufferedReader.lines()).thenReturn(Stream.of("1", "2")).thenReturn(null)
        val result = mockSerializeString.read(mockBufferedReader)
        Mockito.verify(mockBufferedReader).close()
        assert(result == "12")
        val status = mockSerializeString.getStatus()
        assert(status?.status == SerializeStringStatus.SUCCESS)
        assert(status?.message == "")
    }

    @Test
    fun `failure when reading string from file throws exception`() {
        val mockSerializeString = SerializeString()
        val mockBufferedReader = Mockito.mock(BufferedReader::class.java)
        val mockUncheckedIOException = Mockito.mock(UncheckedIOException::class.java)
        Mockito.`when`(mockBufferedReader.lines()).thenThrow(mockUncheckedIOException)
        val result = mockSerializeString.read(mockBufferedReader)
        Mockito.verify(mockBufferedReader).close()
        assert(result == "")
        val status = mockSerializeString.getStatus()
        assert(status?.status == SerializeStringStatus.READ_FAILED)
        assert(status?.message == SerializeString.UNCHECKED_IO_MESSAGE)
    }
}