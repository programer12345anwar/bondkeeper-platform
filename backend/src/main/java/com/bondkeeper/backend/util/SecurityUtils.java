package com.bondkeeper.backend.util;

import com.bondkeeper.backend.exception.BusinessException;
import com.bondkeeper.backend.security.UserPrincipal;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class SecurityUtils {

    public static Long getCurrentUserId() {
        return getCurrentUserPrincipal().getId();
    }

    public static String getCurrentUserEmail() {
        return getCurrentUserPrincipal().getEmail();
    }

    public static UserPrincipal getCurrentUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            throw new BusinessException("User is not authenticated");
        }
        return principal;
    }
}
