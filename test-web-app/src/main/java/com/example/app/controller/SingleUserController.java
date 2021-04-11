package com.example.app.controller;

import com.example.app.service.TestUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.manasyan.kcache.core.annotations.KCacheEvict;
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
            tables = "users",
            key = "#args[0]"
    )
    public ResponseEntity<?> getUserById(
            @PathVariable String id,
            @RequestParam(value = "limit", defaultValue = "20") int limit
    ) {
        return ResponseEntity.ok(service.getUser(id));
    }

    @GetMapping("/evict/{id}")
    @KCacheEvict(
            tables = "users",
            key = "#args[0]"
    )
    public void evictUserById(
            @PathVariable String id
    ) {
        System.out.println("EVICT USER #" + id);
    }
}
