package com.jhj.cinephile.exception;

public class InvalidHashIdException extends RuntimeException {
    public InvalidHashIdException(String hash) {
        super("無効なハッシュ ID です: " + hash);
    }
}