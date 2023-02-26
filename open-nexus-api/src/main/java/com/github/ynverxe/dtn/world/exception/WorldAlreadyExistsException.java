package com.github.ynverxe.dtn.world.exception;

public class WorldAlreadyExistsException extends RuntimeException {
    public WorldAlreadyExistsException(String world) {
        super("World '" + world + "' already exists!");
    }
}
