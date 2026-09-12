package com.rata.userService.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/menus")
@Tag(name = "Menu APIs")
@RequiredArgsConstructor
public class MenuController {


}
