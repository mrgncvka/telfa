package base.controller;

import base.model.User;
import base.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class MainController {

    private final UserRepo userRepo;

    @Autowired
    public MainController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @PostMapping("/add")
    public User add(@RequestBody User user){
        return userRepo.save(user);
    }

    @GetMapping("/id/{id}")
    public User getUser(@PathVariable Long id){
        Optional<User> user = userRepo.findById(id);
        return user.orElse(null);
    }

    @GetMapping("/hello")
    public String test(){
        return "Hello!";
    }


}
