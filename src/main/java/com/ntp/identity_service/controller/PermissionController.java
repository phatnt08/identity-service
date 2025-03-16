package com.ntp.identity_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ntp.identity_service.dto.request.PermissionRequest;
import com.ntp.identity_service.dto.response.ApiResponse;
import com.ntp.identity_service.dto.response.PermissionResponse;
import com.ntp.identity_service.service.PermissionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
        log.info("Create permission: {}", request);
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAll() {
        log.info("Get all permissions");
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAll())
                .build();
    }

    @GetMapping("/{name}")
    ApiResponse<PermissionResponse> get(@PathVariable String name) {
        log.info("Get permission by name: {}", name);
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.get(name))
                .build();
    }

    @PutMapping("/{name}")
    public ApiResponse<PermissionResponse> put(@PathVariable String name, @RequestBody PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.update(name, request))
                .build();
    }

    @DeleteMapping("/{name}")
    ApiResponse<Void> delete(@PathVariable String name) {
        log.info("Delete permission by name: {}", name);
        permissionService.delete(name);
        return ApiResponse.<Void>builder().build();
    }
}
