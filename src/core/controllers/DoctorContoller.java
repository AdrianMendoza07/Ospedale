/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.model.DataRepository;
import core.model.Doctor;
import core.model.JsonManager;
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
            Specialty typeSpecialty;

            if (id == null || id.trim().isEmpty()) {
                return new Response("Id must not be empty", Status.BAD_REQUEST);
            }
            if (username == null || username.trim().isEmpty()) {
                return new Response("Username must not be empty", Status.BAD_REQUEST);
            }
            if (firstname == null || firstname.trim().isEmpty()) {
                return new Response("Firstname must not be empty", Status.BAD_REQUEST);
            }
            if (lastname == null || lastname.trim().isEmpty()) {
                return new Response("Lastname must not be empty", Status.BAD_REQUEST);
            }
            if (password == null || password.trim().isEmpty()) {
                return new Response("Password must not be empty", Status.BAD_REQUEST);
            }
            if (comPassword == null || comPassword.trim().isEmpty()) {
                return new Response("Password confirmation must not be empty", Status.BAD_REQUEST);
            }
            if (inSpecialty == null || inSpecialty.trim().equals("Select one") || inSpecialty.trim().isEmpty()) {
                return new Response("Must choose an option for specialty", Status.BAD_REQUEST);
            }
            if (license == null || license.trim().isEmpty()) {
                return new Response("License must not be empty", Status.BAD_REQUEST);
            }
            if (office == null || office.trim().isEmpty()) {
                return new Response("Office number must not be empty", Status.BAD_REQUEST);
            }

            typeSpecialty = switch (inSpecialty.trim().toUpperCase()) {
                case "GENERALMEDICINE" ->
                    Specialty.GENERAL_MEDICINE;
                case "CARDIOLOGY" ->
                    Specialty.CARDIOLOGY;
                case "PEDIATRICS" ->
                    Specialty.PEDIATRICS;
                case "NEUROLOGY" ->
                    Specialty.NEUROLOGY;
                case "TRAUMATOLOGY&ORTHOPEDICS" ->
                    Specialty.TRAUMATOLOGY_ORTHOPEDICS;
                case "GYNECOLOGY&OBSTETRICS" ->
                    Specialty.GYNECOLOGY_OBSTETRICS;
                case "DERMATOLOGY" ->
                    Specialty.DERMATOLOGY;
                case "PSYCHIATRY" ->
                    Specialty.PSYCHIATRY;
                case "ONCOLOGY" ->
                    Specialty.ONCOLOGY;
                case "OPHTHALMOLOGY" ->
                    Specialty.OPHTHALMOLOGY;
                case "INTERNALMEDICINE" ->
                    Specialty.INTERNAL_MEDICINE;
                default ->
                    null;
            };

            if (typeSpecialty == null) {
                return new Response("Debe seleccionar una especialidad médica válida.", Status.BAD_REQUEST);
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
            if (!storage.addDoctor(longId, username, firstname, lastname, password, license, typeSpecialty, office)) {
                return new Response("Doctor with those cretentials already exists", Status.BAD_REQUEST);
            }

            JsonManager jsonManager = new JsonManager(storage);
            jsonManager.saveAllDataToJson("json/users.json");

            return new Response("Doctor created successfully", Status.CREATED);

        } catch (Exception e) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response updateDoctor(String username, String newUsername, String firstname, String lastname, String password, String comPassword, String inSpecialty, String license, String office) {

        try {
            Specialty dSpecialty;

            if (username == null || username.trim().equals("")) {
                return new Response("Username must not be empty", Status.BAD_REQUEST);
            }

            DataRepository storage = DataRepository.getInstance();
            Doctor d = storage.getDoctorByUsername(username);
            if (d == null) {
                return new Response("User not found", Status.NOT_FOUND);
            }

            if (newUsername == null || newUsername.trim().isEmpty()) {
                return new Response("Username must not be empty", Status.BAD_REQUEST);
            }

            if (firstname == null || firstname.trim().isEmpty()) {
                return new Response("Firstname must not be empty", Status.BAD_REQUEST);
            }
            if (lastname == null || lastname.trim().isEmpty()) {
                return new Response("Lastname must not be empty", Status.BAD_REQUEST);
            }
            if (password == null || password.trim().isEmpty()) {
                return new Response("Password must not be empty", Status.BAD_REQUEST);
            }
            if (comPassword == null || comPassword.trim().isEmpty()) {
                return new Response("Password confirmation must not be empty", Status.BAD_REQUEST);
            }
            if (inSpecialty == null || inSpecialty.trim().equals("Select one") || inSpecialty.trim().isEmpty()) {
                return new Response("Must choose a valid specialty option", Status.BAD_REQUEST);
            }
            if (license == null || license.trim().isEmpty()) {
                return new Response("License must not be empty", Status.BAD_REQUEST);
            }
            if (office == null || office.trim().isEmpty()) {
                return new Response("Office number must not be empty", Status.BAD_REQUEST);
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

            storage.updateDoctor(newUsername, firstname, lastname, password, dSpecialty, license, office);

            JsonManager jsonManager = new JsonManager(storage);
            jsonManager.saveAllDataToJson("json/users.json");

            return new Response("Doctor updated successfully", Status.OK);

        } catch (Exception e) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }
}
