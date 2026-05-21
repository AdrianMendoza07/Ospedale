/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers.utils;

import java.util.HashMap;

/**
 *
 * @author adria
 */
public class Response {
    private String message;
    private int status;
    private HashMap<String, Object> data;

    public Response(String message, int status) {
        this.message = message;
        this.status = status;
    }
    
    public Response(String message, int status, HashMap<String, Object> data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public HashMap<String, Object> getData() {
        return data;
    }
}
