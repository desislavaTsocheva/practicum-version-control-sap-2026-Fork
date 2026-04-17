package com.example.authmicroservice;

import com.example.authmicroservice.models.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldSetDefaultPictureWhenNoneIsProvided() {
        User user = new User();
        user.setProfilePicture(null);

        user.ensureDefaultPicture();

        assertNotNull(user.getProfilePicture());
        assertTrue(user.getProfilePicture().length > 0);
    }

    @Test
    void shouldReturnCorrectAuthorityBasedOnRole() {
        User user = new User();
        user.setRole("manager");

        var authorities = user.getAuthorities();

        boolean hasCorrectRole = authorities.stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_manager"));

        assertTrue(hasCorrectRole);
    }
}