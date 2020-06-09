package net.razvan

import io.ktor.client.HttpClient
import io.ktor.http.*
import io.ktor.http.content.PartData
import io.ktor.server.testing.*
import io.ktor.utils.io.streams.asInput
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class ApplicationKtTest {
    private val boundary = "***bbb***"
    private val  sysTempDir = System.getProperty("java.io.tmpdir") ?: "/tmp"

    private val multipart = listOf(PartData.FileItem({ byteArrayOf(1, 2, 3).inputStream().asInput() }, {}, headersOf(
            HttpHeaders.ContentDisposition,
            ContentDisposition.File
                    .withParameter(ContentDisposition.Parameters.Name, "file")
                    .withParameter(ContentDisposition.Parameters.FileName, "test.jpg")
                    .toString()
    )))

    @Test
    fun testUploadApplication() = testApp {
        handlePost("/", boundary, multipart).apply {
            println("============= RESPONSE ====================")
            println(response.content)
            println("=================================")
        }
    }
}

private fun TestApplicationEngine.handlePost(uri: String,
         boundary: String,
         multipart: List<PartData.FileItem>,
         setup: TestApplicationRequest.() -> Unit = {}
): TestApplicationCall {
    return handleRequest(method = HttpMethod.Post, uri = uri)  {
        addHeader(HttpHeaders.ContentType,
                ContentType.MultiPart.FormData.withParameter("boundary", boundary).toString()
        )
        setBody(boundary, multipart)
        setup()
    }
}

private fun testApp(callback: TestApplicationEngine.() -> Unit): Unit {
    withTestApplication({ module(true) }, callback)
}