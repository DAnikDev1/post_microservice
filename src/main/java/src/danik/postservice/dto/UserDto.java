package src.danik.postservice.dto;

import lombok.Builder;

@Builder
public record UserDto(Long id, String email, String username) {
}
