/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.model.DataRepository;
import core.model.User;

/**
 *
 * @author adria
 */
public class AuthController {

    public static Response LogIn(String username, String password) {

        try {
            DataRepository storage = DataRepository.getInstace();
            User user = storage.getUser(username);
            if (user == null) {
                return new Response("User doesnt exist.", Status.NOT_FOUND);
            }

            if (username.trim().equals("")) {
                return new Response("Username field cannot be empty.", Status.BAD_REQUEST);
            }

            if (password.trim().equals("")) {
                return new Response("Password field cannot be empty.", Status.BAD_REQUEST);
            }

            if (user.getPassword().equals(password)) {
                return new Response("Username and Password are correct, sucessfull login.", Status.OK);
            } else {
                return new Response("Incorrect Password, failed login.", Status.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }
}
