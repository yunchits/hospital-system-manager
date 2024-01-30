package com.solvd.hospital.services;

import com.solvd.hospital.dao.DAOFactory;
import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.exceptions.InvalidArgumentException;
import com.solvd.hospital.common.exceptions.RelatedEntityNotFound;
import com.solvd.hospital.dao.AppointmentDAO;
import com.solvd.hospital.dto.AppointmentDTO;
import com.solvd.hospital.entities.Appointment;
import com.solvd.hospital.entities.Doctor;
import com.solvd.hospital.entities.patient.Patient;

import java.time.LocalDateTime;
import java.util.List;

public class AppointmentService {

    private final AppointmentDAO dao;
    private final PatientService patientService;
    private final DoctorService doctorService;

    public AppointmentService() {
        this.dao = DAOFactory.createAppointmentDAO();
        this.patientService = new PatientService();
        this.doctorService = new DoctorService();
    }

    public Appointment create(long patientId,
                              long doctorId,
                              LocalDateTime appointmentDateTime) throws EntityNotFoundException, InvalidArgumentException {
        validateDateTime(appointmentDateTime);

        Appointment appointment = new Appointment();
        Patient patient = patientService.findById(patientId);
        Doctor doctor = doctorService.findById(doctorId);

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDateTime(appointmentDateTime);

        return dao.create(appointment);
    }

    public Appointment create(AppointmentDTO appointmentDTO) throws EntityNotFoundException, InvalidArgumentException {
        LocalDateTime appointmentDateTime = appointmentDTO.getAppointmentDateTime();
        validateDateTime(appointmentDateTime);

        Appointment appointment = new Appointment();
        Patient patient = patientService.findById(appointmentDTO.getPatientId());
        Doctor doctor = doctorService.findById(appointmentDTO.getDoctorId());

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
            throw new RelatedEntityNotFound("Related entity for Appointment not found");
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
