package base.controller;

import base.model.User;
import base.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    @Autowired
    private UserService userService;

    @PostMapping("/registration")
    public ResponseEntity add(@RequestBody User user) {

        return userService.save(user) ? ResponseEntity.ok("Success") : new ResponseEntity("Error", HttpStatus.NOT_ACCEPTABLE);

    }

}
