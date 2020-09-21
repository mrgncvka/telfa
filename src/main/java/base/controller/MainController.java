package base.controller;

import base.model.AuthenticationRequest;
import base.model.AuthenticationResponse;
import base.model.User;
import base.service.UserService;
import base.util.InstaDao;
import base.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class MainController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        final String username = authenticationRequest.getUsername();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Bad credentials", e);
        }
        User user = (User) userDetailsService.loadUserByUsername(username);

        if (!jwtUtil.validateToken(user.getToken(), user)) {
            String token = userDetailsService.processToken(user);
            return ResponseEntity.ok(new AuthenticationResponse(token));
        } else
            return new ResponseEntity("Your last token is not expired yet", HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        if (userDetailsService.save(user))
            return ResponseEntity.ok("User: " + user.getUsername() + " saved!");
        else
            return new ResponseEntity("Something went wrong", HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping("/data")
    public InstaDao getInstaUser(@AuthenticationPrincipal User user){

        return new InstaDao(user.getInstaUsername(), user.getInstaPassword());

    }

}
