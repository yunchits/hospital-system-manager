package jdbc.reposipories;

import com.solvd.hospital.entities.doctor.Doctor;
import com.solvd.hospital.entities.doctor.DoctorSalary;
import com.solvd.hospital.repositories.DoctorRepository;
import com.solvd.hospital.repositories.impl.DoctorRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.Optional;

import static jdbc.reposipories.DoctorSalaryRepositoryImplTest.createSalaryObj;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DoctorRepositoryImplTest {

    private static final DoctorRepository doctorRepository = new DoctorRepositoryImpl();

    @Test
    void createDoctor() {
        Doctor doctor = createDoctorObj();

        Assertions.assertNotNull(doctor.getId());
    }

    @Test
    void findAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();

        Assertions.assertFalse(doctors.isEmpty());
    }

    @Test
    void findDoctorById() {
        Doctor doctor = createDoctorObj();
        Optional<Doctor> foundDoctor = doctorRepository.findById(doctor.getId());

        Assertions.assertTrue(foundDoctor.isPresent());
        Assertions.assertEquals(doctor.getId(), foundDoctor.get().getId());
    }

    @Test
    void deleteDoctor() {
        Doctor doctor = createDoctorObj();

        boolean isDeleted = doctorRepository.delete(doctor.getId());

        Assertions.assertTrue(isDeleted);
    }

    static Doctor createDoctorObj() {
        DoctorSalary salary = createSalaryObj();
        return doctorRepository.create(new Doctor()
            .setFirstName("Jane")
            .setLastName("Smith")
            .setSpecialization("Neurologist")
            .setSalary(salary));
    }
}
