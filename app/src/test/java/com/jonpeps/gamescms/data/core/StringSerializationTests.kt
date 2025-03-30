package com.jonpeps.gamescms.data.core

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.mock
import java.io.BufferedReader
import java.io.FileWriter
import java.io.IOException
import java.io.UncheckedIOException
import java.util.stream.Stream

@RunWith(JUnit4::class)
class StringSerializationTests {
    @Test
    fun `success when string written to file`() {
        val serializeString = StringSerialization()
        val mockFileWriter = mock(FileWriter::class.java)
        val success = serializeString.write("testFile.txt", mockFileWriter, "123")
        Mockito.verify(mockFileWriter).write("123")
        Mockito.verify(mockFileWriter).close()
        assert(success)
        assert(serializeString.getErrorMsg() == "")
    }

    @Test
    fun `failure when string written to file due to IO exception`() {
        val serializeString = StringSerialization()
        val mockFileWriter = mock(FileWriter::class.java)
        Mockito.`when`(mockFileWriter.write("123")).thenThrow(IOException("Write failed!"))
        val success = serializeString.write("testFile.txt", mockFileWriter, "123")
        Mockito.verify(mockFileWriter).write("123")
        Mockito.verify(mockFileWriter).close()
        assert(!success)
        assert(serializeString.getErrorMsg() == "java.io.IOException: Write failed!")
    }

    @Test
    fun `success when reading string from file`() {
        val serializeString = StringSerialization()
        val mockBufferedReader = mock(BufferedReader::class.java)
        Mockito.`when`(mockBufferedReader.lines()).thenReturn(Stream.of("1", "2")).thenReturn(null)
        val success = serializeString.read(mockBufferedReader)
        Mockito.verify(mockBufferedReader).close()
        assert(success)
        assert(serializeString.getContents() == "12")
    }

    @Test
    fun `failure when reading string from file throws exception`() {
        val serializeString = StringSerialization()
        val mockBufferedReader = mock(BufferedReader::class.java)
        val mockUncheckedIOException = mock(UncheckedIOException::class.java)
        Mockito.`when`(mockBufferedReader.lines()).thenThrow(mockUncheckedIOException)
        val success = serializeString.read(mockBufferedReader)
        Mockito.verify(mockBufferedReader).close()
        assert(!success)
    }
}