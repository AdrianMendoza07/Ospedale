/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.model.Administrator;
import core.model.DataRepository;
import core.model.Doctor;
import core.model.Patient;
import core.model.User;
import java.util.HashMap;

/**
 *
 * @author adria
 */
public class AuthController {

    public static Response LogIn(String username, String password) {

        try {

            if (username == null || username.trim().equals("") ) {
                return new Response("Username field cannot be empty.", Status.BAD_REQUEST);
            }

            if (password == null || password.trim().equals("")) {
                return new Response("Password field cannot be empty.", Status.BAD_REQUEST);
            }

            DataRepository storage = DataRepository.getInstance();
            User user = storage.getUserByUsername(username);
            if (user == null || !user.getPassword().equals(password)) {
                return new Response("Incorrect username or password.", Status.UNAUTHORIZED);
            }

            if (user.getPassword().equals(password)) {
                HashMap<String, Object> userData = new HashMap<>();
                userData.put("username", user.getUsername());

                if (user instanceof Patient) {
                    userData.put("role", "PATIENT");
                } else if (user instanceof Doctor) {
                    userData.put("role", "DOCTOR");
                } else if (user instanceof Administrator) {
                    userData.put("role", "ADMIN");
                }

                return new Response("Successful login.", Status.OK, userData);
            } else {
                return new Response("Incorrect username or password", Status.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }
}
