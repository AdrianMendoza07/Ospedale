/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.model.DataRepository;
import core.model.Specialty;
import core.model.User;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 *
 * @author adria
 */
public class DoctorContoller {

    public static Response createDoctor(String id, String username, String firstname, String lastname, String password, String comPassword, String inSpecialty, String license, String office) {

        try {
            long longId;
            Specialty dSpecialty;

            if (id.trim().equals("")) {
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
            if (inSpecialty.trim().equals("Select one")) {
                return new Response("Must choose an option", Status.BAD_REQUEST);
            }

            try {
                dSpecialty = Specialty.valueOf(inSpecialty);
            } catch (IllegalArgumentException e) {
                return new Response("Invalid specialty selected", Status.BAD_REQUEST);
            }
            if (license.trim().equals("")) {
                return new Response("License must not be empty", Status.BAD_REQUEST);
            }
            if (office.trim().equals("")) {
                return new Response("Office number must not be empty", Status.BAD_REQUEST);
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

            String licenseRegex = "^L-\\d{10} MTL$";
            if (!license.trim().matches(licenseRegex)) {
                return new Response("Not a valid license number", Status.BAD_REQUEST);
            }

            String officeRegex = "^O-\\d{3}$";
            if (!office.trim().matches(officeRegex)) {
                return new Response("Not a valid office number", Status.BAD_REQUEST);
            }

            if (!password.equals(comPassword)) {
                return new Response("Password and password comfirmation must be the same", Status.BAD_REQUEST);
            }

            DataRepository storage = DataRepository.getInstance();
            if (!storage.addDoctor(longId, username, firstname, lastname, password, license, dSpecialty, office)) {
                return new Response("Doctor with those cretentials already exists", Status.BAD_REQUEST);
            }

            return new Response("Doctor created successfully", Status.CREATED);

        } catch (Exception e) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }
    
    public static Response updateDoctor(String id, String username, String firstname, String lastname, String password, String comPassword, String inSpecialty, String license, String office) {

        try {
            long longId;
            Specialty dSpecialty;

            if (id.trim().equals("")) {
                return new Response("Id must not be empty", Status.BAD_REQUEST);
            }
            if (username.trim().equals("")) {
                return new Response("Username must not be empty", Status.BAD_REQUEST);
            }
            
            DataRepository storage = DataRepository.getInstance();
            User user = storage.getUserByUsername(username);
            if (user == null){
                return new Response("User not found",Status.NOT_FOUND);
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
            if (inSpecialty.trim().equals("Select one")) {
                return new Response("Must choose an option", Status.BAD_REQUEST);
            }

            try {
                dSpecialty = Specialty.valueOf(inSpecialty);
            } catch (IllegalArgumentException e) {
                return new Response("Invalid specialty selected", Status.BAD_REQUEST);
            }
            if (license.trim().equals("")) {
                return new Response("License must not be empty", Status.BAD_REQUEST);
            }
            if (office.trim().equals("")) {
                return new Response("Office number must not be empty", Status.BAD_REQUEST);
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

            String licenseRegex = "^L-\\d{10} MTL$";
            if (!license.trim().matches(licenseRegex)) {
                return new Response("Not a valid license number", Status.BAD_REQUEST);
            }

            String officeRegex = "^O-\\d{3}$";
            if (!office.trim().matches(officeRegex)) {
                return new Response("Not a valid office number", Status.BAD_REQUEST);
            }

            if (!password.equals(comPassword)) {
                return new Response("Password and password comfirmation must be the same", Status.BAD_REQUEST);
            }

            storage.updateDoctor(username, firstname, lastname, password, dSpecialty, license, office);
            return new Response("Doctor updated successfully", Status.OK);

        } catch (Exception e) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }
}
