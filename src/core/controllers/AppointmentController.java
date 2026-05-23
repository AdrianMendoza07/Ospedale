/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.model.Appointment;
import core.model.DataRepository;
import core.model.Doctor;
import core.model.Patient;
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

    /**
     *
     * @param username
     * @param specialty
     * @param appointmentDate
     * @param AppointmentTime
     * @param reason
     * @param type
     * @return
     */
    public static Response createAppointmentBySpecialty(String username, String specialty, String appointmentDate, String AppointmentTime, String reason, String type) {

        try {
            Specialty typeSpecialty;
            LocalDate date;
            LocalTime time;
            LocalDateTime datetime;
            Boolean booleanType;

            //Parseamos la especialidad a Specialty
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
            //Revisamos que se haya elegido especialidad
            if (typeSpecialty == null) {
                return new Response("Debe seleccionar una especialidad médica válida.", Status.BAD_REQUEST);
            }

            //Revisamos fecha valida
            try {
                date = LocalDate.parse(appointmentDate);

            } catch (DateTimeParseException e) {
                return new Response("Not a valid date", Status.BAD_REQUEST);
            }

            try {
                time = LocalTime.parse(AppointmentTime);

                int minuto = time.getMinute();
                if (minuto % 15 != 0) {
                    return new Response("Appointments must be booked in quarters of the hour (00,15,30,45)", Status.BAD_REQUEST);
                }
            } catch (Exception e) {
                return new Response("Not a valid time", Status.BAD_REQUEST);

            }

            if (reason.trim().equals("")) {
                return new Response("Reason can not be empty", Status.BAD_REQUEST);
            }

            booleanType = switch (type) {
                case "Remote" ->
                    true;
                case "In-Person" ->
                    false;

                default ->
                    null;

            };

            if (booleanType == null) {
                return new Response("Must choose a type", Status.BAD_REQUEST);
            }
            //Traemos la instancia del storage, buscamos al paciente correspondiente
            DataRepository storage = DataRepository.getInstance();
            Patient p = storage.getPatientByUsername(username);

            //Buscamos si existe un doctor valido
            Doctor d = storage.getDoctorBySpecialty(typeSpecialty, date);
            if (d == null) {
                return new Response("No hay doctores disponibles con esa especialidad en esa fecha", Status.BAD_REQUEST);
            }

            //Generamos el Id de la cita
            long patientId = p.getId();
            int consecutivo = storage.getNextAppointmentConsecutive(patientId);
            String appointmentId = String.format("A-%d-%04d", patientId, consecutivo);

            Appointment appointment = new Appointment(appointmentDate, p, d, typeSpecialty, datetime, reason, booleanType);
            p.addAppointment(appointment);
            return new Response("Appointment created succesfully", Status.CREATED);

        } catch (Exception e) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }

    }

    public static Response createAppointmentByDoctor(String username, String doctor, String appointmentDate, String AppointmentTime, String reason, String type) {

        try {
            Specialty typeSpecialty;
            LocalDate date;
            LocalTime time;
            LocalDateTime datetime;
            Boolean booleanType;

            //Parseamos la especialidad a Specialty
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
            //Revisamos que se haya elegido especialidad
            if (typeSpecialty == null) {
                return new Response("Debe seleccionar una especialidad médica válida.", Status.BAD_REQUEST);
            }

            //Revisamos fecha valida
            try {
                date = LocalDate.parse(appointmentDate);

            } catch (DateTimeParseException e) {
                return new Response("Not a valid date", Status.BAD_REQUEST);
            }

            try {
                time = LocalTime.parse(AppointmentTime);

                int minuto = time.getMinute();
                if (minuto % 15 != 0) {
                    return new Response("Appointments must be booked in quarters of the hour (00,15,30,45)", Status.BAD_REQUEST);
                }
            } catch (Exception e) {
                return new Response("Not a valid time", Status.BAD_REQUEST);

            }

            if (reason.trim().equals("")) {
                return new Response("Reason can not be empty", Status.BAD_REQUEST);
            }

            booleanType = switch (type) {
                case "Remote" ->
                    true;
                case "In-Person" ->
                    false;

                default ->
                    null;

            };

            if (booleanType == null) {
                return new Response("Must choose a type", Status.BAD_REQUEST);
            }
            //Traemos la instancia del storage, buscamos al paciente correspondiente
            DataRepository storage = DataRepository.getInstance();
            Patient p = storage.getPatientByUsername(username);

            //Buscamos si existe un doctor valido
            Doctor d = storage.getDoctorBySpecialty(typeSpecialty, date);
            if (d == null) {
                return new Response("No hay doctores disponibles con esa especialidad en esa fecha", Status.BAD_REQUEST);
            }

            //Generamos el Id de la cita
            long patientId = p.getId();
            int consecutivo = storage.getNextAppointmentConsecutive(patientId);
            String appointmentId = String.format("A-%d-%04d", patientId, consecutivo);

            Appointment appointment = new Appointment(appointmentDate, p, d, typeSpecialty, datetime, reason, booleanType);
            p.addAppointment(appointment);
            return new Response("Appointment created succesfully", Status.CREATED);

        } catch (Exception e) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }

    }

}
