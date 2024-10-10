package dk.lyngby.security.dto;

import dk.lyngby.security.model.Role;

public record TokenDto(String username, Role.RoleName[] roles) {
}
