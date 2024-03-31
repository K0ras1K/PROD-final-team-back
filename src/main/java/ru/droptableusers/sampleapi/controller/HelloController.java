package ru.droptableusers.sampleapi.controller;

import io.ktor.server.application.ApplicationCall;

public class HelloController {
    public static String handle(ApplicationCall call) {
        return "Hello from Java";
    }
}
