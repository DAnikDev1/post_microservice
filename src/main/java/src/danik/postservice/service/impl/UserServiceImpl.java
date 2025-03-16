package src.danik.postservice.service.impl;

import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import src.danik.postservice.config.feign.FeignUserServiceConnect;
import src.danik.postservice.config.user.UserContext;
import src.danik.postservice.dto.UserDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl{
    private final FeignUserServiceConnect feignUserServiceConnect;
    private final UserContext userContext;

    public List<UserDto> getAllUsers() {
        try {
            return feignUserServiceConnect.getAllUsers();
        } catch (FeignException e) {
            return Collections.emptyList();
        }
    }

    public UserDto getUserById(long id) {
        try {
            return feignUserServiceConnect.getUserById(id);
        } catch (FeignException ex) {
            throw new EntityNotFoundException(String.format("User with id = %d was not found", id));
        }
    }

    public List<UserDto> getAllUsersByIds(List<Long> ids) {
        try {
            return feignUserServiceConnect.getAllUsersByIds(ids);
        } catch (FeignException e) {
            return Collections.emptyList();
        }

    }
    public boolean isUserExist(long id) {
        try {
            feignUserServiceConnect.getUserById(id);
            return true;
        } catch (FeignException e) {
            return false;
        }
    }
    public boolean isUserExistInContext() {
        return Optional.ofNullable(userContext.getUserIdVault()).isPresent();
    }

    public UserDto getUserByContext() {
        return getUserById(userContext.getUserIdVault());
    }
}
