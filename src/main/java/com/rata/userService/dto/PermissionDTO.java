package com.rata.userService.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class PermissionDTO {
    private Long key;
    private String label;
    private String code;
    private Long parentId;
    private Long prerequisite;
    private boolean checked = true;
    private boolean partialSelected = false;
    private String style = "";
    private List<PermissionDTO> children;

    public PermissionDTO() {
    }

    public PermissionDTO(Long key, String label, String code) {
        this.key = key;
        this.label = label;
        this.code = code;
    }

    public PermissionDTO(Long key, String label, String code, Long prerequisite) {
        this(key, label, code);
        this.prerequisite = prerequisite;
    }

    public PermissionDTO(Long key, String label, String code, Long prerequisite, Long parentId) {
        this(key, label, code, prerequisite);
        this.parentId = parentId;
    }

    public PermissionDTO(Long key, String label, String code, Long prerequisite, List<PermissionDTO> children) {
        this(key, label, code, prerequisite);
        this.children = children;
    }
}
