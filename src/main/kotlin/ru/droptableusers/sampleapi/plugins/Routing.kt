package ru.droptableusers.sampleapi.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.droptableusers.sampleapi.controller.analytics.TourAnalyticController
import ru.droptableusers.sampleapi.controller.analytics.UserAnalyticController
import ru.droptableusers.sampleapi.controller.documents.AdminDocumentsController
import ru.droptableusers.sampleapi.controller.files.AuthDownloadController
import ru.droptableusers.sampleapi.controller.files.AuthUploadController
import ru.droptableusers.sampleapi.controller.files.PublicDownloadController
import ru.droptableusers.sampleapi.controller.groups.AuthGroupController
import ru.droptableusers.sampleapi.controller.tags.TagsController
import ru.droptableusers.sampleapi.controller.teams.AdminTeamsController
import ru.droptableusers.sampleapi.controller.teams.AuthTeamsController
import ru.droptableusers.sampleapi.controller.teams.PublicTeamsController
import ru.droptableusers.sampleapi.controller.tours.AuthTourResultsController
import ru.droptableusers.sampleapi.controller.users.AdminUsersController
import ru.droptableusers.sampleapi.controller.users.AuthUsersController
import ru.droptableusers.sampleapi.controller.users.UsersController
import ru.droptableusers.sampleapi.controller.vacancies.AuthVacancyController
import ru.droptableusers.sampleapi.controller.vacancies.VacancyController

/**
 * Configure routing
 *
 * @author Roman K0ras1K Kalmykov
 */
fun Application.configureRouting() {
    routing {
        openAPI(path = "openapi", swaggerFile = "openapi/documentation.yaml")

        get("/") {
            call.respondText("DropTableUsers API 1.0.")
        }

        route("/1.0") {
            route("/public") {
                get("/{folder_name}/{file_name}") {
                    PublicDownloadController(call).download()
                }
            }

            route("/tags") {
                get {
                    TagsController(call).getAllTags()
                }
            }

            authenticate("auth-jwt") {
                route("/private") {
                    get("/{file_name}") {
                        AuthDownloadController(call).download()
                    }
                }
            }
            route("/users") {
                post("/register") {
                    UsersController(call).register()
                }
                post("/login") {
                    UsersController(call).login()
                }

                get("/withoutTeam") {
                    UsersController(call).selectWithoutTeam()
                }

                get("/withoutTeamml"){

                }

                get("/tags/{userId}") {
                    TagsController(call).getUserTags()
                }

                post("/tags") {
                    TagsController(call).addUserTags()
                }

                delete("/tags") {
                    TagsController(call).removeUserTag()
                }

                patch("/{userId}") {
                    AuthUsersController(call).updateUser()
                }

                patch("/{userId}/password") {
                    AuthUsersController(call).updateUserPassword()
                }

                authenticate("auth-jwt") {
                    get("/my") {
                        AuthUsersController(call).get()
                    }
                }
                route("/invites") {
                    authenticate("auth-jwt") {
                        post("/apply/{userId}") {
                            AuthUsersController(call).apply()
                        }
                        post("/accept/{inviteId}") {
                            AuthUsersController(call).accept()
                        }
                        get("/load") {
                            AuthUsersController(call).getUserInvites()
                        }
                    }
                }
            }
            route("/teams") {
                get("/load") {
                    PublicTeamsController(call).loadAll()
                }
                get("/loadml") {
                    PublicTeamsController(call).loadAllByML()
                }

                get("/tags/{teamId}") {
                    TagsController(call).getTeamsTags()
                }

                get("/template"){
                    PublicTeamsController(call).getTeamTemplate()
                }

                authenticate("auth-jwt") {
                    post("/create") {
                        AuthTeamsController(call).createTeam()
                    }
                    get("/load/{id}") {
                        AuthTeamsController(call).loadTeam()
                    }
                    post("/apply/{teamId}") {
                        AuthTeamsController(call).apply()
                    }
                    post("/accept/{inviteId}") {
                        AuthTeamsController(call).accept()
                    }
                    get("/invites") {
                        AuthTeamsController(call).loadRequests()
                    }
                    get("/loadmy") {
                        AuthTeamsController(call).loadMy()
                    }
                    route("/vacancy"){
                        post("/generate"){
                            AuthVacancyController(call).generateVacancies()
                        }

                        get("/{teamId}"){
                            AuthVacancyController(call).get()
                        }
                    }
                }


            }



            route("/vacancy") {
                get("/{id}") {
                    VacancyController(call).get()
                }
                authenticate("auth-jwt") {
//                    post {
//                        VacancyController(call).add()
//                    }
                    delete("/{id}") {
                        VacancyController(call).delete()
                    }
                }
            }
            route("/tours") {
                authenticate("auth-jwt") {
                    get("/results/user") {
                        AuthTourResultsController(call).getResultsByUserId()
                    }
                    get("/results/name") {
                        AuthTourResultsController(call).getResultsByTourName()
                    }
                    get("/list") {
                        AuthTourResultsController(call).listTours()
                    }
                }
            }
            route("/files") {
                route("/upload") {
                    authenticate("auth-jwt") {
                        post("/pdf") {
                            AuthUploadController(call).uploadPdfFile()
                        }
                        post("/documentTemplate") {
                            AuthUploadController(call).uploadDocumentTemplateFile()
                        }
                        post("/filledDocument") {
                            AuthUploadController(call).uploadFilledDocumentFile()
                        }
                    }
                }
            }
            route("/admin") {
                authenticate("auth-jwt") {
                    route("/groups") {
                        post("/set") {
                            AuthGroupController(call).setGroup()
                        }
                    }
                    route("/documents") {
                        get("/list") {
                            AdminDocumentsController(call).listDocuments()
                        }
                        post("/create") {
                            AdminDocumentsController(call).addDocument()
                        }
                        patch("/update") {
                            AdminDocumentsController(call).editDocument()
                        }
                        delete("/{documentId}/delete") {
                            AdminDocumentsController(call).deleteDocument()
                        }
                    }
                    route("/analytics") {
                        get("/tour/{tourId}") {
                            TourAnalyticController(call).getTourAnalytic()
                        }
                        get("/user/{id}") {
                            UserAnalyticController(call).getUserAnalytic()
                        }
                    }

                    route("/teams"){
                        route("/template"){
                            get{
                                PublicTeamsController(call).getTeamTemplate()
                            }
                            post {
                                AdminTeamsController(call).editTeamTemplate()
                            }
                        }

                        post{
                            AdminTeamsController(call).createTeam()
                        }

                        delete("/{teamId}") {
                            AdminTeamsController(call).deleteTeam()
                        }

                        route("/members"){
                            post {
                                AdminTeamsController(call).addMember()
                            }

                            delete {
                                AdminTeamsController(call).removeMember()
                            }
                        }
                    }

                    route("/tags") {
                        get {
                            TagsController(call).getAllTags()
                        }

                        post {
                            TagsController(call).createTags()
                        }

                        delete {
                            TagsController(call).deleteTags()
                        }
                    }

                    route("/users") {
                        get {
                            AdminUsersController(call).listUsers()
                        }
                        post("/notify") {
                            AdminUsersController(call).telegramNotify()
                        }
                    }
                }
            }
        }
    }
}
