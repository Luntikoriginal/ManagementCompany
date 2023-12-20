package org.management_company.db.services;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.management_company.db.domain.entities.Staff;
import org.management_company.db.domain.reports.GeneralReport;
import org.management_company.db.domain.reports.StaffReport;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final EntityManager entityManager;
    public List<StaffReport> createAllStaffReports() {
        List<Object[]> results = entityManager.createQuery(
                "SELECT " +
                            "r.staff, " +
                            "COUNT(r), " +
                            "AVG(TIMESTAMPDIFF(HOUR, r.inProgressDateTime, r.doneDateTime)), " +
                            "AVG(rv.score) " +
                        "FROM Request r " +
                        "LEFT JOIN r.review rv " +
                        "WHERE r.doneDateTime IS NOT NULL " +
                        "GROUP BY r.staff", Object[].class)
                .getResultList();

        List<StaffReport> reports = new ArrayList<>();
        for (Object[] result : results) {
            StaffReport staffReport = new StaffReport();
            staffReport.setStaff((Staff) result[0]);
            staffReport.setAmountWorkPerformed((Integer) result[1]);
            staffReport.setAvgDuration((Double) result[2]);
            staffReport.setAvgScore((Double) result[3]);
            reports.add(staffReport);
        }
        return reports;
    }

    public StaffReport createReportByStaff(Staff staff) {
        Object[] result = entityManager.createQuery(
                "SELECT " +
                            "COUNT(r), " +
                            "AVG(TIMESTAMPDIFF(HOUR, r.inProgressDateTime, r.doneDateTime)), " +
                            "AVG(rv.score) " +
                        "FROM Request r " +
                        "LEFT JOIN r.review rv " +
                        "WHERE r.doneDateTime IS NOT NULL" +
                            "AND r.staff.id = :staffId" +
                        "GROUP BY r.staff", Object[].class)
                .setParameter("staffId", staff.getId())
                .getSingleResult();

        StaffReport staffReport = new StaffReport();
        staffReport.setStaff(staff);
        staffReport.setAmountWorkPerformed((Integer) result[0]);
        staffReport.setAvgDuration((Double) result[1]);
        staffReport.setAvgScore((Double) result[2]);
        return staffReport;
    }

    public GeneralReport createGeneralReport() {
        Object[] result = entityManager.createQuery(
                "SELECT " +
                            "COUNT(r), " +
                            "AVG(TIMESTAMPDIFF(HOUR, r.inProgressDateTime, r.doneDateTime)), " +
                            "AVG(rv.score) " +
                        "FROM Request r " +
                        "LEFT JOIN r.review rv " +
                        "WHERE r.doneDateTime IS NOT NULL", Object[].class)
                .getSingleResult();

        GeneralReport generalReport = new GeneralReport();
        generalReport.setAmountWorkPerformed((Integer) result[0]);
        generalReport.setAvgDuration((Double) result[1]);
        generalReport.setAvgScore((Double) result[2]);
        return generalReport;
    }
}
