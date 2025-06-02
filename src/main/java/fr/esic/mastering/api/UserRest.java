package fr.esic.mastering.api;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import fr.esic.mastering.dto.UserCreationDTO;
import fr.esic.mastering.dto.UserDTO;
import fr.esic.mastering.entities.Role;
import fr.esic.mastering.entities.RoleType;
import fr.esic.mastering.exceptions.EmailAlreadyExistsException;
import fr.esic.mastering.mapper.UserMapper;
import fr.esic.mastering.repository.RoleRepository;
import fr.esic.mastering.services.EmailService;
import fr.esic.mastering.services.EmailTemplateService;
import fr.esic.mastering.services.UserAttributeService;
import org.hibernate.usertype.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    @Autowired
    private RoleRepository roleRepository;


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
	public ResponseEntity<List<UserDTO>> getAllUsers() {
		List<User> users = (List<User>) userRepository.findAll();
		List<UserDTO> userDTOs = users.stream()
				.map(UserMapper::toDTO)
				.collect(Collectors.toList());
		return ResponseEntity.ok(userDTOs);
	}

	//get_all_users_with_pagination

	@GetMapping("/users/paginated")
	public ResponseEntity<Map<String, Object>> getPaginatedUsers(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "asc") String direction) {

		// Create Pageable object
		Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ?
				Sort.Direction.DESC : Sort.Direction.ASC;
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

		// Get page of users
		Page<User> usersPage = userRepository.findAll(pageable);

		// Convert to DTOs
		List<UserDTO> userDTOs = usersPage.getContent().stream()
				.map(UserMapper::toDTO)
				.collect(Collectors.toList());

		// Prepare response
		Map<String, Object> response = new HashMap<>();
		response.put("users", userDTOs);
		response.put("currentPage", usersPage.getNumber());
		response.put("totalItems", usersPage.getTotalElements());
		response.put("totalPages", usersPage.getTotalPages());

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	//Get user by role
	@GetMapping("/users/paginated/by-role/{roleType}")
	public ResponseEntity<Map<String, Object>> getPaginatedUsersByRole(
			@PathVariable String roleType,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "asc") String direction) {

		// Create Pageable object
		Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ?
				Sort.Direction.DESC : Sort.Direction.ASC;
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

		// Get page of users by role type
		Page<User> usersPage;
		try {
			// Convert string to RoleType enum
			RoleType roleTypeEnum = RoleType.valueOf(roleType.toUpperCase());
			usersPage = userRepository.findByRole_RoleUtilisateur(roleTypeEnum, pageable);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(Map.of("error", "Invalid role type: " + roleType));
		}

		// Convert to DTOs
		List<UserDTO> userDTOs = usersPage.getContent().stream()
				.map(UserMapper::toDTO)
				.collect(Collectors.toList());

		// Prepare response
		Map<String, Object> response = new HashMap<>();
		response.put("users", userDTOs);
		response.put("currentPage", usersPage.getNumber());
		response.put("totalItems", usersPage.getTotalElements());
		response.put("totalPages", usersPage.getTotalPages());

		return ResponseEntity.ok(response);
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
	@CrossOrigin("*")
	@PostMapping("/user/create")
	public ResponseEntity<User> createUser(@RequestBody UserCreationDTO userDTO) {
		try {
			// Convert string to enum type
			RoleType roleType = RoleType.valueOf(userDTO.getRoleName().toUpperCase());

			// Find role by the enum type
			Role role = roleRepository.findByRoleUtilisateur(roleType)
					.orElseThrow(() -> new IllegalArgumentException("Le role fournis n'existe pas"));



			// Check if the email already exists
			if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
				throw new EmailAlreadyExistsException("Un utilisateur avec ce mail existe déjà");
			}

			User user = new User();
			user.setNom(userDTO.getNom());
			user.setPrenom(userDTO.getPrenom());
			user.setEmail(userDTO.getEmail());
			user.setRole(role);

			// Generate token for profile completion
			String token = UUID.randomUUID().toString();
			user.setResetToken(token);
			user.setResetTokenExpiry(LocalDateTime.now().plusHours(24));

			// Encode the password before saving
			if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
				user.setPassword(passwordEncoder.encode("TemporaryPassword123"));
			} else {
				user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
			}

			// Save the user
			User savedUser = userRepository.save(user);

			// Send email with profile completion link
			sendProfileCompletionEmail(savedUser, token);

			return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (EmailAlreadyExistsException e) {
            throw new RuntimeException(e);
        }
    }

	private void sendProfileCompletionEmail(User user, String token) {
		try {
			String subject = "Veuillez compléter votre profil";

			// Create a link to our own server
			String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
			String profileCompletionUrl = baseUrl + "/complete-profile?token=" + token;

			// Get email content from template service
			String content = EmailTemplateService.getProfileCompletionEmailTemplate(
					baseUrl,
					user.getPrenom(),
					user.getNom(),
					profileCompletionUrl
			);

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

		String jwt = jwtService.generateToken(userDetails, user.getId());

		return ResponseEntity.ok(new AuthResponse(jwt));
	}

}
