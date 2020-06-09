package net.razvan

import io.ktor.http.*
import io.ktor.http.content.PartData
import io.ktor.server.testing.*
import io.ktor.utils.io.streams.asInput
import org.junit.jupiter.api.Test

internal class ApplicationKtTest {
    private val boundary = "***bbb***"
    private val sysTempDir = System.getProperty("java.io.tmpdir") ?: "/tmp"

    private val multipart = listOf(PartData.FileItem({ byteArrayOf(1, 2, 3).inputStream().asInput() }, {}, headersOf(
            HttpHeaders.ContentDisposition,
            ContentDisposition.File
                    .withParameter(ContentDisposition.Parameters.Name, "file")
                    .withParameter(ContentDisposition.Parameters.FileName, "test.jpg")
                    .toString()
    )))

    @Test
    fun `fail when multipart NOT USED`() = testApp {
        handlePost("/stuck", boundary, multipart).apply {
            println("============= RESPONSE ====================")
            println(response.content)
            println("=================================")
            assert(response.content?.contains("File name") ?: false)
        }
    }

    @Test
    fun `fail when multipart just readPart`() = testApp {
        handlePost("/stuck2", boundary, multipart).apply {
            println("============= RESPONSE ====================")
            println(response.content)
            println("=================================")
            assert(response.content?.contains("File name") ?: false)
        }
    }

    @Test
    fun `working when multiparts are read`() = testApp {
        handlePost("/ok", boundary, multipart).apply {
            println("============= RESPONSE ====================")
            println(response.content)
            println("=================================")
            assert(response.content?.contains("File name") ?: false)
        }
    }

    @Test
    fun `working when multiparts are just forEachPart`() = testApp {
        handlePost("/ok2", boundary, multipart).apply {
            println("============= RESPONSE ====================")
            println(response.content)
            println("=================================")
            assert(response.content?.contains("File name") ?: false)
        }
    }
}

private fun TestApplicationEngine.handlePost(uri: String,
                                             boundary: String,
                                             multipart: List<PartData.FileItem>,
                                             setup: TestApplicationRequest.() -> Unit = {}
): TestApplicationCall {
    return handleRequest(method = HttpMethod.Post, uri = uri) {
        addHeader(HttpHeaders.ContentType,
                ContentType.MultiPart.FormData.withParameter("boundary", boundary).toString()
        )
        setBody(boundary, multipart)
        setup()
    }
}

private fun testApp(callback: TestApplicationEngine.() -> Unit): Unit {
    withTestApplication({ module(false) }, callback)
}
