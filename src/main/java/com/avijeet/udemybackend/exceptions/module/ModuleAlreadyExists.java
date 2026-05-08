package com.avijeet.udemybackend.exceptions.module;

public class ModuleAlreadyExists extends RuntimeException {
    public ModuleAlreadyExists(String message) {
        super(message);
    }
}
