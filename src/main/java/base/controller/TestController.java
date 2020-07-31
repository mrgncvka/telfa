package base.controller;

import base.model.AuthenticationRequest;
import base.model.AuthenticationResponse;
import base.model.User;
import base.service.UserService;
import base.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Bad credentials", e);
        }

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        if(!jwtUtil.validateToken(user.getToken(), userDetails)) {
            final String token = jwtUtil.generateToken(userDetails);
            userDetailsService.setToken(user, token);
            return ResponseEntity.ok(new AuthenticationResponse(token));
        }
        else
            return new ResponseEntity("Your last token is not expired yet", HttpStatus.NOT_ACCEPTABLE);

    }

    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody User user){
        return ResponseEntity.ok(userDetailsService.save(user));
    }


}
