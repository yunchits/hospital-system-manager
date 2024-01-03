package jdbc.dao;

import com.solvd.hospital.entities.PatientDiagnosis;
import com.solvd.hospital.dao.PatientDiagnosisDAO;
import com.solvd.hospital.dao.jdbc.impl.JDBCPatientDiagnosisDAOImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static jdbc.dao.JDBCDiagnosisDAOImplTest.createDiagnosisObj;
import static jdbc.dao.JDBCPatientDAOImplTest.createPatientObj;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JDBCPatientDiagnosisDAOImplTest {

    private static final PatientDiagnosisDAO diagnosisRepository = new JDBCPatientDiagnosisDAOImpl();

    @Test
    void createPatientDiagnosis() {
        PatientDiagnosis patientDiagnosis = createPatientDiagnosisObj();

        Assertions.assertNotNull(patientDiagnosis.getDiagnosis());
    }

    @Test
    void findAllByPatientId() {
        PatientDiagnosis patientDiagnosis = createPatientDiagnosisObj();

        List<PatientDiagnosis> patientDiagnoses = diagnosisRepository.findAllByPatientId(patientDiagnosis.getPatientId());

        Assertions.assertNotNull(patientDiagnoses);
        Assertions.assertFalse(patientDiagnoses.isEmpty());
        Assertions.assertTrue(patientDiagnoses.stream()
            .anyMatch(pd -> pd.getPatientId() == patientDiagnosis.getPatientId()));
    }

    @Test
    void deletePatientDiagnosis() {
        PatientDiagnosis patientDiagnosis = createPatientDiagnosisObj();

        boolean result = diagnosisRepository.delete(patientDiagnosis.getPatientId(), patientDiagnosis.getDiagnosis().getId());

        Assertions.assertTrue(result);

        List<PatientDiagnosis> remainingDiagnoses = diagnosisRepository.findAllByPatientId(patientDiagnosis.getPatientId());

        Assertions.assertTrue(remainingDiagnoses.isEmpty());
    }

    static PatientDiagnosis createPatientDiagnosisObj() {
        return diagnosisRepository.create(new PatientDiagnosis()
            .setPatientId(createPatientObj().getId())
            .setDiagnosis(createDiagnosisObj()));
    }
}
