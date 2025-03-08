package com.ntp.identity_service.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ntp.identity_service.dto.request.RoleRequest;
import com.ntp.identity_service.dto.response.RoleResponse;
import com.ntp.identity_service.exception.AppException;
import com.ntp.identity_service.exception.ErrorCode;
import com.ntp.identity_service.mapper.RoleMapper;
import com.ntp.identity_service.repository.PermissionRepository;
import com.ntp.identity_service.repository.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class for handling role-related operations.
 */
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class RoleService {

    // Injecting the RoleRepository using constructor injection
    RoleRepository roleRepository;

    // Injecting the PermissionRepository using constructor injection
    PermissionRepository permissionRepository;

    // Injecting the RoleMapper using constructor injection
    RoleMapper roleMapper;

    /**
     * Creates a new role.
     * 
     * @param request the role creation request containing role details
     * @return the created role response
     */
    public RoleResponse create(RoleRequest request) {
        var role = roleMapper.toRole(request);

        var permissions = permissionRepository.findAllById(request.getPermissions());

        role.setPermissions(new HashSet<>(permissions));

        role = roleRepository.save(role);

        return roleMapper.toRoleResponse(role);
    }

    /**
     * Retrieves a role by ID.
     * 
     * @param id the ID of the role to retrieve
     * @return the role response
     */
    public RoleResponse get(String id) {
        var role = roleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        return roleMapper.toRoleResponse(role);
    }

    /**
     * Retrieves a list of all roles.
     * 
     * @return the list of role responses
     */
    public List<RoleResponse> getAll() {
        var roles = roleRepository.findAll();

        return roles.stream().map(roleMapper::toRoleResponse).toList();
    }

    /**
     * Updates an existing role.
     * 
     * @param id the ID of the role to update
     * @param request the role update request containing updated role details
     * @return the updated role response
     */
    public RoleResponse update(String id, RoleRequest request) {
        var role = roleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        role = roleMapper.toRole(request);

        var permissions = permissionRepository.findAllById(request.getPermissions());

        role.setPermissions(new HashSet<>(permissions));

        role = roleRepository.save(role);

        return roleMapper.toRoleResponse(role);
    }

    /**
     * Deletes a role by ID.
     * 
     * @param id the ID of the role to delete
     */
    public void delete(String id) {
        roleRepository.deleteById(id);
    }

}
