/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models;

import java.util.ArrayList;

/**
 *
 * @author edangulo
 */
public class Doctor extends User {
    
    private Specialty specialty;
    private String licenceNumber;
    private String assignedOffice;
    private ArrayList<Appointment> appointments;
    private ArrayList<Hospitalization> hospitalizations;

    public Doctor(long id, String username, String firstname, String lastname, String password, Specialty specialty, String licenceNumber, String assignedOffice) {
        super(id, username, firstname, lastname, password);
        
        validarDatosDoctor(licenceNumber, assignedOffice);
        
        this.specialty = specialty;
        this.licenceNumber = licenceNumber;
        this.assignedOffice = assignedOffice;
        this.hospitalizations = new ArrayList<>();
        this.appointments = new ArrayList<>();
    }

    private void validarDatosDoctor(String licenceNumber, String assignedOffice) {
        if (licenceNumber == null || !licenceNumber.matches("^L-\\d{10} MTL$")) {
            throw new IllegalArgumentException("La licencia del doctor debe seguir el formato: L-XXXXXXXXXX MTL");
        }
        
        if (assignedOffice == null || !assignedOffice.matches("^O-\\d{3}$")) {
            throw new IllegalArgumentException("La oficina asignada debe seguir el formato: O-XXX");
        }
    }

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public Specialty getSpecialty() {
        return specialty;
    }
    
    public boolean addHospitalization(Hospitalization hosp){
        return hospitalizations.add(hosp);
    }
    
    public void addAppointment(Appointment a) {
        this.appointments.add(a);
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }

    public void setLicenceNumber(String licenceNumber) {
        validarDatosDoctor(licenceNumber, this.assignedOffice);
        this.licenceNumber = licenceNumber;
    }

    public void setAssignedOffice(String assignedOffice) {
        validarDatosDoctor(this.licenceNumber, assignedOffice);
        this.assignedOffice = assignedOffice;
    }
}
