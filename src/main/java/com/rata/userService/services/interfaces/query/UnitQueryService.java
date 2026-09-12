package com.rata.userService.services.interfaces.query;

import com.rata.userService.models.Unit;
import com.rata.userService.records.UnitRecord;
import com.rata.userService.services.interfaces.BasicQueryService;

import java.util.List;
import java.util.Set;

public interface UnitQueryService extends BasicQueryService<Unit, Long> {
    List<UnitRecord> findAllByUserId(long userId);

    Set<Unit> findByListUnitId(List<Long> ides);

}
