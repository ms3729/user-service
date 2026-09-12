package com.rata.userService.services.interfaces;

import java.util.List;
import java.util.Optional;

public interface BasicQueryService<T, D> {

    Optional<T> find(D d);

    List<T> findAll();
}
