/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.model;

import java.time.LocalDate;

/**
 *
 * @author edangulo
 */
public class Hospitalization {
    
    private final String id;
    private Patient patient;
    private Doctor doctor;
    private LocalDate date;
    private String reason;
    private RoomType roomType;
    private String observations;
    private HospitalizationStatus status;

    
    public Hospitalization(String id, Patient patient, Doctor doctor, LocalDate date, String reason, RoomType roomType, String observations) {
        this.id = id;
        this.patient = patient;
        if (patient != null) {
            patient.setHospitalization(this);
        }
        this.doctor = doctor;
        if (doctor != null) {
            doctor.addHospitalization(this);
        }
        this.date = date;
        this.reason = reason;
        this.roomType = roomType;
        this.observations = observations;
        this.status = HospitalizationStatus.REQUESTED;
    }

    public Hospitalization(String id, Patient patient, Doctor doctor, LocalDate date, String reason, RoomType roomType, String observations, HospitalizationStatus hopsS) {
        this.id = id;
        this.patient = patient;
        if (patient != null) {
            patient.setHospitalization(this);
        }
        this.doctor = doctor;
        if (doctor != null) {
            doctor.addHospitalization(this);
        }
        this.date = date;
        this.reason = reason;
        this.roomType = roomType;
        this.observations = observations;
        this.status = hopsS;
    }

    public String getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getReason() {
        return reason;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public String getObservations() {
        return observations;
    }

    public HospitalizationStatus getStatus() {
        return status;
    }

    public void setStatus(HospitalizationStatus status) {
        this.status = status;
    }
    
}
