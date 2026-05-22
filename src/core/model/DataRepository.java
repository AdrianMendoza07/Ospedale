/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.model;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.HashMap;
/**
 *
 * @author tefy1
 */
public class DataRepository {
    private static DataRepository instance;

    public static DataRepository getInstance() {
        if (instance == null) {
            instance = new DataRepository();
        }
        return instance;
    }

    private ArrayList<Patient> patients;
    private ArrayList<Doctor> doctors;
    private ArrayList<Administrator> administrators;
    private ArrayList<Appointment> appointments;
    private ArrayList<Hospitalization> hospitalizations;

    private DataRepository() {
        this.patients = new ArrayList<>();
        this.doctors = new ArrayList<>();
        this.administrators = new ArrayList<>();
        this.appointments = new ArrayList<>();
        this.hospitalizations = new ArrayList<>();
    }

    public ArrayList<Patient> getPatients() { return patients; }
    public ArrayList<Doctor> getDoctors() { return doctors; }
    public ArrayList<Administrator> getAdministrators() { return administrators; }
    public ArrayList<Appointment> getAppointments() { return appointments; }
    public ArrayList<Hospitalization> getHospitalizations() { return hospitalizations; }

    
    public Patient findPatientById(long id) {
        for (Patient p : patients) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    public Doctor findDoctorById(long id) {
        for (Doctor d : doctors) {
            if (d.getId() == id) return d;
        }
        return null;
    }

    public User findUserByUsername(String username) {
        for (Patient p : patients) {
            if (p.getUsername().equalsIgnoreCase(username)) return p;
        }
        for (Doctor d : doctors) {
            if (d.getUsername().equalsIgnoreCase(username)) return d;
        }
        for (Administrator a : administrators) {
            if (a.getUsername().equalsIgnoreCase(username)) return a;
        }
        return null;
    }


    public void addPatient(long longId, String username, String firstname, String lastname, String password, String email, LocalDate dateBirthdate, boolean boolGender, long longPhone, String address) {
        Patient target = new Patient(longId, username, firstname, lastname, password, email, dateBirthdate, boolGender, longPhone, address);
        this.patients.add(target);
    }

    public void updatePatient(long longId, String username, String firstname, String lastname, String password, String email, LocalDate dateBirthdate, boolean boolGender, long longPhone, String address) {
        Patient target = findPatientById(longId);
        if (target != null) {
            target.setUsername(username);
            target.setFirstname(firstname);
            target.setLastname(lastname);
            target.setPassword(password);
            target.setEmail(email);
            target.setBirthdate(dateBirthdate);
            target.setGender(boolGender);
            target.setPhone(longPhone);
            target.setAddress(address);
        }
    }

    public void deletePatient(long id) {
        Patient target = findPatientById(id);
        if (target != null) {
            this.patients.remove(target);
        }
    }


    public void addDoctor(long longId, String username, String firstname, String lastname, String password, String licenceNumber, Specialty specialty, String assignedOffice) {
        Doctor target = new Doctor(longId, username, firstname, lastname, password, licenceNumber, specialty, assignedOffice);
        this.doctors.add(target);
    }

    public void updateDoctor(long longId, String username, String firstname, String lastname, String password, String licenceNumber, Specialty specialty, String assignedOffice) {
        Doctor target = findDoctorById(longId);
        if (target != null) {
            target.setUsername(username);
            target.setFirstname(firstname);
            target.setLastname(lastname);
            target.setPassword(password);
            target.setLicenceNumber(licenceNumber);
            target.setSpecialty(specialty);
            target.setAssignedOffice(assignedOffice);
        }
    }

    public void deleteDoctor(long id) {
        Doctor target = findDoctorById(id);
        if (target != null) {
            this.doctors.remove(target);
        }
    }


    public void addAppointment(Appointment app) {
        if (app != null) {
            this.appointments.add(app);
        }
    }

    public void addHospitalization(Hospitalization hosp) {
        if (hosp != null) {
            this.hospitalizations.add(hosp);
        }
    }

    public void addAdministrator(Administrator admin) {
        if (admin != null) {
            this.administrators.add(admin);
        }
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


    public HashMap<String, Object> patientToMap(Patient p) {
        if (p == null) return null;
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", p.getId());
        map.put("username", p.getUsername());
        map.put("firstname", p.getFirstname());
        map.put("lastname", p.getLastname());
        map.put("email", p.getEmail());
        map.put("birthdate", p.getBirthdate());
        map.put("gender", p.isGender() ? "Femenino" : "Masculino");
        map.put("phone", p.getPhone());
        map.put("address", p.getAddress());
        return map;
    }

    public ArrayList<HashMap<String, Object>> getPatientsAsMaps() {
        ArrayList<HashMap<String, Object>> listaMapeada = new ArrayList<>();
        for (Patient p : patients) {
            listaMapeada.add(patientToMap(p));
        }
        return listaMapeada;
    }

    public HashMap<String, Object> doctorToMap(Doctor d) {
        if (d == null) return null;
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", d.getId());
        map.put("username", d.getUsername());
        map.put("firstname", d.getFirstname());
        map.put("lastname", d.getLastname());
        map.put("licenceNumber", d.getLicenceNumber());
        map.put("specialty", d.getSpecialty().name());
        map.put("assignedOffice", d.getAssignedOffice());
        return map;
    }

    public ArrayList<HashMap<String, Object>> getDoctorsAsMaps() {
        ArrayList<HashMap<String, Object>> listaMapeada = new ArrayList<>();
        for (Doctor d : doctors) {
            listaMapeada.add(doctorToMap(d));
        }
        return listaMapeada;
    }
}

