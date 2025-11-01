package ru.yandex.practicum.catsgram.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Имейл Должен быть указан");
        }
        String newEmail = user.getEmail();
        User equalEmailUser = users.values().stream()
                .filter(u -> u.getEmail().equals(newEmail))
                .findFirst()
                .orElse(null);
        if (equalEmailUser != null) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }
        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());
        //допустим username и пароль могут быть пустыми
        users.put(user.getId(), user);
        return user;
    }


    //вспомогательный метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = users.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        if (user.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (!users.containsKey(user.getId())){
            throw new ConditionsNotMetException("Пользователь с таким id не найден");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Имейл Должен быть указан");
        }
        String newEmail = user.getEmail();
        User equalEmailUser = users.values().stream()
                .filter(u -> u.getEmail().equals(newEmail))
                .findFirst()
                .orElse(null);
        if (equalEmailUser != null) {
            throw new DuplicatedDataException("Этот Имейл уже Используется");
        }
        User oldUser = users.get(user.getId());
        oldUser.setEmail(user.getEmail());
        oldUser.setUsername(user.getUsername());
        oldUser.setPassword(user.getPassword());
        oldUser.setEmail(user.getEmail());
        return oldUser;
    }
}
