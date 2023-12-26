package com.solvd.hospital.services.impl;

import com.solvd.hospital.common.exceptions.EntityNotFoundException;
import com.solvd.hospital.common.exceptions.RelatedEntityNotFound;
import com.solvd.hospital.dao.AppointmentDAO;
import com.solvd.hospital.dao.impl.JDBCAppointmentDAOImpl;
import com.solvd.hospital.entities.Appointment;
import com.solvd.hospital.entities.doctor.Doctor;
import com.solvd.hospital.entities.patient.Patient;
import com.solvd.hospital.services.AppointmentService;
import com.solvd.hospital.services.DoctorService;
import com.solvd.hospital.services.PatientService;

import java.time.LocalDateTime;
import java.util.List;

public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentDAO dao;
    private final PatientService patientService;
    private final DoctorService doctorService;

    public AppointmentServiceImpl() {
//    public AppointmentServiceImpl(DAOType daoType) {
//        switch (daoType) { todo
//            case MYBATIS:
//                this.repository = new MyBatisAppointmentDAOImpl();
//                break;
//            case JDBC:
//                this.repository = new JDBCAppointmentDAOImpl();
//                break;
//            default:
//                throw new IllegalArgumentException("Invalid DAOType: " + daoType);
//        }
        this.dao = new JDBCAppointmentDAOImpl();
        this.patientService = new PatientServiceImpl();
        this.doctorService = new DoctorServiceImpl();
    }

    @Override
    public Appointment create(long patientId, long doctorId, LocalDateTime appointmentDateTime) throws EntityNotFoundException {
        Appointment appointment = new Appointment();

        Patient patient = patientService.findById(patientId);
        Doctor doctor = doctorService.findById(doctorId);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        appointment.setAppointmentDateTime(appointmentDateTime);

        return dao.create(appointment);
    }

    @Override
    public List<Appointment> findAll() {
        return dao.findAll();
    }

    @Override
    public Appointment findById(long id) throws EntityNotFoundException {
        return dao.findById(id).orElseThrow(
            () -> new EntityNotFoundException("Appointment with ID: " + id + " not found")
        );
    }

    @Override
    public List<Appointment> findByPatientId(long patientId) throws EntityNotFoundException {
        List<Appointment> appointments = dao.findByPatientId(patientId);

        if (appointments.isEmpty()) {
            throw new EntityNotFoundException("Appointments not found for patient with ID: " + patientId);
        }

        return appointments;
    }

    @Override
    public List<Appointment> findByDoctorId(long doctorId) throws EntityNotFoundException {
        List<Appointment> appointments = dao.findByDoctorId(doctorId);

        if (appointments.isEmpty()) {
            throw new EntityNotFoundException("Appointments not found for doctor with ID: " + doctorId);
        }

        return appointments;
    }

    @Override
    public Appointment update(long id, long patientId, long doctorId, LocalDateTime appointmentDateTime) throws RelatedEntityNotFound, EntityNotFoundException {
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

    @Override
    public void delete(long id) throws EntityNotFoundException {
        if (!dao.delete(id)) {
            throw new EntityNotFoundException("Appointment with ID: " + id + " not found");
        }
    }
}
