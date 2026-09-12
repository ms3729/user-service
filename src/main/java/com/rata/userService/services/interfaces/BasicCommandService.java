package com.rata.userService.services.interfaces;

public interface BasicCommandService<T, D> {

    D save(D dto);

    D update(T entity, D dto);

    void delete(T entity);

}
