package base.controller;

import base.model.User;
import base.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class MainController {

    private final UserRepo userRepo;

    @Autowired
    public MainController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @PostMapping("/user")
    public void test(@RequestParam String name){
        System.out.println(name);
    }

    @PostMapping("/add")
    public User add(@RequestBody User user){
        return userRepo.save(user);
    }

}
