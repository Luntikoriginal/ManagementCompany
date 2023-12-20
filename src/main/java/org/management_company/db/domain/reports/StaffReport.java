package org.management_company.db.domain.reports;

import lombok.Data;
import org.management_company.db.domain.entities.Staff;

@Data
public class StaffReport {

    private Staff staff;

    private Integer  amountWorkPerformed;

    private Double avgDuration;

    private Double avgScore;
}
