package fr.esic.mastering.api;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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
import java.util.List;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class UserRest {
	@Autowired
	private UserRepository userRepository;
    @Autowired
	private final AuthenticationManager authManager;
	private final JwtService jwtService = new JwtService();

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
