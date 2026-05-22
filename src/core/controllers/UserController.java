/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.model.User;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 *
 * @author adria
 */
public class UserController {
    
    public static Response LogIn(String username, String password){
        
        try {
            Storage storage = Storage.getInstace();
            User user = storage.getUser(username);
            if(user == null){
                return new Response("User doesnt exist.", Status.NOT_FOUND);
            }
            
            if(username.trim().equals("")){
                return new Response("Username field cannot be empty.", Status.BAD_REQUEST);
            }
            
            if(password.trim().equals("")){
                return new Response("Password field cannot be empty.", Status.BAD_REQUEST);
            }
            
            if(user.getPassword().equals(password) ){
                return new Response("Username and Password are correct, sucessfull login.", Status.OK);
            }else{
                return new Response("Incorrect Password, failed login.", Status.UNAUTHORIZED);
            }
        } catch (Exception e) {
           return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }
    
    public static Response createPatient(String id, String username, String firstname, String lastname, String password, String email, String birthdate, String gender, String phone, String address){
        try {
            long longId, longPhone;
            boolean boolGender;
            LocalDate dateBirthdate;
            
            try {
                longId = Integer.parseInt(id.trim());
                if(longId<0){
                    return new Response("Id must be positive.", Status.BAD_REQUEST);
                }
                if(id.length()!= 12){
                    return new Response("Id must have 12 digits.", Status.BAD_REQUEST);
                }

            } catch (NumberFormatException e) {
                return new Response("Id must be a number", Status.BAD_REQUEST);
            }
            
            try {
                longPhone = Integer.parseInt(phone.trim());
                if(longPhone<0){
                    return new Response("Phone must be positive.", Status.BAD_REQUEST);
                }
                if(phone.length()!= 12){
                    return new Response("Phone must have 12 digits.", Status.BAD_REQUEST);
                }

            } catch (NumberFormatException e) {
                return new Response("Phone must be a number", Status.BAD_REQUEST);
            }
            
            try {
                dateBirthdate = LocalDate.parse(birthdate);
            } catch (DateTimeParseException e) {
                return new Response("Invalid date", Status.BAD_REQUEST);
            }
            
            if (firstname.trim().equals("")) {
                return new Response("Firstname must not be empty", Status.BAD_REQUEST);
            }
            
            if (lastname.trim().equals("")) {
                return new Response("Lastname must not be empty", Status.BAD_REQUEST);
            }
            
            if (password.trim().equals("")) {
                return new Response("Password must not be empty", Status.BAD_REQUEST);
            }
            
            
            if (email.trim().equals("")) {
                return new Response("Email must not be empty", Status.BAD_REQUEST);
            }
            String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.com$";
            if (email.matches(emailRegex)) {
                return new Response("Not a valid email", Status.BAD_REQUEST);
            }
            
            if(gender.trim().equals("Female")){
                boolGender = false;
            }
            
            if(gender.trim().equals("Male")){
                boolGender = true;
            }

            
            
        } catch (Exception e) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }
    
    
}
