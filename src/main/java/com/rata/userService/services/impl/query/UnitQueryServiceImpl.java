package com.rata.userService.services.impl.query;

import com.rata.userService.models.Unit;
import com.rata.userService.records.UnitRecord;
import com.rata.userService.repositories.mysql.UnitRepository;
import com.rata.userService.services.interfaces.query.UnitQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UnitQueryServiceImpl implements UnitQueryService {

    private final UnitRepository unitRepository;


    @Override
    public Optional<Unit> find(Long aLong) {
        return unitRepository.findById(aLong);
    }

    @Override
    public List<Unit> findAll() {
        return unitRepository.findAll();
    }

    @Override
    public List<UnitRecord> findAllByUserId(long userId) {
        List<Unit> unitList = unitRepository.findAllByUserId(userId);
        List<UnitRecord> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(unitList)) {
            result = unitList.stream().map(
                    u -> new UnitRecord(u.getId(), u.getTitle(), u.getCode())
            ).toList();
        }
        return result;
    }

    @Override
    public Set<Unit> findByListUnitId(List<Long> ides) {
        return unitRepository.findAllByIdIn(ides);
    }
}
