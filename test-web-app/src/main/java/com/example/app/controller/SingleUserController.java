package com.example.app.controller;

import com.example.app.data.TestUser;
import com.example.app.service.TestUserService;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.manasyan.kcache.core.annotations.KCacheable;

@RestController
@RequestMapping("/test/users")
public class SingleUserController {
    private final TestUserService service;

    public SingleUserController(TestUserService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    @KCacheable(
            tables = "users"
    )
    public ResponseEntity<?> getUserById(
            @PathVariable String id,
            RequestEntity<?> requestEntity
    ) {
        return ResponseEntity.ok(service.getUser(id));
    }
}
