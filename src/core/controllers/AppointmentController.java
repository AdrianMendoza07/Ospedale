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

        return new Response("Specialties loaded sucessfully", Status.OK, especialidadesMap);

    }

    public static Response loadDoctors() {
        DataRepository storage = DataRepository.getInstance();

        // Creamos el mapa que viajará a la vista: <String ID, String Nombre>
        HashMap<String, Object> doctoresMap = new HashMap<>();

        // Recorremos los doctores reales del repositorio
        for (Doctor doc : storage.getDoctors()) {
            String idStr = String.valueOf(doc.getId());
            String nombreCompleto = "Dr. " + doc.getFirstname() + " " + doc.getLastname();

            doctoresMap.put(idStr, nombreCompleto);
        }

        return new Response("Doctors loaded sucessfully", Status.OK, doctoresMap);
    }

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
                case "ORTHOPEDICS" ->
                    Specialty.ORTHOPEDICS;
                case "GYNECOLOGY" ->
                    Specialty.GYNECOLOGY;

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
            //Traemos la instancia del storage, buscamos al paciente correspondiente
            DataRepository storage = DataRepository.getInstance();
            Patient p = storage.getPatientByUsername(username);

            //Buscamos si existe un doctor valido
            Doctor d = storage.findAvailableDoctorBySpecialty(typeSpecialty, datetime);
            if (d == null) {
                return new Response("No hay doctores disponibles con esa especialidad en esa fecha", Status.BAD_REQUEST);
            }

            //Generamos el Id de la cita
            long patientId = p.getId();
            int consecutivo = storage.getNextAppointmentConsecutive(patientId);
            String appointmentId = String.format("A-%d-%04d", patientId, consecutivo);

            Appointment appointment = new Appointment(appointmentId, p, d, typeSpecialty, datetime, reason, booleanType);
            p.addAppointment(appointment);
            return new Response("Appointment created succesfully", Status.CREATED);

        } catch (Exception e) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }

    }

    //
    public static Response createAppointmentByDoctor(String username, String doctorId, String appointmentDate, String AppointmentTime, String reason, String type) {

        try {
            LocalDate date;
            LocalTime time;
            LocalDateTime datetime;
            Boolean booleanType;
            long longDoctorId;

            //Revisamos fecha valida
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
            //Traemos la instancia del storage, buscamos al paciente correspondiente
            DataRepository storage = DataRepository.getInstance();
            Patient p = storage.getPatientByUsername(username);
            Doctor d = storage.findDoctorById(longDoctorId);

            //Buscamos si existe un doctor valido
            if (!storage.isDoctorAvailableById(longDoctorId, datetime)) {

                return new Response("Este doctor no esta disponible en esta fecha", Status.BAD_REQUEST);

            }

            //Generamos el Id de la cita
            long patientId = p.getId();
            int consecutivo = storage.getNextAppointmentConsecutive(patientId);
            String appointmentId = String.format("A-%d-%04d", patientId, consecutivo);

            Appointment appointment = new Appointment(appointmentDate, p, d, d.getSpecialty(), datetime, reason, booleanType);
            p.addAppointment(appointment);
            return new Response("Appointment created succesfully", Status.CREATED);

        } catch (Exception e) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }

    }

    public static Response loadTotalAppointmentsToDoctor(String username) {

        DataRepository storage = DataRepository.getInstance();
        Doctor d = storage.getDoctorByUsername(username);

        HashMap<String, Object> appointmentMap = new HashMap<>();

        for (Appointment ap : d.getAppointments()) {
            String id = ap.getId();
            LocalDateTime date = ap.getDatetime();
            String patient = ap.getPatient().getFirstname() + " " + ap.getPatient().getLastname();
            String specialty = "" + ap.getSpecialty();
            String type;
            if (ap.isType()) {
                type = "Remote";
            } else {
                type = "In-person";
            }
            String status = "" + ap.getStatus();

            String[] rowData = new String[]{
                id, // Columna 1:  ID
                String.valueOf(date), // Columna 2: Date
                patient, // Columna 3: Patient name
                specialty, // Columna 4: Specialty
                type, // Columna 5: Type
                status // Columna 6: Status
            };

            appointmentMap.put(id, rowData);

        }

        return new Response("Appointments loaded sucessfully", Status.OK, appointmentMap);

    }

    public static Response loadPendingAppointmentsToDoctor(String username) {

        DataRepository storage = DataRepository.getInstance();
        Doctor d = storage.getDoctorByUsername(username);

        HashMap<String, Object> appointmentMap = new HashMap<>();

        for (Appointment ap : d.getAppointments()) {
            if (ap.getStatus() == AppointmentStatus.PENDING) {
                String id = ap.getId();
                LocalDateTime date = ap.getDatetime();
                String patient = ap.getPatient().getFirstname() + " " + ap.getPatient().getLastname();
                String specialty = "" + ap.getSpecialty();
                String type;
                if (ap.isType()) {
                    type = "Remote";
                } else {
                    type = "In-person";
                }
                String status = "" + ap.getStatus();

                String[] rowData = new String[]{
                    id, // Columna 1:  ID
                    String.valueOf(date), // Columna 2: Date
                    patient, // Columna 3: Patient name
                    specialty, // Columna 4: Specialty
                    type, // Columna 5: Type
                    status // Columna 6: Status
                };

                appointmentMap.put(id, rowData);
            }

        }

        return new Response("Appointments loaded sucessfully", Status.OK, appointmentMap);

    }

    public static Response loadPatientAppointmentHistoryForDoctorView(String patientId) {

        long longId;

        try {
            longId = Long.parseLong(patientId);
        } catch (NumberFormatException e) {
            return new Response("Not a valid Id", Status.BAD_REQUEST);
        }

        DataRepository storage = DataRepository.getInstance();
        Patient p = storage.findPatientById(longId);

        HashMap<String, Object> appointmentMap = new HashMap<>();

        for (Appointment ap : p.getAppointments()) {
            String apId = ap.getId();
            LocalDateTime date = ap.getDatetime();
            String doctor = ap.getDoctor().getFirstname() + "" + ap.getDoctor().getFirstname();
            String specialty = "" + ap.getSpecialty();
            String type;
            if (ap.isType()) {
                type = "Remote";
            } else {
                type = "In-person";
            }
            String status = "" + ap.getStatus();
            String[] rowData = new String[]{
                apId, // Columna 1:  ID
                String.valueOf(date), // Columna 2: Date
                doctor, // Columna 3: Doctor name
                specialty, // Columna 4: Specialty
                type, // Columna 5: Type
                status // Columna 6: Status
            };

            appointmentMap.put(apId, rowData);

        }
        return new Response("Appointments loaded sucessfully", Status.OK, appointmentMap);
    }
    
    public static Response loadPatientAppointmentHistoryForPatientView(String username) {

        

        DataRepository storage = DataRepository.getInstance();
        Patient p = storage.getPatientByUsername(username);

        HashMap<String, Object> appointmentMap = new HashMap<>();

        for (Appointment ap : p.getAppointments()) {
            String apId = ap.getId();
            LocalDateTime date = ap.getDatetime();
            String doctor = ap.getDoctor().getFirstname() + "" + ap.getDoctor().getFirstname();
            String specialty = "" + ap.getSpecialty();
            String type;
            if (ap.isType()) {
                type = "Remote";
            } else {
                type = "In-person";
            }
            String status = "" + ap.getStatus();
            String[] rowData = new String[]{
                apId, // Columna 1:  ID
                String.valueOf(date), // Columna 2: Date
                doctor, // Columna 3: Doctor name
                specialty, // Columna 4: Specialty
                type, // Columna 5: Type
                status // Columna 6: Status
            };

            appointmentMap.put(apId, rowData);

        }
        return new Response("Appointments loaded sucessfully", Status.OK, appointmentMap);
    }

    public static Response loadPendingPatientAppointments(String username) {
        DataRepository storage = DataRepository.getInstance();
        Patient p = storage.getPatientByUsername(username);

        HashMap<String, Object> appointmentMap = new HashMap<>();

        for (Appointment ap : p.getAppointments()) {
            if (ap.getStatus() == AppointmentStatus.PENDING) {
                String apId = ap.getId();

                appointmentMap.put(apId, apId);
            }

        }

        return new Response("Appointment ids loaded sucessfully", Status.OK, appointmentMap);
    }
    
    public static Response cancelAppointment(String username, String apId, String observations){
        
        try {
            
            Response r = AppointmentController.loadPendingPatientAppointments(username);
            HashMap<String, Object> pendingAp = r.getData();
            
            if(!pendingAp.containsKey(apId)){
                return new Response("Not a valid id", Status.BAD_REQUEST);
            }
            
            if(observations.trim().equals("")){
                return new Response("Observations can not be empty",Status.BAD_REQUEST);
            }
            
            DataRepository storage = DataRepository.getInstance();
            storage.getAppointment(apId).setStatus(AppointmentStatus.CANCELED);
            
            return new Response("Appointment cancelled sucessfully", Status.OK);
            
        } catch (Exception e) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
        
    }
}
