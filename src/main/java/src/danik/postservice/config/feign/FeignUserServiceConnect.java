package src.danik.postservice.config.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import src.danik.postservice.dto.UserDto;

import java.util.List;

@FeignClient(name = "user-microservice", url = "http://localhost:8080/api/v1", configuration = FeignConfig.class)
public interface FeignUserServiceConnect {
    @GetMapping("/users")
    List<UserDto> getAllUsers();

    @GetMapping("/users/{id}")
    UserDto getUserById(@PathVariable Long id);

    @PostMapping(value = "/users/list", consumes = MediaType.APPLICATION_JSON_VALUE)
    List<UserDto> getAllUsersByIds(@RequestBody List<Long> ids);
}
