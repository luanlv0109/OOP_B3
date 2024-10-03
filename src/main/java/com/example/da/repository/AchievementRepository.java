package com.example.da.repository;

import com.example.da.domain.Achievement;
import com.example.da.domain.Employee;
import com.example.da.dto.EmployeeSummaryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {

    @Query("SELECT new com.example.da.dto.EmployeeSummaryDTO(" +
            "e.id, " +
            "e.name, " +
            "e.image, " +
            "d.name, " +
            "COALESCE(SUM(CASE WHEN a.type = 1 THEN 1 ELSE 0 END), 0), " +
            "COALESCE(SUM(CASE WHEN a.type = 0 THEN 1 ELSE 0 END), 0), " +
            "COALESCE(SUM(CASE WHEN a.type = 1 THEN 1 ELSE 0 END) - SUM(CASE WHEN a.type = 0 THEN 1 ELSE 0 END), 0) " +
            ")" +
            " FROM Achievement a " +
            "LEFT JOIN Employee e ON e.id = a.employee.id " +
            "JOIN Department d ON d.id = e.department.id " +
            "GROUP BY e.id, e.name, e.image, d.name"
    )
    List<EmployeeSummaryDTO> getEmployeeAchievementSummary();

    boolean existsByEmployeeAndTypeAndDate(Employee employee, Integer type, LocalDate date);
    boolean existsByEmployeeAndTypeAndDateAndIdNot(Employee employee, Integer type, LocalDate date, Long id);

}
