package com.rata.userService.models.party;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

/**
 * نوع مالکیت شرکت (دولتی، خصوصی و ...)
 */
@Getter
@Entity
public class OwnershipType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
}
