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

    /**
     * Carga los usuarios desde el formato único de "users"
     */
    public void loadAllDataFromJson(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            JSONObject root = new JSONObject(new JSONTokener(reader));
            
            // 1. CARGAR USUARIOS (Tu arreglo principal)
            if (root.has("users")) {
                JSONArray usersArray = root.getJSONArray("users");
                for (int i = 0; i < usersArray.length(); i++) {
                    JSONObject obj = usersArray.getJSONObject(i);
                    
                    // Validamos el tipo para saber en qué lista del repositorio inyectarlo
                    String type = obj.getString("type").toLowerCase();
                    
                    long id = obj.getLong("id");
                    String username = obj.getString("username");
                    String firstname = obj.getString("firstname");
                    String lastname = obj.getString("lastname");
                    String password = obj.getString("password");

                    switch (type) {
                        case "admin":
                            Administrator admin = new Administrator(id, username, firstname, lastname, password);
                            repository.addAdministrator(admin);
                            break;

                        case "patient":
                            String email = obj.getString("email");
                            long phone = obj.getLong("phone");
                            String address = obj.getString("address");
                            LocalDate birthdate = LocalDate.parse(obj.getString("birthdate"));
                            boolean gender = obj.getBoolean("gender");
                            
                            Patient patient = new Patient(id, username, firstname, lastname, password, email, birthdate, gender, phone, address);
                            repository.loadPatient(patient);
                            break;

                        case "doctor":
                            String licenceNumber = obj.getString("licenceNumber");
                            String assignedOffice = obj.getString("assignedOffice");
                            Specialty specialty = Specialty.valueOf(obj.getString("specialty").toUpperCase());
                            
                            Doctor doctor = new Doctor(id, username, firstname, lastname, password, licenceNumber, specialty, assignedOffice);
                            repository.loadDoctor(doctor);
                            break;
                    }
                }
            }

            // 2. CARGAR CITAS (Solo si el JSON eventualmente las tiene)
            if (root.has("appointments")) {
                JSONArray appointmentsArray = root.getJSONArray("appointments");
                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                
                for (int i = 0; i < appointmentsArray.length(); i++) {
                    JSONObject obj = appointmentsArray.getJSONObject(i);
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
            }
            
            System.out.println("Carga de usuarios desde 'users.json' completada con éxito.");
            
        } catch (Exception e) {
            System.out.println("Error al cargar el archivo unificado JSON: " + e.getMessage());
        }
    }

    /**
     * Guarda manteniendo tu estructura limpia de un solo arreglo "users"
     */
    public void saveAllDataToJson(String filePath) {
        JSONObject root = new JSONObject();
        JSONArray usersArray = new JSONArray();
        
        // 1. Administradores
        for (Administrator a : repository.getAdministrators()) {
            JSONObject obj = new JSONObject();
            obj.put("type", "admin");
            obj.put("id", a.getId());
            obj.put("username", a.getUsername());
            obj.put("firstname", a.getFirstname());
            obj.put("lastname", a.getLastname());
            obj.put("password", a.getPassword());
            usersArray.put(obj);
        }

        // 2. Pacientes
        for (Patient p : repository.getPatients()) {
            JSONObject obj = new JSONObject();
            obj.put("type", "patient");
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
            usersArray.put(obj);
        }

        // 3. Doctores
        for (Doctor d : repository.getDoctors()) {
            JSONObject obj = new JSONObject();
            obj.put("type", "doctor");
            obj.put("id", d.getId());
            obj.put("username", d.getUsername());
            obj.put("firstname", d.getFirstname());
            obj.put("lastname", d.getLastname());
            obj.put("password", d.getPassword());
            obj.put("licenceNumber", d.getLicenceNumber());
            obj.put("assignedOffice", d.getAssignedOffice());
            obj.put("specialty", d.getSpecialty().name());
            usersArray.put(obj);
        }
        
        root.put("users", usersArray);

        // 4. Citas (Solo se añade al archivo si el repositorio tiene citas registradas)
        if (!repository.getAppointments().isEmpty()) {
            JSONArray appointmentsArray = new JSONArray();
            for (Appointment app : repository.getAppointments()) {
                JSONObject obj = new JSONObject();
                obj.put("id", app.getId());
                obj.put("patientId", app.getPatient() != null ? app.getPatient().getId() : -1);
                obj.put("doctorId", app.getDoctor() != null ? app.getDoctor().getId() : -1);
                obj.put("reason", app.getReason());
                obj.put("type", app.isType());
                obj.put("datetime", app.getDatetime().toString());
                obj.put("status", app.getStatus().name());
                appointmentsArray.put(obj);
            }
            root.put("appointments", appointmentsArray);
        }

        try (FileWriter writer = new FileWriter(filePath)) {
            root.write(writer, 4, 0);
            System.out.println("¡Usuarios actualizados correctamente en 'json/users.json'!");
        } catch (Exception e) {
            System.out.println("Error al guardar en el archivo unificado JSON: " + e.getMessage());
        }
    }
}