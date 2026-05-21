/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.model;

/**
 *
 * @author jjlora
 */
public class prescription {
    private Appointment appointment;
    private String medicationName;
    private double dose;
    private String administrationRoute;
    private int treatmentDuration;
    private String additionalInstructions;
    private int frecuency;

    public prescription(Appointment appointment, String medicationName, double dose, String administrationRoute, int treatmentDuration, String additionalInstructions, int frecuency) {
        this.appointment = appointment;
        if (appointment != null) {
            appointment.addPrescription(this);
        }
        this.medicationName = medicationName;
        this.dose = dose;
        this.administrationRoute = administrationRoute;
        this.treatmentDuration = treatmentDuration;
        this.additionalInstructions = additionalInstructions;
        this.frecuency = frecuency;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public double getDose() {
        return dose;
    }

    public String getAdministrationRoute() {
        return administrationRoute;
    }

    public int getTreatmentDuration() {
        return treatmentDuration;
    }

    public String getAdditionalInstructions() {
        return additionalInstructions;
    }

    public int getFrecuency() {
        return frecuency;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public void setDose(double dose) {
        this.dose = dose;
    }

    public void setAdministrationRoute(String administrationRoute) {
        this.administrationRoute = administrationRoute;
    }

    public void setTreatmentDuration(int treatmentDuration) {
        this.treatmentDuration = treatmentDuration;
    }

    public void setAdditionalInstructions(String additionalInstructions) {
        this.additionalInstructions = additionalInstructions;
    }

    public void setFrecuency(int frecuency) {
        this.frecuency = frecuency;
    }
    
}
