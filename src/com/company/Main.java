package com.company;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Main {

    public static void main (String[] args) {

        String data = scanData();
        addEmployeeData(data);
        maxHoursWorkedHours(addEmployeeData(data));
    }

    private static String scanData () {
        JFileChooser selectedFile = new JFileChooser();

        selectedFile.setDialogTitle("Please select a text file");
        String data = "";

        if (selectedFile.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            StringBuilder sb = new StringBuilder();
            File employeeData = selectedFile.getSelectedFile();

            try (Scanner sc = new Scanner(employeeData)) {
                while (sc.hasNextLine()) {
                    sb.append(sc.nextLine()).append("\n");
                }
            } catch (FileNotFoundException e) {
                System.out.println("There is no data!");
            }

            data = sb.toString();
        }
        return data;
    }

    private static HashMap<Integer, ArrayList<EmployeeImpl>> addEmployeeData (String data) {
        String[] arr = data.split("\n");
        HashMap<Integer, ArrayList<EmployeeImpl>> map = new HashMap<>();
        String[] row;


        for (int i = 0; i < arr.length; i++) {
            row = arr[i].split(", ");
            if (row.length == 4) {

                int projectID = Integer.parseInt(row[1].trim());
                int employeeID = Integer.parseInt(row[0].trim());
                LocalDate dateFrom = LocalDate.parse(row[2].trim());
                LocalDate endDate;
                if (row[3].trim().equalsIgnoreCase("null")) {
                    endDate = LocalDate.now();
                } else {
                    endDate = LocalDate.parse(row[3]);
                }

                EmployeeImpl employee = new EmployeeImpl(employeeID, projectID, dateFrom, endDate);

                if (!map.containsKey(projectID)) {
                    map.put(projectID, new ArrayList<>());

                }
                map.get(projectID).add(employee);

            }

        }
        return map;
    }

    public static HashMap<String, Long> maxHoursWorkedHours (HashMap<Integer, ArrayList<EmployeeImpl>> sortedHours) {
        HashMap<String, Long> result = new HashMap<>();
        Long maxValue;
        for (Map.Entry<Integer, ArrayList<EmployeeImpl>> entry : sortedHours.entrySet()) {
            ArrayList<EmployeeImpl> list = entry.getValue();

            for (int i = 0; i < list.size() - 1; i++) {
                EmployeeImpl employee1 = list.get(i);
                EmployeeImpl employee2 = list.get(i + 1);
                long combinedHours = 0;
                String bothEmployeeIDs = employee1.getEmpID() + "-" + employee2.getEmpID();

                if (employee1.getStartDate().isBefore(employee2.getStartDate())
                        && (employee2.getEndDate().isBefore(employee1.getEndDate()))) {
                    combinedHours = employee2.getStartDate().until(employee2.getEndDate(), ChronoUnit.DAYS);

                    result.put(bothEmployeeIDs, combinedHours);
                } else if (employee2.getStartDate().isBefore(employee1.getStartDate())
                        && (employee1.getEndDate().isBefore(employee2.getEndDate()))) {
                    combinedHours = employee1.getStartDate().until(employee1.getEndDate(), ChronoUnit.DAYS);
                    result.put(bothEmployeeIDs, combinedHours);
                } else if (employee1.getStartDate().isBefore(employee2.getStartDate())
                        && (employee1.getEndDate().isBefore(employee2.getEndDate()))) {
                    combinedHours = employee2.getStartDate().until(employee1.getEndDate(), ChronoUnit.DAYS);
                    result.put(bothEmployeeIDs, combinedHours);
                } else {
                    combinedHours = employee1.getStartDate().until(employee2.getEndDate(), ChronoUnit.DAYS);
                    result.put(bothEmployeeIDs, combinedHours);
                }
            }


        }

        Optional<Map.Entry<String, Long>> maxEntry = result.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue()
                );
        maxValue = maxEntry.get().getValue();
        for (Map.Entry<String, Long> employees : result.entrySet()) {
            if (employees.getValue().equals(maxValue)) {
                String[] employeesIDs = employees.getKey().split("-");
                System.out.printf("Employees with the  most combined hours are %s and %s!", employeesIDs[0], employeesIDs[1]);
                System.out.println();
                System.out.printf("Together they have %d days in between them!", maxValue);
                System.out.println();
            }
        }
            return result;
        }

    }