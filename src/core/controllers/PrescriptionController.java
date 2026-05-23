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
import core.model.JsonManager;
import core.model.prescription;
import java.util.HashMap;

/**
 *
 * @author adria
 */
public class PrescriptionController {

    public static Response loadAppointments(String username) {

        DataRepository storage = DataRepository.getInstance();
        Doctor d = storage.getDoctorByUsername(username);
        HashMap<String, Object> appointmentsMap = new HashMap<>();

        for (Appointment a : d.getAppointments()) {
            if (a.getStatus() == AppointmentStatus.PENDING) {
                String aId = a.getId();

                appointmentsMap.put(aId, aId);
            }

        }

        return new Response("Appointments loaded sucessfully", Status.OK, appointmentsMap);
    }

    public static Response createPrescription(String username, String appointmentId, String name, String dose, String administrationRoute, String duration, String instructions, String frecuency) {
        try {
            double dDose;
            int intDuration, intFrecuency;

            if (name.trim().equals("")) {
                return new Response("Medication name must not be empty", Status.BAD_REQUEST);
            }

            if (dose == null || dose.trim().equals("")) {
                return new Response("Dose must not be empty", Status.BAD_REQUEST);
            }

            try {
                dDose = Double.parseDouble(dose);
            } catch (NumberFormatException e) {
                return new Response("Not a valid number", Status.BAD_REQUEST);
            }

            if (administrationRoute == null || administrationRoute.trim().equals("")) {
                return new Response("Administration route must not be empty", Status.BAD_REQUEST);
            }

            if (duration == null || duration.trim().equals("")) {
                return new Response("Duration must not be empty", Status.BAD_REQUEST);
            }
            try {
                intDuration = Integer.parseInt(duration);
            } catch (NumberFormatException e) {
                return new Response("Not a valid number", Status.BAD_REQUEST);
            }

            if (instructions == null || instructions.trim().equals("")) {
                return new Response("Instructions must not be empty", Status.BAD_REQUEST);
            }

            if (frecuency == null || frecuency.trim().equals("")) {
                return new Response("Frecuency must not be empty", Status.BAD_REQUEST);
            }
            try {
                intFrecuency = Integer.parseInt(frecuency);
            } catch (NumberFormatException e) {
                return new Response("Not a valid number", Status.BAD_REQUEST);
            }

            DataRepository storage = DataRepository.getInstance();
            Appointment ap = storage.getAppointment(appointmentId);
            
            if (ap == null) {
                return new Response("The selected appointment does not exist.", Status.NOT_FOUND);
            }
            
            prescription p = new prescription(ap, name, dDose, administrationRoute, intDuration, instructions, intFrecuency);
            storage.addPrescription(p);
            
            JsonManager jsonManager = new JsonManager(storage);
            jsonManager.saveAllDataToJson("json/users.json");
            
            return new Response("Prescription created sucessfully", Status.CREATED);

        } catch (Exception e) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response loadAllPrescriptions(String username) {
        DataRepository storage = DataRepository.getInstance();
        Doctor d = storage.getDoctorByUsername(username);

        HashMap<String, Object> prescriptionsMap = new HashMap<>();

        for (Appointment ap : d.getAppointments()) {
            String apId = ap.getId();
            for (prescription p : ap.getPrescriptions()) {
                String medName = p.getMedicationName();
                Double dose = p.getDose();
                String route = p.getAdministrationRoute();
                int duration = p.getTreatmentDuration();
                String instructions = p.getAdditionalInstructions();
                int frecuency = p.getFrecuency();

                String pId = apId + "-" + medName;

                
                String[] rowData = new String[]{
                    apId, // Columna 1: Appointment ID
                    medName, // Columna 2: Medication name
                    String.valueOf(dose), // Columna 3: Dose
                    route, // Columna 4: Administration route
                    String.valueOf(duration), // Columna 5: Treatment duration
                    instructions, // Columna 6: Additional instructions
                    String.valueOf(frecuency) // Columna 7: Frecuency
                };

                prescriptionsMap.put(pId, rowData);

            }
        }
        
        return new Response("Prescriptions loaded sucessfully", Status.OK, prescriptionsMap);
    }
}
