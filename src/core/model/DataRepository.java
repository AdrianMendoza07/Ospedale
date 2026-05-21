/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.model;
import java.util.ArrayList;
/**
 *
 * @author tefy1
 */
public class DataRepository {
    private ArrayList<Patient> patients;
    private ArrayList<Doctor> doctors;
    private ArrayList<Appointment> appointments;
    private ArrayList<Hospitalization> hospitalizations;

    public DataRepository() {
        this.patients = new ArrayList<>();
        this.doctors = new ArrayList<>();
        this.appointments = new ArrayList<>();
        this.hospitalizations = new ArrayList<>();
    }

    public ArrayList<Patient> getPatients() {
        return patients;
    }

    public ArrayList<Doctor> getDoctors() {
        return doctors;
    }

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public ArrayList<Hospitalization> getHospitalizations() {
        return hospitalizations;
    }

    public Patient findPatientById(long id) {
        for (Patient p : patients) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null; 
    }

    public Doctor findDoctorById(long id) {
        for (Doctor d : doctors) {
            if (d.getId() == id) {
                return d;
            }
        }
        return null;
    }

    public ArrayList<Appointment> getAppointmentsByPatientOrdered(long patientId) {
        ArrayList<Appointment> filtradas = new ArrayList<>();
        
        for (Appointment app : appointments) {
            if (app.getPatient() != null && app.getPatient().getId() == patientId) {
                filtradas.add(app);
            }
        }
        
        filtradas.sort((a, b) -> b.getDatetime().compareTo(a.getDatetime()));
        
        return filtradas;
    }
}

