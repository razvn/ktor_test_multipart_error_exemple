package net.razvan

import io.ktor.application.*
import io.ktor.features.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.post
import io.ktor.routing.routing

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
    }

    routing {
        post("/stuck") {
            val fileName = "(unset)"
            val mp = try {
                val mp = call.receiveMultipart()
            } catch (e: Exception) {
                call.application.log.error("Getting multipart error", e)
                null
            }

           if (mp == null) call.respond(HttpStatusCode.BadRequest, "Null value")
           else call.respond(HttpStatusCode.OK,"File name: $fileName")
        }

        post("/stuck2") {
            val fileName = "(unset)"
            val mp = try {
                val mp = call.receiveMultipart()

                mp.readPart()?.dispose
            } catch (e: Exception) {
                call.application.log.error("Getting multipart error", e)
                null
            }

            if (mp == null) call.respond(HttpStatusCode.BadRequest, "Null value")
            else call.respond(HttpStatusCode.OK,"File name: $fileName")
        }

        post("/ok") {
            var fileName = "(unset)"
            val mp = try {
                val mp = call.receiveMultipart()

                mp.forEachPart { part ->
                    when (part) {
                        is PartData.FileItem -> fileName = part.originalFileName ?: "(no file name)"
                        else -> call.application.log.info("Not a file item: ${part.name}")
                    }
                    part.dispose
                }
            } catch (e: Exception) {
                call.application.log.error("Getting multipart error", e)
                null
            }

            if (mp == null) call.respond(HttpStatusCode.BadRequest, "Null value")
            else call.respond(HttpStatusCode.OK,"File name: $fileName")
        }

        post("/ok2") {
            val fileName = "(unset)"
            val mp = try {
                val mp = call.receiveMultipart()

                mp.forEachPart {

                }
            } catch (e: Exception) {
                call.application.log.error("Getting multipart error", e)
                null
            }

            if (mp == null) call.respond(HttpStatusCode.BadRequest, "Null value")
            else call.respond(HttpStatusCode.OK,"File name: $fileName")
        }
    }
}

