package moreno.joaquin.webdemo.service;

import moreno.joaquin.webdemo.config.JwtService;
import moreno.joaquin.webdemo.exception.DuplicateEntryException;
import moreno.joaquin.webdemo.exception.UserCredentialsException;
import moreno.joaquin.webdemo.exception.UserNotFoundException;
import moreno.joaquin.webdemo.model.Role;
import moreno.joaquin.webdemo.model.User;
import moreno.joaquin.webdemo.model.UserDTO;
import moreno.joaquin.webdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    //@Transactional
    public UserDTO createUser(User user){
            if(userRepository.findByEmail(user.getEmail()).isPresent()){
                throw new DuplicateEntryException(user.getEmail());
            }
//            if(userRepository.findByUsername(user.getUsername()).isPresent()){
//                throw new DuplicateEntryException(user.getUsername());
//            }
            //Added by JWT tuto
            User userToCreate = User.builder()
                    //.username(user.getUsername())
                    .email(user.getEmail())
                    .name(user.getName())
                    .lastname(user.getLastname())
                    .password(passwordEncoder.encode(user.getPassword()))
                    .role(Role.USER)
                    .build();
            //
            //Save User
            userRepository.save(userToCreate);

            return UserDTO.builder().email(user.getEmail()).build();


    }


    public String authUser(UserDTO user){
        if(userRepository.findByEmail(user.getEmail()).isEmpty()){
            throw new UserNotFoundException(user.getEmail());
        }
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken( user.getEmail(), user.getPassword()));

        }catch (AuthenticationException e){
            throw new UserCredentialsException();
        }

        User registeredUser = User.builder().email(user.getEmail()).password(user.getPassword()).build();


        return jwtService.generateToken(registeredUser);

    }

    public boolean checkUser(String email){
        return userRepository.existsByEmail(email);
    }

    public User findUserById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public String getCurrentUser(){
        return  String.format("Logged in as %s",   SecurityContextHolder.getContext().getAuthentication().getName());

    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

}
