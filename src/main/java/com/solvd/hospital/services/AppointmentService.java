package com.solvd.hospital.services;

import com.solvd.hospital.common.AppProperties;
import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.exceptions.InvalidArgumentException;
import com.solvd.hospital.common.exceptions.RelatedEntityNotFound;
import com.solvd.hospital.dao.AppointmentDAO;
import com.solvd.hospital.dao.jdbc.impl.JDBCAppointmentDAOImpl;
import com.solvd.hospital.dao.mybatis.impl.MyBatisAppointmentDAOImpl;
import com.solvd.hospital.entities.Appointment;
import com.solvd.hospital.entities.doctor.Doctor;
import com.solvd.hospital.entities.patient.Patient;

import java.time.LocalDateTime;
import java.util.List;

public class AppointmentService {

    private final AppointmentDAO dao;
    private final PatientService patientService;
    private final DoctorService doctorService;

    public AppointmentService() {
        switch (AppProperties.getProperty("dao.type")) {
            case "mybatis":
                this.dao = new MyBatisAppointmentDAOImpl();
                break;
            case "jdbc":
                this.dao = new JDBCAppointmentDAOImpl();
                break;
            default:
                throw new IllegalArgumentException("Invalid DAO type");
        }
        this.patientService = new PatientService();
        this.doctorService = new DoctorService();
    }

    public Appointment create(long patientId, long doctorId, LocalDateTime appointmentDateTime) throws EntityNotFoundException, InvalidArgumentException {
        validateDateTime(appointmentDateTime);

        Appointment appointment = new Appointment();

        Patient patient = patientService.findById(patientId);
        Doctor doctor = doctorService.findById(doctorId);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        appointment.setAppointmentDateTime(appointmentDateTime);

        return dao.create(appointment);
    }

    public List<Appointment> findAll() {
        return dao.findAll();
    }

    public Appointment findById(long id) throws EntityNotFoundException {
        return dao.findById(id).orElseThrow(
            () -> new EntityNotFoundException("Appointment with ID: " + id + " not found")
        );
    }

    public List<Appointment> findByPatientId(long patientId) throws EntityNotFoundException {
        List<Appointment> appointments = dao.findByPatientId(patientId);

        if (appointments.isEmpty()) {
            throw new EntityNotFoundException("Appointments not found for patient with ID: " + patientId);
        }

        return appointments;
    }

    public List<Appointment> findByDoctorId(long doctorId) throws EntityNotFoundException {
        List<Appointment> appointments = dao.findByDoctorId(doctorId);

        if (appointments.isEmpty()) {
            throw new EntityNotFoundException("Appointments not found for doctor with ID: " + doctorId);
        }

        return appointments;
    }

    public Appointment update(long id, long patientId, long doctorId, LocalDateTime appointmentDateTime) throws RelatedEntityNotFound, EntityNotFoundException, InvalidArgumentException {
        validateDateTime(appointmentDateTime);

        Appointment appointment = new Appointment();

        findById(id);

        Patient patient;
        Doctor doctor;

        try {
            patient = patientService.findById(patientId);
            doctor = doctorService.findById(doctorId);
        } catch (EntityNotFoundException e) {
            throw new RelatedEntityNotFound();
        }

        appointment.setId(id);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        appointment.setAppointmentDateTime(appointmentDateTime);

        return dao.update(appointment);
    }

    public void delete(long id) throws EntityNotFoundException {
        findById(id);
        dao.delete(id);
    }

    private void validateDateTime(LocalDateTime appointmentDateTime) throws InvalidArgumentException {
        if (appointmentDateTime.isBefore(LocalDateTime.now())) {
            throw new InvalidArgumentException("The appointment date and time must be in the future");
        }
    }
}
