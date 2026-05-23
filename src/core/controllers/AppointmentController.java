/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.model.Appointment;
import core.model.AppointmentStatus;
import core.model.DataRepository;
import core.model.Doctor;
import core.model.Patient;
import core.model.Specialty;
import core.model.JsonManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;

/**
 *
 * @author adria
 */
public class AppointmentController {

    public static Response getAllSpecialties() {
        HashMap<String, Object> especialidadesMap = new HashMap<>();
        for (Specialty esp : Specialty.values()) {
            String nombreEspecialidad = esp.name();
            especialidadesMap.put(nombreEspecialidad, nombreEspecialidad);
        }
        return new Response("Specialties loaded successfully", Status.OK, especialidadesMap);
    }

    public static Response loadDoctors() {
        DataRepository storage = DataRepository.getInstance();
        HashMap<String, Object> doctoresMap = new HashMap<>();
        for (Doctor doc : storage.getDoctors()) {
            String idStr = String.valueOf(doc.getId());
            String nombreCompleto = "Dr. " + doc.getFirstname() + " " + doc.getLastname();
            doctoresMap.put(idStr, nombreCompleto);
        }
        return new Response("Doctors loaded successfully", Status.OK, doctoresMap);
    }

    public static Response createAppointmentBySpecialty(String username, String specialty, String appointmentDate, String AppointmentTime, String reason, String type) {
        try {
            Specialty typeSpecialty;
            LocalDate date;
            LocalTime time;
            LocalDateTime datetime;
            Boolean booleanType;

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

            try {
                time = LocalTime.parse(AppointmentTime);
                int minuto = time.getMinute();
                if (minuto % 15 != 0) {
                    return new Response("Appointments must be booked in quarters of the hour (00,15,30,45)", Status.BAD_REQUEST);
                }
            } catch (Exception e) {
                return new Response("Not a valid time", Status.BAD_REQUEST);
            }

            datetime = LocalDateTime.of(date, time);

            if (reason.trim().equals("")) {
                return new Response("Reason can not be empty", Status.BAD_REQUEST);
            }

            booleanType = switch (type.trim()) {
                case "Remote" ->
                    true;
                case "In-person" ->
                    false;
                default ->
                    null;
            };

            if (booleanType == null) {
                return new Response("Must choose a type", Status.BAD_REQUEST);
            }

            DataRepository storage = DataRepository.getInstance();
            Patient p = storage.getPatientByUsername(username);

            Doctor d = storage.findAvailableDoctorBySpecialty(typeSpecialty, datetime);
            if (d == null) {
                return new Response("No hay doctores disponibles con esa especialidad en esa fecha", Status.BAD_REQUEST);
            }

            long patientId = p.getId();
            int consecutivo = storage.getNextAppointmentConsecutive(patientId);
            String appointmentId = String.format("A-%d-%04d", patientId, consecutivo);

            Appointment appointment = new Appointment(appointmentId, p, d, typeSpecialty, datetime, reason, booleanType);

            p.addAppointment(appointment);
            d.addAppointment(appointment);
            storage.addAppointment(appointment);
            
            JsonManager jsonManager = new JsonManager(storage);
            jsonManager.saveAllDataToJson("json/users.json");

            return new Response("Appointment created successfully", Status.CREATED);

        } catch (Exception e) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response createAppointmentByDoctor(String username, String doctorId, String appointmentDate, String AppointmentTime, String reason, String type) {
        try {
            LocalDate date;
            LocalTime time;
            LocalDateTime datetime;
            Boolean booleanType;
            long longDoctorId;

            try {
                date = LocalDate.parse(appointmentDate);
            } catch (DateTimeParseException e) {
                return new Response("Not a valid date", Status.BAD_REQUEST);
            }

            try {
                longDoctorId = Long.parseLong(doctorId.trim());
            } catch (NumberFormatException e) {
                return new Response("Not a valid ID", Status.BAD_REQUEST);
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

            datetime = LocalDateTime.of(date, time);

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

            DataRepository storage = DataRepository.getInstance();
            Patient p = storage.getPatientByUsername(username);
            Doctor d = storage.findDoctorById(longDoctorId);

            if (!storage.isDoctorAvailableById(longDoctorId, datetime)) {
                return new Response("Este doctor no esta disponible en esta fecha", Status.BAD_REQUEST);
            }

            long patientId = p.getId();
            int consecutivo = storage.getNextAppointmentConsecutive(patientId);
            String appointmentId = String.format("A-%d-%04d", patientId, consecutivo);

            Appointment appointment = new Appointment(appointmentId, p, d, d.getSpecialty(), datetime, reason, booleanType);

            p.addAppointment(appointment);
            d.addAppointment(appointment);
            storage.addAppointment(appointment);
            
            JsonManager jsonManager = new JsonManager(storage);
            jsonManager.saveAllDataToJson("json/users.json");

            return new Response("Appointment created successfully", Status.CREATED);

        } catch (Exception e) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response acceptAppointment(String appointmentId) {
        try {
            if (appointmentId == null || appointmentId.trim().isEmpty() || appointmentId.equals("Select one")) {
                return new Response("Debe seleccionar un ID de cita válido.", Status.BAD_REQUEST);
            }

            DataRepository storage = DataRepository.getInstance();
            Appointment targetAppointment = null;
            for (Appointment app : storage.getAppointments()) {
                if (app.getId().equals(appointmentId.trim())) {
                    targetAppointment = app;
                    break;
                }
            }

            if (targetAppointment == null) {
                return new Response("La cita médica especificada no existe.", Status.NOT_FOUND);
            }

            if (targetAppointment.getStatus() != AppointmentStatus.REQUESTED) {
                return new Response("Esta cita ya ha sido aceptada previamente.", Status.BAD_REQUEST);
            }

            targetAppointment.setStatus(AppointmentStatus.PENDING);

            JsonManager jsonManager = new JsonManager(storage);
            jsonManager.saveAllDataToJson("json/users.json");

            return new Response("Cita aceptada con éxito y sincronizada.", Status.OK);

        } catch (Exception e) {
            return new Response("Error al aceptar la cita: " + e.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response rescheduleAppointment(String appointmentId, String newDateStr, String newTimeStr, String newReason) {
        try {
            if (appointmentId == null || appointmentId.trim().isEmpty() || appointmentId.equals("Select one")) {
                return new Response("Debe seleccionar una cita válida.", Status.BAD_REQUEST);
            }
            if (newReason == null || newReason.trim().isEmpty()) {
                return new Response("El motivo de la reprogramación no puede estar vacío.", Status.BAD_REQUEST);
            }

            LocalDate date;
            LocalTime time;
            try {
                date = LocalDate.parse(newDateStr.trim());
            } catch (DateTimeParseException e) {
                return new Response("La nueva fecha no tiene un formato válido (AAAA-MM-DD).", Status.BAD_REQUEST);
            }

            try {
                time = LocalTime.parse(newTimeStr.trim());
                if (time.getMinute() % 15 != 0) {
                    return new Response("Las citas deben agendarse en cuartos de hora (00, 15, 30, 45).", Status.BAD_REQUEST);
                }
            } catch (Exception e) {
                return new Response("La nueva hora no tiene un formato válido (HH:MM).", Status.BAD_REQUEST);
            }

            LocalDateTime newDateTime = LocalDateTime.of(date, time);

            DataRepository storage = DataRepository.getInstance();
            Appointment target = null;
            for (Appointment app : storage.getAppointments()) {
                if (app.getId().equals(appointmentId.trim())) {
                    target = app;
                    break;
                }
            }

            if (target == null) {
                return new Response("La cita especificada no existe.", Status.NOT_FOUND);
            }

            long doctorId = target.getDoctor().getId();
            for (Appointment app : storage.getAppointments()) {
                if (!app.getId().equals(target.getId()) && app.getDoctor() != null && app.getDoctor().getId() == doctorId) {
                    if (app.getDatetime().equals(newDateTime)) {
                        return new Response("El doctor no está disponible en la nueva fecha y hora elegida.", Status.BAD_REQUEST);
                    }
                }
            }

            target.setDatetime(newDateTime);
            target.setReason(newReason.trim());
            target.setStatus(AppointmentStatus.REQUESTED);

            JsonManager jsonManager = new JsonManager(storage);
            jsonManager.saveAllDataToJson("json/users.json");

            return new Response("Cita reprogramada con éxito.", Status.OK);

        } catch (Exception e) {
            return new Response("Error al reprogramar la cita: " + e.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response completeAppointment(String appointmentId, String diagnosis, String observations, String treatment, String followUp) {
        try {
            if (appointmentId == null || appointmentId.trim().isEmpty() || appointmentId.equals("Select one")) {
                return new Response("Debe seleccionar una cita para completarla.", Status.BAD_REQUEST);
            }
            if (diagnosis == null || diagnosis.trim().isEmpty()) {
                return new Response("El diagnóstico es obligatorio para cerrar la cita.", Status.BAD_REQUEST);
            }
            if (treatment == null || treatment.trim().isEmpty()) {
                return new Response("El tratamiento recomendado es obligatorio.", Status.BAD_REQUEST);
            }

            DataRepository storage = DataRepository.getInstance();
            Appointment target = null;
            for (Appointment app : storage.getAppointments()) {
                if (app.getId().equals(appointmentId.trim())) {
                    target = app;
                    break;
                }
            }

            if (target == null) {
                return new Response("La cita especificada no existe.", Status.NOT_FOUND);
            }

            target.setStatus(AppointmentStatus.COMPLETED);

            System.out.println("--- REGISTRO CLÍNICO ADJUNTO ---");
            System.out.println("Cita ID: " + appointmentId);
            System.out.println("Diagnóstico: " + diagnosis.trim());
            System.out.println("Tratamiento: " + treatment.trim());

            JsonManager jsonManager = new JsonManager(storage);
            jsonManager.saveAllDataToJson("json/users.json");

            return new Response("Cita completada y registro clínico almacenado con éxito.", Status.OK);

        } catch (Exception e) {
            return new Response("Error al completar la cita: " + e.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response cancelAppointment(String username, String appointmentId) {
        try {
            if (username == null || username.trim().isEmpty()) {
                return new Response("User session is not valid.", Status.BAD_REQUEST);
            }
            if (appointmentId == null || appointmentId.trim().isEmpty() || appointmentId.equals("Select one")) {
                return new Response("You must select a valid appointment ID to cancel.", Status.BAD_REQUEST);
            }

            DataRepository storage = DataRepository.getInstance();

            Patient p = storage.getPatientByUsername(username);
            if (p == null) {
                return new Response("Patient profile not found in system.", Status.NOT_FOUND);
            }

            Appointment targetAppointment = null;
            for (Appointment app : storage.getAppointments()) {
                if (app.getId().equals(appointmentId.trim())) {
                    targetAppointment = app;
                    break;
                }
            }

            if (targetAppointment == null) {
                return new Response("The specified appointment does not exist.", Status.NOT_FOUND);
            }

            if (targetAppointment.getStatus() == AppointmentStatus.COMPLETED) {
                return new Response("Cannot cancel an appointment that has already been completed.", Status.BAD_REQUEST);
            }

            if (targetAppointment.getStatus() == AppointmentStatus.CANCELED) {
                return new Response("This appointment is already cancelled.", Status.BAD_REQUEST);
            }

            targetAppointment.setStatus(AppointmentStatus.CANCELED);

            JsonManager jsonManager = new JsonManager(storage);
            jsonManager.saveAllDataToJson("json/users.json");

            return new Response("Appointment successfully cancelled and system updated.", Status.OK);

        } catch (Exception e) {
            return new Response("Error while cancelling the appointment: " + e.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }
}
