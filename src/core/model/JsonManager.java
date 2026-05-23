/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.model;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 *
 * @author tefy1
 */
public class JsonManager {
    private DataRepository repository;

    public JsonManager(DataRepository repository) {
        this.repository = repository;
    }

    public void loadPatientsFromJson(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            JSONArray array = new JSONArray(new JSONTokener(reader));
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                long id = obj.getLong("id");
                String username = obj.getString("username");
                String firstname = obj.getString("firstname");
                String lastname = obj.getString("lastname");
                String password = obj.getString("password");
                String email = obj.getString("email");
                long phone = obj.getLong("phone");
                String address = obj.getString("address");
                LocalDate birthdate = LocalDate.parse(obj.getString("birthdate"));
                boolean gender = obj.getBoolean("gender");
                Patient patient = new Patient(id, username, firstname, lastname, password, email, birthdate, gender, phone, address);
                repository.loadPatient(patient);
            }
        } catch (Exception e) {
            System.out.println("Error al cargar pacientes desde JSON: " + e.getMessage());
        }
    }

    public void loadDoctorsFromJson(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            JSONArray array = new JSONArray(new JSONTokener(reader));
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                long id = obj.getLong("id");
                String username = obj.getString("username");
                String firstname = obj.getString("firstname");
                String lastname = obj.getString("lastname");
                String password = obj.getString("password");
                String licenceNumber = obj.getString("licenceNumber");
                String assignedOffice = obj.getString("assignedOffice");
                Specialty specialty = Specialty.valueOf(obj.getString("specialty").toUpperCase()); 
                Doctor doctor = new Doctor(id, username, firstname, lastname, password, licenceNumber, specialty, assignedOffice);
                repository.loadDoctor(doctor);
            }
        } catch (Exception e) {
            System.out.println("Error al cargar doctores desde JSON: " + e.getMessage());
        }
    }

    public void loadAppointmentsFromJson(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            JSONArray array = new JSONArray(new JSONTokener(reader));
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String id = obj.getString("id");
                long patientId = obj.getLong("patientId");
                long doctorId = obj.getLong("doctorId");
                String reason = obj.getString("reason");
                boolean type = obj.getBoolean("type");
                LocalDateTime datetime = LocalDateTime.parse(obj.getString("datetime"), formatter);
                AppointmentStatus status = AppointmentStatus.valueOf(obj.getString("status").toUpperCase());
                
                Patient patient = repository.findPatientById(patientId);
                Doctor doctor = repository.findDoctorById(doctorId);
                Specialty specialty = (doctor != null) ? doctor.getSpecialty() : null;
                
                if (patient != null && doctor != null) {
                    Appointment appointment = new Appointment(id, patient, doctor, specialty, datetime, reason, type);
                    appointment.setStatus(status);
                    
                    patient.addAppointment(appointment);
                    doctor.addAppointment(appointment);
                    repository.addAppointment(appointment);
                }
            }
        } catch (Exception e) {
            System.out.println("Error al cargar citas desde JSON: " + e.getMessage());
        }
    }

    public void savePatientsToJson(String filePath) {
        JSONArray array = new JSONArray();
        for (Patient p : repository.getPatients()) {
            JSONObject obj = new JSONObject();
            obj.put("id", p.getId());
            obj.put("username", p.getUsername());
            obj.put("firstname", p.getFirstname());
            obj.put("lastname", p.getLastname());
            obj.put("password", p.getPassword());
            obj.put("email", p.getEmail());
            obj.put("phone", p.getPhone());
            obj.put("address", p.getAddress());
            obj.put("birthdate", p.getBirthdate().toString());
            obj.put("gender", p.isGender());
            array.put(obj);
        }
        try (FileWriter writer = new FileWriter(filePath)) {
            array.write(writer, 4, 0);
        } catch (Exception e) {
            System.out.println("Error al guardar pacientes en JSON: " + e.getMessage());
        }
    }

    public void saveDoctorsToJson(String filePath) {
        JSONArray array = new JSONArray();
        for (Doctor d : repository.getDoctors()) {
            JSONObject obj = new JSONObject();
            obj.put("id", d.getId());
            obj.put("username", d.getUsername());
            obj.put("firstname", d.getFirstname());
            obj.put("lastname", d.getLastname());
            obj.put("password", d.getPassword());
            obj.put("licenceNumber", d.getLicenceNumber());
            obj.put("assignedOffice", d.getAssignedOffice());
            obj.put("specialty", d.getSpecialty().name());
            array.put(obj);
        }
        try (FileWriter writer = new FileWriter(filePath)) {
            array.write(writer, 4, 0);
        } catch (Exception e) {
            System.out.println("Error al guardar doctores en JSON: " + e.getMessage());
        }
    }

    public void saveAppointmentsToJson(String filePath) {
        JSONArray array = new JSONArray();
        for (Appointment app : repository.getAppointments()) {
            JSONObject obj = new JSONObject();
            obj.put("id", app.getId());
            obj.put("patientId", app.getPatient() != null ? app.getPatient().getId() : -1);
            obj.put("doctorId", app.getDoctor() != null ? app.getDoctor().getId() : -1);
            obj.put("reason", app.getReason());
            obj.put("type", app.isType());
            obj.put("datetime", app.getDatetime().toString());
            obj.put("status", app.getStatus().name());
            array.put(obj);
        }
        try (FileWriter writer = new FileWriter(filePath)) {
            array.write(writer, 4, 0);
        } catch (Exception e) {
            System.out.println("Error al guardar citas en JSON: " + e.getMessage());
        }
    }
}

