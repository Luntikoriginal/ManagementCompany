package org.management_company.db.domain.reports;

import lombok.Data;

@Data
public class GeneralReport {

    private Integer amountWorkPerformed;

    private Double avgDuration;

    private Double avgScore;
}
