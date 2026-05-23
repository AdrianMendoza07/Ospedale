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
import core.model.Hospitalization;
import core.model.HospitalizationStatus;
import core.model.JsonManager;
import core.model.Patient;
import core.model.RoomType;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.DataFormatException;

/**
 *
 * @author adria
 */
public class HospitalizationController {

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

    public static Response getAllRoomType() {
        HashMap<String, Object> roomTypesMap = new HashMap<>();

        for (RoomType room : RoomType.values()) {
            String nombreTipo = room.name();

            roomTypesMap.put(nombreTipo, nombreTipo);
        }

        return new Response("Specialties loaded sucessfully", Status.OK, roomTypesMap);

    }

    public static Response loadHospRequestedForDoctor(String username) {
        DataRepository storage = DataRepository.getInstance();
        Doctor d = storage.getDoctorByUsername(username);

        if (d == null) {
            return new Response("Doctor doesnt exist", Status.BAD_REQUEST);
        }

        HashMap<String, Object> pendingHosp = new HashMap<>();

        for (Hospitalization h : d.getHospitalizations()) {
            if (h.getStatus() == HospitalizationStatus.REQUESTED) {
                String idHosp = h.getId();
                String hosp = "Patient: " + h.getPatient().getFirstname() + " " + h.getPatient().getLastname();

                pendingHosp.put(idHosp, hosp);
            }
        }

        return new Response("Hospitalizations loaded sucessfully", Status.OK, pendingHosp);

    }

    public static Response loadPatientIdForDoctor() {
        DataRepository storage = DataRepository.getInstance();
        ArrayList<Patient> ids = storage.getPatients();
        HashMap<String, Object> patientIdMap = new HashMap<>();
        
        for(Patient p : ids){
            String id = ""+p.getId();
            patientIdMap.put(id, id);
        }
        
        

        return new Response("Hospitalizations loaded sucessfully", Status.OK, patientIdMap);

    }

    public static Response createHospitalizationByDoctor(String username, String patientId, String incomingId, String reason, String hospDate, String hospDuration, String observations) {

        try {
            long longId;
            LocalDate date;
            LocalTime time;

            try {
                longId = Long.parseLong(patientId);
            } catch (NumberFormatException e) {
                return new Response("Not a valid Id", Status.BAD_REQUEST);
            }

            DataRepository storage = DataRepository.getInstance();
            Patient p = storage.findPatientById(longId);
            Doctor d = storage.getDoctorByUsername(username);

            if (d == null) {
                return new Response("This doctor does not exist", Status.BAD_REQUEST);
            }

            if (reason.trim().equals("")) {
                return new Response("Reason for hospitalization can not be empty", Status.BAD_REQUEST);
            }
            if (hospDate.trim().equals("")) {
                return new Response("Date can not be empty", Status.BAD_REQUEST);
            }
            if (hospDuration.trim().equals("")) {
                return new Response("Duration can not be empty", Status.BAD_REQUEST);
            }

            try {
                date = LocalDate.parse(hospDate);
            } catch (DateTimeParseException e) {
                return new Response("Not a valid date", Status.BAD_REQUEST);
            }

            if (observations.trim().equals("")) {
                return new Response("Observations can not be empty", Status.BAD_REQUEST);
            }

            int consecutivo = storage.getNextAppointmentConsecutive(longId);
            String hospitalizationId = String.format("H-%d-%04d", longId, consecutivo);

            Hospitalization h = new Hospitalization(hospitalizationId, p, d, date, reason, RoomType.STANDARD, observations, HospitalizationStatus.ONGOING);
            storage.addHospitalization(h);
            
            JsonManager jsonManager = new JsonManager(storage);
            jsonManager.saveAllDataToJson("json/users.json");
            
            return new Response("Hospitalization submited", Status.CREATED);

        } catch (Exception e) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response createHospitalizationByPatient(String username, String reason, String doctorId, String adminDate, String roomType, String observations) {

        try {
            long longdId;
            LocalDate date;
            RoomType parseRoom;

            try {
                longdId = Long.parseLong(doctorId);
            } catch (NumberFormatException e) {
                return new Response("Id must be a number", Status.BAD_REQUEST);

            }

            if (reason.trim().equals("")) {
                return new Response("Reason field must not be empty", Status.BAD_REQUEST);
            }

            DataRepository storage = DataRepository.getInstance();
            Patient p = storage.getPatientByUsername(username);
            Doctor d = storage.findDoctorById(longdId);

            if (d == null) {
                return new Response("This doctor does not exist", Status.BAD_REQUEST);
            }

            if (adminDate.trim().equals("")) {
                return new Response("Date must not be empty", Status.BAD_REQUEST);
            }

            try {
                date = LocalDate.parse(adminDate);
            } catch (DateTimeParseException e) {
                return new Response("Not a valid date", Status.BAD_REQUEST);

            }

            parseRoom = switch (roomType.trim()) {
                case "STANDARD" ->
                    RoomType.STANDARD;
                case "ICU" ->
                    RoomType.ICU;
                case "NICU" ->
                    RoomType.NICU;
                case "ISOLATION" ->
                    RoomType.ISOLATION;
                case "IMC" ->
                    RoomType.IMC;
                default ->
                    null;

            };

            if (parseRoom == null) {
                return new Response("Must choose a room type", Status.BAD_REQUEST);
            }

            if (observations.trim().equals("")) {
                return new Response("Observations can not be empty", Status.BAD_REQUEST);
            }

            long patientId = p.getId();
            int consecutivo = storage.getNextAppointmentConsecutive(patientId);
            String hospitalizationId = String.format("H-%d-%04d", patientId, consecutivo);

            Hospitalization h = new Hospitalization(hospitalizationId, p, d, date, reason, parseRoom, observations);
            storage.addHospitalization(h);
            
            JsonManager jsonManager = new JsonManager(storage);
            jsonManager.saveAllDataToJson("json/users.json");
            
            return new Response("Hospitalization request submited", Status.CREATED);

        } catch (Exception e) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }

    }
}
