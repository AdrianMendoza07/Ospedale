/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 *
 * @author edangulo
 */
public class Appointment {
    
    private final String id;
    private Patient patient;
    private Doctor doctor;
    private Specialty specialty;
    private LocalDateTime datetime;
    private String reason;
    private boolean type;
    private ArrayList<prescription> prescriptions;
    private AppointmentStatus status;
    private String diagnosis;
    private String observations;
    private String recommendedTreatment;
    private String followUp;

    public Appointment(String id, Patient patient, Doctor doctor, Specialty specialty, LocalDateTime datetime, String reason, boolean type) {
        
        if (datetime != null) {
            int minute = datetime.getMinute();
            if (minute != 0 && minute != 15 && minute != 30 && minute != 45) {
                throw new IllegalArgumentException("Los minutos de la cita deben estar en cuartos de hora (:00, :15, :30, :45).");
            }
        }

        if (doctor != null && specialty != null) {
            if (!doctor.getSpecialty().equals(specialty)) {
                throw new IllegalArgumentException("La especialidad de la cita no coincide con la especialidad del doctor asignado.");
            }
        }

        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.specialty = specialty;
        this.datetime = datetime;
        this.reason = reason;
        this.type = type;
        this.status = AppointmentStatus.REQUESTED;
        this.prescriptions = new ArrayList<>();
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

    public Specialty getSpecialty() {
        return specialty;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public String getReason() {
        return reason;
    }

    public boolean isType() {
        return type;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getObservations() {
        return observations;
    }

    public String getRecommendedTreatment() {
        return recommendedTreatment;
    }

    public String getFollowUp() {
        return followUp;
    }

    public ArrayList<prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public void setRecommendedTreatment(String recommendedTreatment) {
        this.recommendedTreatment = recommendedTreatment;
    }

    public void setFollowUp(String followUp) {
        this.followUp = followUp;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public boolean addPrescription(prescription prescrip) {
        if (this.status != AppointmentStatus.PENDING) {
            throw new IllegalStateException("Solo se pueden agregar recetas médicas si la cita está en estado PENDING.");
        }
        return this.prescriptions.add(prescrip);
    }
    
}
