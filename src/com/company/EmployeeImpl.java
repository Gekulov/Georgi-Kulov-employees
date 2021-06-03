package com.company;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class EmployeeImpl {
    private int empID;
    private int projectID;
    private LocalDate startDate;
    private LocalDate endDate;

    public EmployeeImpl (int empID, int projectID, LocalDate startDate, LocalDate endDate) {
        this.empID = empID;
        this.projectID = projectID;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getEmpID () {
        return empID;
    }

    public int getProjectID () {
        return projectID;
    }

    public LocalDate getStartDate () {
        return startDate;
    }

    public LocalDate getEndDate () {
        return endDate;
    }



}
