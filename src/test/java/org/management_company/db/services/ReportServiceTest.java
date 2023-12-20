package org.management_company.db.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.management_company.db.TestUtils;
import org.management_company.db.domain.entities.Staff;
import org.management_company.db.domain.reports.GeneralReport;
import org.management_company.db.domain.reports.StaffReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ReportServiceTest {

    @Autowired
    private ReportService reportService;

    @MockBean
    private EntityManager entityManager;

    @Test
    public void testCreateAllStaffReports() {
        List<Object[]> mockResults = new ArrayList<>();
        Object[] result1 = {new Staff(), 5, 10.0, 4.5};
        Object[] result2 = {new Staff(), 3, 8.0, 3.0};
        mockResults.add(result1);
        mockResults.add(result2);

        TypedQuery queryMock = mock(TypedQuery.class);
        when(entityManager.createQuery(
                "SELECT " +
                        "r.staff, " +
                        "COUNT(r), " +
                        "AVG(TIMESTAMPDIFF(HOUR, r.inProgressDateTime, r.doneDateTime)), " +
                        "AVG(rv.score) " +
                        "FROM Request r " +
                        "LEFT JOIN r.review rv " +
                        "WHERE r.doneDateTime IS NOT NULL " +
                        "GROUP BY r.staff", Object[].class))
                .thenReturn(queryMock);

        when(queryMock.getResultList()).thenReturn(mockResults);

        List<StaffReport> staffReports = reportService.createAllStaffReports();

        assertEquals(2, staffReports.size());
    }

    @Test
    public void testCreateReportByStaffId() {
        List<Object[]> mockResults = new ArrayList<>();
        Staff staff = TestUtils.createStaff(TestUtils.createSpecialization());
        Object[] result = {5, 10.0, 4.5};
        mockResults.add(result);

        TypedQuery queryMock = mock(TypedQuery.class);
        when(entityManager.createQuery(
                "SELECT " +
                        "COUNT(r), " +
                        "AVG(TIMESTAMPDIFF(HOUR, r.inProgressDateTime, r.doneDateTime)), " +
                        "AVG(rv.score) " +
                        "FROM Request r " +
                        "LEFT JOIN r.review rv " +
                        "WHERE r.doneDateTime IS NOT NULL" +
                        "AND r.staff.id = :staffId" +
                        "GROUP BY r.staff", Object[].class))
                .thenReturn(queryMock);

        when(queryMock.setParameter(eq("staffId"), any()))
                .thenReturn(queryMock);

        when(queryMock.getSingleResult())
                .thenReturn(mockResults.get(0));

        StaffReport staffReport = reportService.createReportByStaff(staff);

        assertNotNull(staffReport);
    }

    @Test
    public void testCreateGeneralReport() {
        List<Object[]> mockResults = new ArrayList<>();
        Staff staff = TestUtils.createStaff(TestUtils.createSpecialization());
        Object[] result = {5, 10.0, 4.5};
        mockResults.add(result);

        TypedQuery queryMock = mock(TypedQuery.class);
        when(entityManager.createQuery(
                "SELECT " +
                        "COUNT(r), " +
                        "AVG(TIMESTAMPDIFF(HOUR, r.inProgressDateTime, r.doneDateTime)), " +
                        "AVG(rv.score) " +
                        "FROM Request r " +
                        "LEFT JOIN r.review rv " +
                        "WHERE r.doneDateTime IS NOT NULL", Object[].class))
                .thenReturn(queryMock);

        when(queryMock.getSingleResult())
                .thenReturn(mockResults.get(0));

        GeneralReport generalReport = reportService.createGeneralReport();

        assertNotNull(generalReport);
    }
}

