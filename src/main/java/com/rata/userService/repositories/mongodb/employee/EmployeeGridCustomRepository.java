package com.rata.userService.repositories.mongodb.employee;

import com.rata.userService.models.docs.EmployeeGrid;
import com.rata.userService.records.newRecords.EmployeeSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeGridCustomRepository {

    Page<EmployeeGrid> searchEmployees(EmployeeSearchCriteria criteria, Pageable pageable);
}