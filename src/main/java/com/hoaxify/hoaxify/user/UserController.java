package com.hoaxify.hoaxify.user;

import com.hoaxify.hoaxify.shared.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/api/1.0/users")
    @ResponseStatus(code = HttpStatus.CREATED)
    GenericResponse createUser(@RequestBody User user) {
        userService.save(user);
        GenericResponse genericResponse = new GenericResponse();
        genericResponse.setMessage("user saved");
        return genericResponse;
    }
}
