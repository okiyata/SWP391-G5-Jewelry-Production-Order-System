package com.swp391.JewelryProduction.util.exceptions;

public class ObjectNotFoundException extends Exception{
    public ObjectNotFoundException(){
        super("User not found");
    }
    public ObjectNotFoundException(String message){
        super(message);
    }
}
