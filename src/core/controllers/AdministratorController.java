/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.model.DataRepository;
import core.model.Doctor;
import core.model.Patient;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author adria
 */
public class AdministratorController {

    public static Response loadAllDoctors() {
        DataRepository storage = DataRepository.getInstance();
        HashMap<String, Object> doctoresMap = new HashMap<>();
        for (Doctor doc : storage.getDoctors()) {
            String idStr = String.valueOf(doc.getId());
            String nombreCompleto = "Dr. " + doc.getFirstname() + " " + doc.getLastname();
            doctoresMap.put(idStr, nombreCompleto);
        }
        return new Response("Doctors loaded successfully", Status.OK, doctoresMap);
    }

    public static Response loadAllPatient() {
        DataRepository storage = DataRepository.getInstance();
        ArrayList<Patient> ids = storage.getPatients();
        HashMap<String, Object> patientIdMap = new HashMap<>();

        for (Patient p : ids) {
            String id = "" + p.getId();
            String nombreCompleto = "Dr. " + p.getFirstname() + " " + p.getLastname();
            patientIdMap.put(id, nombreCompleto);
        }

        return new Response("Patients loaded sucessfully", Status.OK, patientIdMap);

    }

    public static Response returnDoctorUsernameToChangeView(String id) {

        try {
            long longId;
            try {
                longId = Long.parseLong(id);

            } catch (NumberFormatException e) {
                return new Response("Not a valid Id", Status.BAD_REQUEST);
            }

            DataRepository storage = DataRepository.getInstance();
            Doctor d = storage.findDoctorById(longId);

            if (d == null) {
                return new Response("Doctor not found", Status.BAD_REQUEST);
            }

            HashMap<String, Object> username = new HashMap<>();
            String stringUsername = d.getUsername();
            username.put(id, stringUsername);

            return new Response("Returned doctor's username", Status.OK, username);

        } catch (Exception e) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response returnPatientUsernameToChangeView(String id) {

        try {
            long longId;
            try {
                longId = Long.parseLong(id);

            } catch (NumberFormatException e) {
                return new Response("Not a valid Id", Status.BAD_REQUEST);
            }

            DataRepository storage = DataRepository.getInstance();
            Patient p = storage.findPatientById(longId);
            if (p == null) {
                return new Response("Patient not found", Status.BAD_REQUEST);
            }

            HashMap<String, Object> username = new HashMap<>();
            String stringUsername = p.getUsername();
            username.put(id, stringUsername);

            return new Response("Returned doctor's username", Status.OK, username);

        } catch (Exception e) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }
}
