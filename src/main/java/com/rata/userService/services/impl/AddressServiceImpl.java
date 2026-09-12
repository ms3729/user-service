package com.rata.userService.services.impl;

import com.rata.userService.repositories.mysql.AddressRepository;
import com.rata.userService.services.interfaces.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

}
