package dk.lyngby.security.dto;

import java.util.Set;

public record RegisterDto(String username, String password, Set<String> roleList) {}
