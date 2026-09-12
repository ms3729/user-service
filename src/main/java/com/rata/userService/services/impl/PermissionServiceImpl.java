package com.rata.userService.services.impl;

import com.rata.userService.dto.PermissionDTO;
import com.rata.userService.models.Permission;
import com.rata.userService.repositories.mysql.PermissionRepository;
import com.rata.userService.services.interfaces.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;


    @Override
    public Optional<PermissionDTO> findById(long id) {
        return permissionRepository.findPermissionById(id);
    }

    @Override
    public List<PermissionDTO> findAllByEnabledStatus(boolean enabled) {
        List<Permission> permissionList = permissionRepository.findAll(enabled);
        List<PermissionDTO> dtoList = new ArrayList<>();
        permissionBuilder(permissionList, dtoList);
        return dtoList;
    }


    private void permissionBuilder(List<Permission> permissionList, List<PermissionDTO> permissionDTOList) {
        permissionList.forEach(
                p -> {
                    PermissionDTO permissionDTO = new PermissionDTO(p.getId(), p.getName(), p.getCode(), p.getPrerequisite() != null ? p.getPrerequisite().getId() : null);
                    if (!CollectionUtils.isEmpty(p.getChildes())) {
                        permissionDTO.setChildren(new ArrayList<>());
                        permissionBuilder(p.getChildes().stream().toList(), permissionDTO.getChildren());
                    }
                    permissionDTOList.add(permissionDTO);
                }
        );
    }
}
