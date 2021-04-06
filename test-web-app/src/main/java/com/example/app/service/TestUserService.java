package com.example.app.service;

import com.example.app.data.TestUser;
import com.example.app.repository.TestUserRepository;
import org.springframework.stereotype.Service;
import ru.nsu.manasyan.kcache.core.annotations.KCacheEvict;

import java.util.List;

@Service
public class TestUserService {
    private final TestUserRepository repository;

    public TestUserService(TestUserRepository userRepository) {
        this.repository = userRepository;
    }

    public List<TestUser> getUsers() {
        return repository.findAll();
    }

    @KCacheEvict(tables = {"users"})
    public void addUser(TestUser user) {
        repository.save(user);
    }

    public TestUser getUser(String id) {
        return repository.findUserById(id);
    }

    @KCacheEvict(tables = {"users"})
    public void evictUsersCache() {}
}
