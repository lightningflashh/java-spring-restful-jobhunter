package vn.cheesethank.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.cheesethank.jobhunter.domain.Permission;
import vn.cheesethank.jobhunter.domain.response.ResultPaginationDTO;
import vn.cheesethank.jobhunter.service.PermissionService;
import vn.cheesethank.jobhunter.util.annotation.ApiMessage;
import vn.cheesethank.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    @ApiMessage("Create permission")
    public ResponseEntity<Permission> createAPermission(@Valid @RequestBody Permission permission)
            throws IdInvalidException {

        // check exist
        if (this.permissionService.isPermissionExist(permission)) {
            throw new IdInvalidException("Permission đã tồn tại.");
        }
        // create new permission
        return ResponseEntity.status(HttpStatus.CREATED).body(this.permissionService.create(permission));

    }

    @PutMapping("/permissions")
    @ApiMessage("Update a permission")
    public ResponseEntity<Permission> update(@Valid @RequestBody Permission permission) throws IdInvalidException {
        // check exist by id
        if (this.permissionService.fetchById(permission.getId()) == null) {
            throw new IdInvalidException("Permission với id = " + permission.getId() + " không tồn tại.");
        }

        // check exist by module, apiPath and method
        if (this.permissionService.isPermissionExist(permission)) {
            if (this.permissionService.isExistName(permission)) {
                throw new IdInvalidException("Permission đã tồn tại.");

            }
        }

        // update permission
        return ResponseEntity.ok().body(this.permissionService.update(permission));
    }

    @GetMapping("/permissions")
    @ApiMessage("Get permissions with pagination")
    public ResponseEntity<ResultPaginationDTO> getAllPermissions(
            @Filter Specification<Permission> spec,
            Pageable pageable) {
        return ResponseEntity.ok().body(this.permissionService.fetchAll(spec, pageable));
    }

    @DeleteMapping("/permissions/{id}")
    @ApiMessage("delete a permission")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        // check exist by id
        if (this.permissionService.fetchById(id) == null) {
            throw new IdInvalidException("Permission với id = " + id + " không tồn tại.");
        }
        this.permissionService.delete(id);
        return ResponseEntity.ok().body(null);
    }

}
