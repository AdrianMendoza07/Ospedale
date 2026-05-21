/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.model;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author edangulo
 */
public class Patient extends User {
    
    private String email;
    private LocalDate birthdate;
    private boolean gender;
    private long phone;
    private String address;
    private ArrayList<Appointment> appointments;
    private Hospitalization hospitalization;

    public Patient(long id, String username, String firstname, String lastname, String password, 
                   String email, LocalDate birthdate, boolean gender, long phone, String address) {
        
        super(id, username, firstname, lastname, password);
        
        validarDatosPatient(email, phone);
        
        this.email = email;
        this.birthdate = birthdate;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.appointments = new ArrayList<>();
    }

    private void validarDatosPatient(String email, long phone) {
        String phoneTexto = String.valueOf(phone);
        if (phoneTexto.length() != 10) {
            throw new IllegalArgumentException("El teléfono del paciente debe tener exactamente 10 dígitos.");
        }
        
        if (email == null || !email.matches("^[^@]+@[^@]+\\.com$")) {
            throw new IllegalArgumentException("El email de los pacientes debe ser válido (ejemplo: XXXXX@XXXXX.com).");
        }
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public boolean isGender() {
        return gender;
    }

    public long getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public Hospitalization getHospitalization() {
        return hospitalization;
    }

    public void setEmail(String email) {
        validarDatosPatient(email, this.phone);
        this.email = email;
    }

    public void setPhone(long phone) {
        validarDatosPatient(this.email, phone);
        this.phone = phone;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setHospitalization(Hospitalization hospitalization) {
        this.hospitalization = hospitalization;
    }
    
    public void addAppointment(Appointment a) {
        this.appointments.add(a);
    }
    
}
