/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.model.DataRepository;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 *
 * @author adria
 */
public class DoctorContoller {
    public static Response createPatient(String id, String username, String firstname, String lastname, String password, String comPassword, String email, String birthdate, String gender, String phone, String address) {

        try {
            long longId, longPhone;
            boolean boolGender;
            LocalDate dateBirthdate;

            if(id.trim().equals("")){
                return new Response("Id must not be empty", Status.BAD_REQUEST);
            }
            if (username.trim().equals("")) {
                return new Response("Username must not be empty", Status.BAD_REQUEST);
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
            if (comPassword.trim().equals("")) {
                return new Response("Password comfirmation must not be empty", Status.BAD_REQUEST);
            }
            if (email.trim().equals("")) {
                return new Response("Email must not be empty", Status.BAD_REQUEST);
            }
            if(phone.trim().equals("")){
                return new Response("Phone must not be empty", Status.BAD_REQUEST);
            }
            if (address.trim().equals("")) {
                return new Response("Address must not be empty", Status.BAD_REQUEST);
            }
            try {
                longId = Long.parseLong(id.trim());
                if (longId < 0) {
                    return new Response("Id must be positive.", Status.BAD_REQUEST);
                }
                if (id.length() != 12) {
                    return new Response("Id must have 12 digits.", Status.BAD_REQUEST);
                }

            } catch (NumberFormatException e) {
                return new Response("Id must be a number", Status.BAD_REQUEST);
            }

            try {
                longPhone = Long.parseLong(phone.trim());
                if (longPhone < 0) {
                    return new Response("Phone must be positive.", Status.BAD_REQUEST);
                }
                if (phone.length() != 12) {
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

            if (!password.equals(comPassword)) {
                return new Response("Password and password comfirmation must be the same", Status.BAD_REQUEST);
            }

            String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.com$";
            if (!email.matches(emailRegex)) {
                return new Response("Not a valid email", Status.BAD_REQUEST);
            }

            if (gender.trim().equals("Female")) {
                boolGender = false;
            }

            if (gender.trim().equals("Male")) {
                boolGender = true;
            }

            DataRepository storage = DataRepository.getInstace();
            if (!storage.addPatient(longId, username, firstname, lastname, password, email, dateBirthdate, boolGender, longPhone, address)) {
                return new Response("Patient with those cretentials already exists", Status.BAD_REQUEST);
            }

            return new Response("Patient created successfully", Status.CREATED);

        } catch (Exception e) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }
}
