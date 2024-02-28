package tn.esprit.spring.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.spring.entities.AppRole;
import tn.esprit.spring.entities.AppUser;
import tn.esprit.spring.repositories.AppRoleRepository;
import tn.esprit.spring.repositories.AppUserRepository;

import java.util.Random;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    private AppUserRepository appUserRepository;
    private AppRoleRepository appRoleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private EmailService emailService;
    public AccountServiceImpl(AppUserRepository appUserRepository, AppRoleRepository appRoleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.appUserRepository = appUserRepository;
        this.appRoleRepository = appRoleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    public AppUser signUp(String username, String password, String confirmedPassword, String email, Long cin) {
        // Check if the user already exists
        AppUser user = appUserRepository.findByUsername(username);
        if (user != null) {
            throw new RuntimeException("User already exists");
        }

        // Check if the password and confirmed password match
        if (!password.equals(confirmedPassword)) {
            throw new RuntimeException("Please confirm your password");
        }

        // Create a new user
        AppUser appUser = new AppUser();
        appUser.setUsername(username);
        appUser.setEmail(email);
        appUser.setActived(true);
        appUser.setPassword(bCryptPasswordEncoder.encode(password));
        appUser.setCin(cin);
        appUserRepository.save(appUser);

        // Assign the "USER" role to the user
        addRoleToUser(username, "USER");

        return appUser;
    }
    private String generateRandomPassword() {
        int length = 8; // Set the desired length of the password
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }

        return password.toString();
    }
    @Override
    public void forgetPassword(String username) {
        // Find the user by username
        AppUser appUser = appUserRepository.findByUsername(username);
        if (appUser == null) {
            throw new RuntimeException("User not found");
        }

        // Generate a new random password
        String newPassword = generateRandomPassword();

        // Set the new password for the user
        appUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
        appUserRepository.save(appUser);

        // Send the new password to the user
        String emailBody = "Your new password: " + newPassword;
        emailService.sendEmail(appUser.getEmail(), "Password Reset", emailBody);
    }

    @Override
    public void updatePassword(String username, String currentPassword, String newPassword, String confirmedPassword) {
        AppUser appUser = appUserRepository.findByUsername(username);
        if (appUser == null) {
            throw new RuntimeException("User not found");
        }

        // Check if the current password is correct
        if (!bCryptPasswordEncoder.matches(currentPassword, appUser.getPassword())) {
            throw new RuntimeException("Invalid current password");
        }

        // Check if the new password and confirmed password match
        if (!newPassword.equals(confirmedPassword)) {
            throw new RuntimeException("Please confirm your new password");
        }

        // Update the password
        appUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
        appUserRepository.save(appUser);
    }

    @Override
    public AppUser saveUser(String username, String password, String confirmedPassword, String email , Long cin) {
        AppUser  user=appUserRepository.findByUsername(username);
        if(user!=null) throw new RuntimeException("User already exists");
        if(!password.equals(confirmedPassword)) throw new RuntimeException("Please confirm your password");
        AppUser appUser=new AppUser();
        appUser.setUsername(username);
        appUser.setEmail(email);
        appUser.setActived(true);
        appUser.setPassword(bCryptPasswordEncoder.encode(password));
        appUser.setCin(cin);
        appUserRepository.save(appUser);
        addRoleToUser(username,"USER");
        return appUser;
    }




    @Override
    public AppRole save(AppRole role) {
        return appRoleRepository.save(role);
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public void addRoleToUser(String username, String rolename) {
        AppUser appUser=appUserRepository.findByUsername(username);
        AppRole appRole=appRoleRepository.findByRoleName(rolename);
        appUser.getRoles().add(appRole);
    }


}
