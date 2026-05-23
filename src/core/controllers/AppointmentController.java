/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.model.Specialty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

/**
 *
 * @author adria
 */
public class AppointmentController {

    public static Response createResponse(String specialty, String appointmentDate, String AppointmentTime, String reason, String type) {

        try {
            Specialty typeSpecialty;
            LocalDate date;
            LocalTime time;
            LocalDateTime datetime;
            boolean booleanType;

            typeSpecialty = switch (specialty.trim()) {
                case "GENERAL_MEDICINE" ->
                    Specialty.GENERAL_MEDICINE;
                case "CARDIOLOGY" ->
                    Specialty.CARDIOLOGY;
                case "PEDIATRICS" ->
                    Specialty.PEDIATRICS;
                case "NEUROLOGY" ->
                    Specialty.NEUROLOGY;
                case "TRAUMATOLOGY_ORTHOPEDICS" ->
                    Specialty.TRAUMATOLOGY_ORTHOPEDICS;
                case "GYNECOLOGY_OBSTETRICS" ->
                    Specialty.GYNECOLOGY_OBSTETRICS;
                case "DERMATOLOGY" ->
                    Specialty.DERMATOLOGY;
                case "PSYCHIATRY" ->
                    Specialty.PSYCHIATRY;
                case "ONCOLOGY" ->
                    Specialty.ONCOLOGY;
                case "OPHTHALMOLOGY" ->
                    Specialty.OPHTHALMOLOGY;
                case "INTERNAL_MEDICINE" ->
                    Specialty.INTERNAL_MEDICINE;

                default ->
                    null;

            };

            if (typeSpecialty == null) {
                return new Response("Debe seleccionar una especialidad médica válida.", Status.BAD_REQUEST);
            }

            try {
                date = LocalDate.parse(appointmentDate);

            } catch (DateTimeParseException e) {
                return new Response("Not a valid date", Status.BAD_REQUEST);
            }

        } catch (Exception e) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }

    }
}
