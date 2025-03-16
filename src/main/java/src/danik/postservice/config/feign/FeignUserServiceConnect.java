package src.danik.postservice.config.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import src.danik.postservice.dto.UserDto;

import java.util.List;

@FeignClient(name = "user-microservice", url = "localhost:8080")
@RequestMapping("/api/v1")
public interface FeignUserServiceConnect {
    @GetMapping("/users")
    List<UserDto> getAllUsers();

    @GetMapping("/users/{id}")
    UserDto getUserById(@PathVariable long id);

    @PostMapping("/users/list")
    List<UserDto> getAllUsersByIds(@RequestBody List<Long> ids);
}
