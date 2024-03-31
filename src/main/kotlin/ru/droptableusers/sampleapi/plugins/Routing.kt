package ru.droptableusers.sampleapi.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.droptableusers.sampleapi.UsersController
import ru.droptableusers.sampleapi.controller.HelloController
import ru.droptableusers.sampleapi.controller.teams.AuthTeamsController
import ru.droptableusers.sampleapi.controller.teams.PublicTeamsController
import ru.droptableusers.sampleapi.controller.users.AuthUsersController

/**
 * Configure routing
 *
 * @author Roman K0ras1K Kalmykov
 */
fun Application.configureRouting() {
    routing {
        openAPI(path = "openapi", swaggerFile = "openapi/documentation.yaml")

        get("/") {
            call.respondText("Hello World!")
        }
        get("/java") {
            call.respondText(HelloController.handle(call))
        }
        get("/kotlin") {
            call.respondText("Hello from Kotlin!")
        }

//        post("/1.0/users/register") {
//            UsersController(call).register()
//        }

        route("/1.0") {
            route("/users") {
                post("/register") {
                    UsersController(call).register()
                }
                post("/login") {
                    UsersController(call).login()
                }
                authenticate("auth-jwt") {
                    get("/my") {
                        AuthUsersController(call).get()
                    }
                }
            }
            route("/teams") {
                get("/load") {
                    PublicTeamsController(call).loadAll()
                }
                authenticate("auth-jwt") {
                    post("/create") {
                        AuthTeamsController(call).createTeam()
                    }
                    get("/load/{id}") {
                        AuthTeamsController(call).loadTeam()
                    }
                    post("/apply") {

                    }
                    get("/loadmy") {

                    }
                }
            }
        }
    }
}
