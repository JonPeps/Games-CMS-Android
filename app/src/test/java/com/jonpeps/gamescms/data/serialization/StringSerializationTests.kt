package com.jonpeps.gamescms.data.serialization

import com.jonpeps.gamescms.data.serialization.string.StringSerialization
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.BufferedReader
import java.io.FileWriter
import java.io.IOException
import java.io.UncheckedIOException
import java.util.stream.Stream

@RunWith(JUnit4::class)
class StringSerializationTests {
    @Test
    fun `SUCCESS WHEN string WRITTEN to file`() {
        val serializeString = StringSerialization()
        val mockFileWriter = mockk<FileWriter>(relaxed = true)
        every { mockFileWriter.write("123") } returns Unit
        every { mockFileWriter.close() } returns Unit
        val success = serializeString.write(mockFileWriter, "123")
        verify { mockFileWriter.write("123") }
        verify { mockFileWriter.close() }
        assert(success)
        assert(serializeString.getErrorMsg() == "")
    }

    @Test
    fun `string WRITTEN to file FAILS due to IO exception`() {
        val serializeString = StringSerialization()
        val mockFileWriter = mockk<FileWriter>(relaxed = true)
        every { mockFileWriter.write("123") } throws IOException("Write failed!")
        every { mockFileWriter.close() } returns Unit
        val success = serializeString.write(mockFileWriter, "123")
        verify { mockFileWriter.write("123") }
        verify { mockFileWriter.close() }
        assert(!success)
        assert(serializeString.getErrorMsg() == "java.io.IOException: Write failed!")
    }

    @Test
    fun `SUCCESS WHEN READING string from file`() {
        val serializeString = StringSerialization()
        val mockBufferedReader = mockk<BufferedReader>(relaxed = true)
        every { mockBufferedReader.lines() } returns Stream.of("1", "2")
        every { mockBufferedReader.close() } returns Unit
        val success = serializeString.read(mockBufferedReader)
        verify { mockBufferedReader.close() }
        assert(success)
        assert(serializeString.getContents() == "12")
    }

    @Test
    fun `FAILURE WHEN READING string from file THROWS exception`() {
        val serializeString = StringSerialization()
        val mockBufferedReader = mockk<BufferedReader>(relaxed = true)
        val mockUncheckedIOException = mockk<UncheckedIOException>()
        every { mockBufferedReader.lines() } throws mockUncheckedIOException
        val success = serializeString.read(mockBufferedReader)
        verify { mockBufferedReader.close() }
        assert(!success)
    }
}