package tn.esprit.spring.controllers;

import lombok.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.AppUser;
import tn.esprit.spring.repositories.AppUserRepository;
import tn.esprit.spring.services.AccountService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "*")
@RepositoryRestResource
@RestController
public class UserController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AppUserRepository appUserRepository;
    @GetMapping("/Users")
    public List<AppUser> getAllUsers (){
        return appUserRepository.findAll();
    }
    @PostMapping("/signup")
    public AppUser signUp(@RequestBody UserForm userForm) {
        return accountService.signUp(
                userForm.getUsername(), userForm.getPassword(), userForm.getConfirmedPassword(),
                userForm.getEmail(), userForm.getCin());
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgetPassword(@RequestParam("username") String username) {
        try {
            accountService.forgetPassword(username);
            return ResponseEntity.ok("Password reset email sent");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to reset password: " + e.getMessage());
        }
    }
    @PostMapping("/register")
    public AppUser register(@RequestBody  UserForm userForm){
        return  accountService.saveUser(
                userForm.getUsername(),userForm.getPassword(),userForm.getConfirmedPassword(),userForm.getEmail(),userForm.getCin());
    }

    @GetMapping("/User/{username}")
    public ResponseEntity<AppUser> getUserByUsername(@PathVariable String username) {
        AppUser appUser = appUserRepository.findByUsername(username);
        if(appUser==null) throw new UsernameNotFoundException("User doesn't exist with username "+ username);
        return ResponseEntity.ok(appUser);
    }
    @PutMapping("/update/{username}")
    public ResponseEntity<AppUser> updateServor(@PathVariable String username, @RequestBody AppUser userDetails ){
        AppUser appUser = appUserRepository.findByUsername(username);


        appUser.setUsername(userDetails.getUsername());
        appUser.setEmail(userDetails.getEmail());
        appUser.setCin(userDetails.getCin());


      AppUser updatedServor = appUserRepository.save(appUser);
        return ResponseEntity.ok(updatedServor);
    }
    @PutMapping("/update-password/{username}")
    public ResponseEntity<String> updatePassword(
            @PathVariable String username,
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmedPassword") String confirmedPassword) {
        try {
            accountService.updatePassword(username, currentPassword, newPassword, confirmedPassword);
            return ResponseEntity.ok("Password updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update password: " + e.getMessage());
        }
    }

    @DeleteMapping ("/delete/{username}")
    public ResponseEntity<Map<String, Boolean>> delete(@PathVariable String username){
        AppUser appUser = appUserRepository.findByUsername(username);


       appUserRepository.delete(appUser);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}

@Data
class UserForm{
    private String username;
    private String password;
    private String confirmedPassword;
    private String email;
    private Long cin;
}
