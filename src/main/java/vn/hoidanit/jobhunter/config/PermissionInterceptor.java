package vn.hoidanit.jobhunter.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.SecurityUtils;
import vn.hoidanit.jobhunter.util.error.PermissionException;

public class PermissionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    @Transactional // Cause role type is lazy load => need to open transaction to get role
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response, Object handler)
            throws Exception {

        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        System.out.println(">>> RUN preHandle");
        System.out.println(">>> path= " + path);
        System.out.println(">>> httpMethod= " + httpMethod);
        System.out.println(">>> requestURI= " + requestURI);

        // check permission

        // get current user from token after login and decode
        String email = SecurityUtils.getCurrentUserLogin().isPresent()
                ? SecurityUtils.getCurrentUserLogin().get()
                : null;
        if (email != null && !email.isEmpty()) {
            User user = this.userService.handleGetUserByUsername(email);
            if (user != null) {
                Role role = user.getRole();
                if (role != null) {
                    List<Permission> permissions = role.getPermissions();
                    boolean isAuthenticated = permissions
                            .stream()
                            .anyMatch(permission -> {
                                return permission.getApiPath().equals(path)
                                        && permission.getMethod().equals(httpMethod);
                            });
                    System.out.println(">>> isAuthenticated= " + isAuthenticated);
                    if (!isAuthenticated) {
                        throw new PermissionException("Permission denied");
                    }
                } else {
                    throw new PermissionException("Role not found");
                }
            }
        }

        return true;
    }
}
