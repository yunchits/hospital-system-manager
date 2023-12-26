package jdbc.dao;

import com.solvd.hospital.entities.doctor.Doctor;
import com.solvd.hospital.entities.doctor.DoctorSalary;
import com.solvd.hospital.dao.DoctorDAO;
import com.solvd.hospital.dao.impl.JDBCDoctorDAOImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.Optional;

import static jdbc.dao.JDBCDoctorSalaryDAOImplTest.createSalaryObj;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JDBCDoctorDAOImplTest {

    private static final DoctorDAO DOCTOR_DAO = new JDBCDoctorDAOImpl();

    @Test
    void createDoctor() {
        Doctor doctor = createDoctorObj();

        Assertions.assertNotNull(doctor.getId());
    }

    @Test
    void findAllDoctors() {
        List<Doctor> doctors = DOCTOR_DAO.findAll();

        Assertions.assertFalse(doctors.isEmpty());
    }

    @Test
    void findDoctorById() {
        Doctor doctor = createDoctorObj();
        Optional<Doctor> foundDoctor = DOCTOR_DAO.findById(doctor.getId());

        Assertions.assertTrue(foundDoctor.isPresent());
        Assertions.assertEquals(doctor.getId(), foundDoctor.get().getId());
    }

    @Test
    void deleteDoctor() {
        Doctor doctor = createDoctorObj();

        boolean isDeleted = DOCTOR_DAO.delete(doctor.getId());

        Assertions.assertTrue(isDeleted);
    }

    static Doctor createDoctorObj() {
        DoctorSalary salary = createSalaryObj();
        return DOCTOR_DAO.create(new Doctor()
            .setFirstName("Jane")
            .setLastName("Smith")
            .setSpecialization("Neurologist")
            .setSalary(salary));
    }
}
