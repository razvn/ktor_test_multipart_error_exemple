package net.razvan

import io.ktor.application.*
import io.ktor.features.ContentNegotiation
import io.ktor.http.HttpStatusCode
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
        post("/") {
            val mp = try {
                call.receiveMultipart()
            } catch (e: Exception) {
                call.application.log.info("Erreur lors de la recuperation du multipart")
                null
            }

           if (mp == null) call.respond(HttpStatusCode.BadRequest, "Null value")
           else call.respond(HttpStatusCode.OK,"OK")
        }
    }
}

