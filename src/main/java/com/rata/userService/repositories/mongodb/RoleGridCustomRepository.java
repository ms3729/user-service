package com.rata.userService.repositories.mongodb;

import com.rata.userService.models.docs.RoleGrid;
import com.rata.userService.records.newRecords.RoleSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleGridCustomRepository {

    Page<RoleGrid> searchRoles(RoleSearchCriteria criteria, Pageable pageable);
}
