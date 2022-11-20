package com.example.authenticationdemo.exception;

/*

Объект ошибки с запрашиваемым id объекта

 */

import lombok.Value;

import java.util.UUID;

@Value
public class ObjectNotFoundException extends RuntimeException {

    UUID id;

}
