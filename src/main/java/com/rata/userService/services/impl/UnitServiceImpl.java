package com.rata.userService.services.impl;

import com.rata.userService.repositories.mysql.UnitRepository;
import com.rata.userService.services.interfaces.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UnitServiceImpl implements UnitService {

    private final UnitRepository unitRepository;

}
