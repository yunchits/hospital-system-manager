package jdbc.reposipories;

import com.solvd.hospital.entities.doctor.DoctorSalary;
import com.solvd.hospital.repositories.DoctorSalaryRepository;
import com.solvd.hospital.repositories.impl.DoctorSalaryRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDate;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DoctorSalaryRepositoryImplTest {

    private static final DoctorSalaryRepository salaryRepository = new DoctorSalaryRepositoryImpl();

    @Test
    void createSalary() {
        DoctorSalary salary = createSalaryObj();

        Assertions.assertNotNull(salary.getId());
    }

    @Test
    void findSalaryById() {
        DoctorSalary salary = createSalaryObj();

        Optional<DoctorSalary> foundSalary = salaryRepository.findById(salary.getId());

        Assertions.assertTrue(foundSalary.isPresent());
        Assertions.assertEquals(salary.getId(), foundSalary.get().getId());
    }

    @Test
    void updateSalary() {
        DoctorSalary salary = createSalaryObj();

        salary.setSalary(120000.0);
        DoctorSalary updatedSalary = salaryRepository.update(salary);

        Assertions.assertEquals(120000.0, updatedSalary.getSalary());
    }

    @Test
    void deleteSalary() {
        DoctorSalary salary = createSalaryObj();

        boolean isDeleted = salaryRepository.delete(salary.getId());

        Assertions.assertTrue(isDeleted);
    }

    static DoctorSalary createSalaryObj() {
        return salaryRepository.create(new DoctorSalary()
            .setSalary(1111.0)
            .setPaymentDate(LocalDate.now()));
    }
}
