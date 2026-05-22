/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.model;

import java.util.ArrayList;

/**
 *
 * @author edangulo
 */
public class Doctor extends User {
    
    private String licenceNumber;
    private Specialty specialty;
    private String assignedOffice;
    private ArrayList<Appointment> appointments;
    private ArrayList<Hospitalization> hospitalizations;

    public Doctor(long id, String username, String firstname, String lastname, String password, String licenceNumber, Specialty specialty, String assignedOffice) {
        super(id, username, firstname, lastname, password);
        this.licenceNumber = licenceNumber;
        this.specialty = specialty;
        this.assignedOffice = assignedOffice;
        this.appointments = new ArrayList<>();
        this.hospitalizations = new ArrayList<>();
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }

    public String getAssignedOffice() {
        return assignedOffice;
    }

    public void setAssignedOffice(String assignedOffice) {
        this.assignedOffice = assignedOffice;
    }

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public ArrayList<Hospitalization> getHospitalizations() {
        return hospitalizations;
    }

    public boolean addAppointment(Appointment appointment) {
        return this.appointments.add(appointment);
    }

    public boolean addHospitalization(Hospitalization hospitalization) {
        return this.hospitalizations.add(hospitalization);
    }
    
}
