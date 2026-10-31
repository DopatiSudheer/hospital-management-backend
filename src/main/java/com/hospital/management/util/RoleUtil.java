package com.hospital.management.util;

import jakarta.servlet.http.HttpServletRequest;

public class RoleUtil {
    
    private static final String ROLE_HEADER = "X-User-Role";
    
    /**
     * Get user role from request header
     * Note: In a production system, this should be extracted from JWT token or session
     */
    public static String getUserRole(HttpServletRequest request) {
        String role = request.getHeader(ROLE_HEADER);
        if (role == null || role.trim().isEmpty()) {
            return null;
        }
        return role.toUpperCase();
    }
    
    /**
     * Check if user has required role
     */
    public static boolean hasRole(HttpServletRequest request, String requiredRole) {
        String userRole = getUserRole(request);
        if (userRole == null) {
            return false;
        }
        return userRole.equals(requiredRole.toUpperCase());
    }
    
    /**
     * Check if user has any of the required roles
     */
    public static boolean hasAnyRole(HttpServletRequest request, String... requiredRoles) {
        String userRole = getUserRole(request);
        if (userRole == null) {
            return false;
        }
        for (String role : requiredRoles) {
            if (userRole.equals(role.toUpperCase())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check if user is Admin
     */
    public static boolean isAdmin(HttpServletRequest request) {
        return hasRole(request, "ADMIN");
    }
}
