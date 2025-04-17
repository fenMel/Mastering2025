package fr.esic.mastering.api;

import java.time.LocalDateTime;
import java.util.*;

import fr.esic.mastering.entities.RoleType;
import fr.esic.mastering.services.EmailService;
import fr.esic.mastering.services.UserAttributeService;
import org.hibernate.usertype.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import fr.esic.mastering.dto.AuthRequest;
import fr.esic.mastering.dto.AuthResponse;
import fr.esic.mastering.entities.User;
import fr.esic.mastering.repository.UserRepository;
import fr.esic.mastering.security.JwtService;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class UserRest {
	@Autowired
	private UserRepository userRepository;
    @Autowired
	private final AuthenticationManager authManager;
	private final JwtService jwtService = new JwtService();
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private EmailService emailService;
	// @PostMapping("login")
	// public Optional<User> login(@RequestBody User user) {
	// return userRepository.findByEmailAndPassword(user.getEmail(),
	// user.getPassword());
	// }

	// samy
	@PostMapping("/login")
	public ResponseEntity<User> login(@RequestBody User user) {
		Optional<User> optionalUser = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
		if (optionalUser.isPresent()) {
			return ResponseEntity.ok(optionalUser.get());
		} else {
			return ResponseEntity.status(404).body(null); // Code 404 si l'utilisateur n'est pas trouvé
		}
	}
	// melissa

	@GetMapping("users")
	public Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}

	@GetMapping("users/{userId}")
	public Optional<User> User(@PathVariable Long userId) {
		return userRepository.findById(userId);
	}

	@GetMapping("users/email/{userEmail}")
	public Optional<User> User(@PathVariable String userEmail) {
		return userRepository.findByEmail(userEmail);
	}

	@GetMapping("email")
	public Optional<User> getUserByEmail(@RequestParam String email) {
		return userRepository.getByEmail(email);
	}

	@PutMapping("users/modif/{id}")
	public User updateUser(@PathVariable Long id, @RequestBody User u) {
		u.setId(id);
		return userRepository.save(u);

	}

	@DeleteMapping("users/delet/{id}")

	public boolean deletUser(@PathVariable Long id) {
		// avoir un ou 0 max user
		Optional<User> user = userRepository.findById(id);
		if (user.isPresent()) {
			userRepository.deleteById(id);
			return true;
		} else {
			return false;
		}
	}

	@GetMapping("user/noPwd/{id}")
	public Optional<User> getAllUserWithoutPassword(@PathVariable Long id) {
		return userRepository.findById(id);
	}



	//Create  user api
	@PostMapping("/user/create")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		// Generate token for profile completion
		String token = UUID.randomUUID().toString();
		user.setResetToken(token);
		user.setResetTokenExpiry(LocalDateTime.now().plusHours(24));

		// Encode the password before saving (use a temporary password if none provided)
		if (user.getPassword() == null || user.getPassword().isEmpty()) {
			user.setPassword(passwordEncoder.encode("TemporaryPassword123")); // This will be changed by the user
		} else {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}

		// Save the user
		User savedUser = userRepository.save(user);

		// Send email with profile completion link
		sendProfileCompletionEmail(savedUser, token);

		return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
	}

	private void sendProfileCompletionEmail(User user, String token) {
		try {
			String subject = "Veuillez completer votre profile";

			// Create a link to our own server
			String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
			String profileCompletionUrl = baseUrl + "/complete-profile?token=" + token;

			String content =
					"<p>Hello " + user.getPrenom() + " " + user.getNom() + ",</p>" +
							"<p>Vous avez été inscrit :</p>" +
							"<p><a href='" + profileCompletionUrl + "'>completer votre profile</a></p>" +
							"<p>Expire dans  24 heures.</p>" ;


			emailService.sendHtmlEmail(user.getEmail(), subject, content);
		} catch (Exception e) {
			// Log the error but don't prevent user creation
			System.err.println("Failed to send profile completion email: " + e.getMessage());
		}
	}


	@PostMapping("/login_with_jwt")
	public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
		authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

		UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getEmail(),
				user.getPassword(), List.of(new SimpleGrantedAuthority(user.getRole().getRoleUtilisateur().name())));

		String jwt = jwtService.generateToken(userDetails);

		return ResponseEntity.ok(new AuthResponse(jwt));
	}

}
