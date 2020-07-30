package base.service;

import base.model.User;
import base.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final PasswordEncoder bcryptEncoder;

    @Autowired
    private UserService(UserRepo userRepo, PasswordEncoder bcryptEncoder) {
        this.userRepo = userRepo;
        this.bcryptEncoder = bcryptEncoder;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findUserByUsername(username);

        if(user == null)
            throw new UsernameNotFoundException("Not found");
        return user;

    }

    public User save(User user){

        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
        return userRepo.save(newUser);


    }
}
