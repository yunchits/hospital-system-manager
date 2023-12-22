package jdbc.reposipories;

import com.solvd.hospital.entities.bill.Bill;
import com.solvd.hospital.entities.bill.PaymentStatus;
import com.solvd.hospital.repositories.BillRepository;
import com.solvd.hospital.repositories.impl.BillRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDate;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BillRepositoryImplTest {

    private BillRepository billRepository;

    @BeforeAll
    void setUp() {
        billRepository = new BillRepositoryImpl();
    }

    @Test
    void createBill() {
        Bill bill = new Bill()
            .setPatientId(1L)
            .setAmount(100.0)
            .setBillingDate(LocalDate.now())
            .setPaymentStatus(PaymentStatus.PAID);

        Bill createdBill = billRepository.create(bill);

        Assertions.assertNotNull(createdBill.getId());
    }

    @Test
    void findByPatientId() {
        List<Bill> bills = billRepository.findByPatientId(1L);

        Assertions.assertFalse(bills.isEmpty());
    }

    @Test
    void findByPatientIdAndBillingDate() {
        LocalDate date = LocalDate.now();

        List<Bill> bills = billRepository.findByPatientIdAndBillingDate(1L, date);

        Assertions.assertFalse(bills.isEmpty());
    }

    @Test
    void findByPatientIdAndPaymentStatus() {
        PaymentStatus status = PaymentStatus.PAID;

        List<Bill> bills = billRepository.findByPatientIdAndPaymentStatus(1L, status);

        Assertions.assertFalse(bills.isEmpty());
    }

    @Test
    void updateBill() {
        Bill bill = new Bill()
            .setPatientId(1L)
            .setAmount(150.0)
            .setBillingDate(LocalDate.now())
            .setPaymentStatus(PaymentStatus.PAID);
        Bill createdBill = billRepository.create(bill);


        createdBill.setAmount(200.0);
        Bill updatedBill = billRepository.update(createdBill);

        Assertions.assertEquals(200.0, updatedBill.getAmount());
    }

    @Test
    void deleteBill() {
        Bill bill = new Bill()
            .setPatientId(1L)
            .setAmount(300.0)
            .setBillingDate(LocalDate.now())
            .setPaymentStatus(PaymentStatus.PAID);
        Bill createdBill = billRepository.create(bill);

        boolean isDeleted = billRepository.delete(createdBill.getId());

        Assertions.assertTrue(isDeleted);
    }
}
