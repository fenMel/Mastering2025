package fr.esic.mastering.services;


import fr.esic.mastering.repository.UserAttributeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import fr.esic.mastering.entities.User;
import fr.esic.mastering.repository.UserRepository;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepo;

    @Autowired
    private UserAttributeService userAttributeService;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + email));

        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            List.of(new SimpleGrantedAuthority(user.getRole().getRoleUtilisateur().name()))
        );



    }

    public User getUserWithAttributes(Long userId) {
        User user = userRepo.findById(userId).orElse(null);
        if (user != null) {
            // Attach attributes as a transient property or return a DTO
            Map<String, String> attributes = userAttributeService.getAllAttributes(userId);
            // Either set to a transient field in User or create a UserDTO with these attributes
        }
        return user;
    }

    public String getUserAttribute(Long userId, String key) {
        return userAttributeService.getAttribute(userId, key);
    }




}



