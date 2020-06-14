package base.controller;

import base.model.User;
import base.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    private final UserRepo userRepo;

    @Autowired
    public MainController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @PostMapping
    public User add(User user){
        return userRepo.save(user);
    }

}
