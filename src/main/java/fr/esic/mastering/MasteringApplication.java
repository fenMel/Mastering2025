package fr.esic.mastering;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Stream;

import fr.esic.mastering.entities.*;
import fr.esic.mastering.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import fr.esic.mastering.entities.Formation;
import fr.esic.mastering.entities.Role;
import fr.esic.mastering.entities.RoleType;
import fr.esic.mastering.entities.SessionFormation;
import fr.esic.mastering.entities.SessionSoutenance;
import fr.esic.mastering.entities.User;
import fr.esic.mastering.repository.FormationRepository;
import fr.esic.mastering.repository.RoleRepository;
import fr.esic.mastering.repository.SessionFormationRepository;
import fr.esic.mastering.repository.UserRepository;
import fr.esic.mastering.repository.SessionSoutenanceRepository;
import fr.esic.mastering.repository.SessionSoutenanceUserRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class MasteringApplication implements CommandLineRunner {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private FormationRepository formationRepository;

	@Autowired
	private EvaluationRepository evaluationRepository;

	@Autowired
	private SessionFormationRepository sessionFormationRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private SessionSoutenanceRepository sessionSoutenanceRepository;

	@Autowired
	private SessionSoutenanceUserRepository sessionSoutenanceUserRepository;

	public static void main(String[] args) {
		SpringApplication.run(MasteringApplication.class, args);
		System.out.println("lancement terminé");
	}

	@Override
	public void run(String... args) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		LocalDate today = LocalDate.now(); // Get the current date once

		/*
		 * -------------------------------- Ajout des Roles
		 * --------------------------------
		 */
		System.out.println("****************---------------Ajout des roles-----------------****************");
		Role roleCoordinateur = new Role();
		roleCoordinateur.setRoleUtilisateur(RoleType.CORDINATEUR);

		Role roleApprenant = new Role();
		roleApprenant.setRoleUtilisateur(RoleType.APPRENANT);

		Role roleJury = new Role();
		roleJury.setRoleUtilisateur(RoleType.JURY);

		Role roleSupervisor = new Role();
		roleSupervisor.setRoleUtilisateur(RoleType.SUPERVISOR);

		Role roleSupportStaff = new Role();
		roleSupportStaff.setRoleUtilisateur(RoleType.SUPPORT_STAFF);

		Role roleCandidate = new Role();
		roleCandidate.setRoleUtilisateur(RoleType.CANDIDAT);

		Stream.of(roleCoordinateur, roleApprenant, roleJury, roleSupervisor, roleSupportStaff, roleCandidate)
				.forEach(role -> {
					roleRepository.save(role);
				});
		System.out.println("****************---------------°FIN° Ajout des roles-----------------****************");

		/*
		 * -------------------------------- Ajout des Users
		 * --------------------------------
		 */
		System.out.println("****************---------------Ajout des users-----------------****************");

		// Coordinateurs (3)
		User coord1 = new User(null, "Durand", "Luc", sdf.parse("05/02/1985"), "0601122334", "dede@gmail.com",
				"Lyon", passwordEncoder.encode("LucCoord123"), roleCoordinateur);
		User coord2 = new User(null, "Morel", "Sophie", sdf.parse("12/03/1984"), "0601234567", "sophie.morel@gmail.com",
				"Nice", passwordEncoder.encode("SophieCoord123"), roleCoordinateur);
		User coord3 = new User(null, "Martin", "Paul", sdf.parse("23/07/1986"), "0601456789", "paul.martin@gmail.com",
				"Toulouse", passwordEncoder.encode("PaulCoord123"), roleCoordinateur);

		// Apprenants (5)
		User appr1 = new User(null, "Nguyen", "Linh", sdf.parse("15/09/1998"), "0601987654", "linh.nguyen@gmail.com",
				"Hanoi", passwordEncoder.encode("LinhApp123"), roleApprenant);
		User appr2 = new User(null, "Toure", "Abdou", sdf.parse("02/02/2000"), "0601122544", "abdou.toure@gmail.com",
				"Abidjan", passwordEncoder.encode("AbdouApp123"), roleApprenant);
		User appr3 = new User(null, "Petit", "Claire", sdf.parse("30/11/1997"), "0601346723", "claire.petit@gmail.com",
				"Lille", passwordEncoder.encode("ClaireApp123"), roleApprenant);
		User appr4 = new User(null, "Moulin", "Julien", sdf.parse("19/06/1999"), "0601764532",
				"julien.moulin@gmail.com", "Bordeaux", passwordEncoder.encode("JulienApp123"), roleApprenant);
		User appr5 = new User(null, "Keita", "Fatou", sdf.parse("08/04/1996"), "0601763456", "fatou.keita@gmail.com",
				"Bamako", passwordEncoder.encode("FatouApp123"), roleApprenant);

		// Jury (5)
		User jury1 = new User(null, "Benoit", "Elise", sdf.parse("25/12/1975"), "0601452367", "elise.benoit@gmail.com",
				"Strasbourg", passwordEncoder.encode("EliseJury123"), roleJury);
		User jury2 = new User(null, "Rossi", "Mario", sdf.parse("03/06/1980"), "0601543627", "mario.rossi@gmail.com",
				"Rome", passwordEncoder.encode("MarioJury123"), roleJury);
		User jury3 = new User(null, "Lopez", "Ines", sdf.parse("22/10/1978"), "0601654738", "ines.lopez@gmail.com",
				"Madrid", passwordEncoder.encode("InesJury123"), roleJury);
		User jury4 = new User(null, "Dubois", "Arnaud", sdf.parse("18/01/1982"), "0601122433",
				"arnaud.dubois@gmail.com", "Marseille", passwordEncoder.encode("ArnaudJury123"), roleJury);
		User jury5 = new User(null, "Schmidt", "Laura", sdf.parse("11/11/1985"), "0601457689",
				"laura.schmidt@gmail.com", "Berlin", passwordEncoder.encode("LauraJury123"), roleJury);

		// Superviseurs (2)
		User sup1 = new User(null, "Ahmed", "Karim", sdf.parse("14/08/1979"), "0601998765", "karim.ahmed@gmail.com",
				"Tunis", passwordEncoder.encode("KarimSup123"), roleSupervisor);
		User sup2 = new User(null, "Becker", "Hans", sdf.parse("07/05/1983"), "0601223344", "hans.becker@gmail.com",
				"Frankfurt", passwordEncoder.encode("HansSup123"), roleSupervisor);

		// Support Staff (3)
		User staff1 = new User(null, "Roy", "Camille", sdf.parse("29/03/1990"), "0601334455", "camille.roy@gmail.com",
				"Rennes", passwordEncoder.encode("CamilleSupport123"), roleSupportStaff);
		User staff2 = new User(null, "Zhou", "Wei", sdf.parse("17/12/1992"), "0601445566", "wei.zhou@gmail.com",
				"Shanghai", passwordEncoder.encode("WeiSupport123"), roleSupportStaff);
		User staff3 = new User(null, "Ali", "Nour", sdf.parse("10/10/1988"), "0601556677", "nour.ali@gmail.com",
				"Casablanca", passwordEncoder.encode("NourSupport123"), roleSupportStaff);

		// Candidats (10)
		User cand1 = new User(null, "Remsou", "Remi", sdf.parse("09/01/1995"), "0601667788", "remsou28@gmail.com",
				"Dakar", passwordEncoder.encode("Remsou28"), roleCandidate);
		User cand2 = new User(null, "Chance", "Lucas", sdf.parse("20/05/1996"), "0601778899", "cdmilandou@gmail.com",
				"Madrid", passwordEncoder.encode("Chance123"), roleCandidate);
		User cand3 = new User(null, "Kim", "Jin", sdf.parse("01/07/1994"), "0601889900", "jin.kim@gmail.com", "Seoul",
				passwordEncoder.encode("JinCand123"), roleCandidate);
		User cand4 = new User(null, "Leblanc", "Eva", sdf.parse("14/02/1993"), "0601990011", "eva.leblanc@gmail.com",
				"Lyon", passwordEncoder.encode("EvaCand123"), roleCandidate);
		User cand5 = new User(null, "Smith", "Emily", sdf.parse("03/03/1992"), "0601223345", "emily.smith@gmail.com",
				"London", passwordEncoder.encode("EmilyCand123"), roleCandidate);
		User cand6 = new User(null, "Fernandez", "Carlos", sdf.parse("06/06/1991"), "0601334456",
				"carlos.fernandez@gmail.com", "Barcelone", passwordEncoder.encode("CarlosCand123"), roleCandidate);
		User cand7 = new User(null, "Kouassi", "Yao", sdf.parse("13/07/1990"), "0601445567", "yao.kouassi@gmail.com",
				"Abidjan", passwordEncoder.encode("YaoCand123"), roleCandidate);
		User cand8 = new User(null, "Ivanov", "Anna", sdf.parse("22/08/1989"), "0601556678", "anna.ivanov@gmail.com",
				"Moscou", passwordEncoder.encode("AnnaCand123"), roleCandidate);
		User cand9 = new User(null, "Omar", "Lina", sdf.parse("27/09/1987"), "0601667789", "lina.omar@gmail.com",
				"Rabat", passwordEncoder.encode("LinaCand123"), roleCandidate);
		User cand10 = new User(null, "Park", "Min", sdf.parse("30/10/1986"), "0601778890", "min.park@gmail.com",
				"Busan", passwordEncoder.encode("MinCand123"), roleCandidate);

		Stream.of(coord1, coord2, coord3, appr1, appr2, appr3, appr4, appr5, jury1, jury2, jury3, jury4, jury5, sup1,
						sup2, staff1, staff2, staff3, cand1, cand2, cand3, cand4, cand5, cand6, cand7, cand8, cand9, cand10)
				.peek(user -> {
					// Set token fields for all users
					user.setResetToken(null);
					user.setResetTokenExpiry(null);
				})
				.forEach(user -> {
					userRepository.save(user);
				});

		System.out.println("****************---------------°FIN° Ajout des users-----------------****************");

		/*
		 * -------------------------------- Ajout des formations
		 * --------------------------------
		 */
		System.out.println("****************---------------Ajout des formations-----------------****************");

		Formation iaFormation = formationRepository.findByNom("Master Intelligence Artificielle")
				.orElseGet(() -> formationRepository.save(new Formation(
						null,
						"Master Intelligence Artificielle",
						"Bac+5",
						"RNCP12345",
						"Formation sur l'intelligence artificielle, deep learning, machine learning.",
						"2 ans",
						"Licence Informatique ou équivalent",
						"Maîtriser les techniques d'IA pour développer des solutions intelligentes.")));

		Formation cyberFormation = formationRepository.findByNom("Master Cybersécurité")
				.orElseGet(() -> formationRepository.save(new Formation(
						null,
						"Master Cybersécurité",
						"Bac+5",
						"RNCP23456",
						"Formation sur la sécurité des systèmes informatiques et des réseaux.",
						"2 ans",
						"Licence Réseaux ou Informatique",
						"Protéger les infrastructures numériques contre les cybermenaces.")));

		Formation cloudFormation = formationRepository.findByNom("Master Cloud Computing")
				.orElseGet(() -> formationRepository.save(new Formation(
						null,
						"Master Cloud Computing",
						"Bac+5",
						"RNCP34567",
						"Apprentissage des solutions cloud, conteneurs, virtualisation.",
						"2 ans",
						"Licence ou équivalent en Informatique",
						"Déployer des architectures cloud performantes et sécurisées.")));

		Formation dataFormation = formationRepository.findByNom("Master Data Science")
				.orElseGet(() -> formationRepository.save(new Formation(
						null,
						"Master Data Science",
						"Bac+5",
						"RNCP45678",
						"Analyse de données, big data, statistiques avancées.",
						"2 ans",
						"Licence Mathématiques, Statistiques ou Informatique",
						"Extraire des informations à partir de grands ensembles de données.")));

		Formation gestionFormation = formationRepository.findByNom("Master Gestion de Projet")
				.orElseGet(() -> formationRepository.save(new Formation(
						null,
						"Master Gestion de Projet",
						"Bac+5",
						"RNCP56789",
						"Formation sur les méthodes de gestion de projet (agile, PMP, etc.).",
						"2 ans",
						"Licence ou équivalent en gestion ou informatique",
						"Piloter efficacement des projets complexes en entreprise.")));

		System.out.println("****************---------------°FIN° Ajout des Formations-----------------****************");

		/*
		 * -------------------------------- Ajout des sessions de formations
		 * --------------------------------
		 */
		// We're no longer using a fixed formatter here, but keep it if used for parsing other fixed dates
		// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		System.out.println("****************---------------Ajout des sessions de formations-----------------****************");

		// Dynamically setting future dates using LocalDate.now()
		SessionFormation session1 = new SessionFormation(
				null,
				"Session IA - Paris",
				"Session avancée sur l'intelligence artificielle.",
				today.plusDays(7), // Starts 7 days from today
				today.plusMonths(6).plusDays(7), // Ends 6 months and 7 days from today
				iaFormation,
				new ArrayList<>()
		);

		SessionFormation session2 = new SessionFormation(
				null,
				"Session Cybersécurité - Lyon",
				"Session sécurité offensive et défensive.",
				today.plusWeeks(4), // Starts 4 weeks from today
				today.plusMonths(7).plusWeeks(4), // Ends 7 months and 4 weeks from today
				cyberFormation,
				new ArrayList<>()
		);

		SessionFormation session3 = new SessionFormation(
				null,
				"Session Cloud - Marseille",
				"Session sur les solutions cloud modernes.",
				today.plusMonths(2).plusDays(10), // Starts 2 months and 10 days from today
				today.plusMonths(8).plusDays(10), // Ends 8 months and 10 days from today
				cloudFormation,
				new ArrayList<>()
		);

		SessionFormation session4 = new SessionFormation(
				null,
				"Session Data Science - Toulouse",
				"Session pour apprendre à analyser des données.",
				today.plusMonths(3).plusDays(5), // Starts 3 months and 5 days from today
				today.plusMonths(9).plusDays(5), // Ends 9 months and 5 days from today
				dataFormation,
				new ArrayList<>()
		);

		SessionFormation session5 = new SessionFormation(
				null,
				"Session Gestion de Projet - Nantes",
				"Session sur les méthodes agiles et traditionnelles.",
				today.plusMonths(4).plusDays(1), // Starts 4 months and 1 day from today
				today.plusMonths(10).plusDays(1), // Ends 10 months and 1 day from today
				gestionFormation,
				new ArrayList<>()
		);

		Stream.of(session1, session2, session3, session4, session5)
				.forEach(session -> {
					sessionFormationRepository.save(session);
				});

		System.out.println("****************---------------°FIN° Ajout des sessions de formations-----------------****************");

		/*
		 * -------------------------------- Ajout des sessions de soutenances
		 * --------------------------------
		 */
		System.out.println("****************---------------Ajout des sessions de soutenances-----------------****************");

		// Re-using the formatter for soutenance dates, assuming they are fixed but still need to be valid.
		// You might want to adjust these to be dynamically linked to the sessionFormation dates for consistency.
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Re-declare or move up if needed

		// Ensure soutenance dates are after their respective session formation dates
		// For example, soutenance1 should be after session1.dateDebut
		SessionSoutenance soutenance1 = new SessionSoutenance(
				null, // ID will be generated automatically
				session1, // Link with the "Session IA - Paris" training session
				session1.getDateFin().plusDays(5), // 5 days after the session ends (ensures future)
				"Jean Dupont", // Soutenance manager
				"Prévue pour les projets IA",
				new ArrayList<>(), // List of participants
				LocalDateTime.now(), // Creation date
				LocalDateTime.now()  // Last modification
		);

		SessionSoutenance soutenance2 = new SessionSoutenance(
				null, // ID will be generated automatically
				session2, // Link with the "Session Cybersécurité - Lyon" training session
				session2.getDateFin().plusDays(5), // 5 days after the session ends
				"Marie Lemoine", // Soutenance manager
				"Prévue pour les projets de cybersécurité",
				new ArrayList<>(), // List of participants
				LocalDateTime.now(), // Creation date
				LocalDateTime.now()  // Last modification
		);

		SessionSoutenance soutenance3 = new SessionSoutenance(
				null, // ID will be generated automatically
				session3, // Link with the "Session Cloud - Marseille" training session
				session3.getDateFin().plusDays(5), // 5 days after the session ends
				"Luc Martin", // Soutenance manager
				"Prévue pour les projets Cloud",
				new ArrayList<>(), // List of participants
				LocalDateTime.now(), // Creation date
				LocalDateTime.now()  // Last modification
		);

		SessionSoutenance soutenance4 = new SessionSoutenance(
				null, // ID will be generated automatically
				session4, // Link with the "Session Data Science - Toulouse" training session
				session4.getDateFin().plusDays(5), // 5 days after the session ends
				"Sophie Durand", // Soutenance manager
				"Prévue pour les projets Data Science",
				new ArrayList<>(), // List of participants
				LocalDateTime.now(), // Creation date
				LocalDateTime.now()  // Last modification
		);

		SessionSoutenance soutenance5 = new SessionSoutenance(
				null, // ID will be generated automatically
				session5, // Link with the "Session Gestion de Projet - Nantes" training session
				session5.getDateFin().plusDays(5), // 5 days after the session ends
				"Paul Lefevre", // Soutenance manager
				"Prévue pour les projets de gestion de projet",
				new ArrayList<>(), // List of participants
				LocalDateTime.now(), // Creation date
				LocalDateTime.now()  // Last modification
		);

		sessionSoutenanceRepository.save(soutenance1);
		sessionSoutenanceRepository.save(soutenance2);
		sessionSoutenanceRepository.save(soutenance3);
		sessionSoutenanceRepository.save(soutenance4);
		sessionSoutenanceRepository.save(soutenance5);

		System.out.println("****************---------------°FIN° Ajout des session_soutenance -----------------****************");

		/*
		 * -------------------------------- Ajout des évaluations
		 * --------------------------------
		 */
		System.out.println("****************---------------Ajout des évaluations-----------------****************");

		// Retrieve users for evaluation creation
		jury1 = userRepository.findByEmail("elise.benoit@gmail.com").orElse(null);
		jury2 = userRepository.findByEmail("mario.rossi@gmail.com").orElse(null);
		jury3 = userRepository.findByEmail("ines.lopez@gmail.com").orElse(null);

		cand1 = userRepository.findByEmail("remsou28@gmail.com").orElse(null);
		cand2 = userRepository.findByEmail("cdmilandou@gmail.com").orElse(null);
		cand3 = userRepository.findByEmail("jin.kim@gmail.com").orElse(null);
		cand4 = userRepository.findByEmail("eva.leblanc@gmail.com").orElse(null);
		cand5 = userRepository.findByEmail("emily.smith@gmail.com").orElse(null);

		// Create evaluations
		Evaluation eval1 = new Evaluation(
				null,
				jury1,
				cand1,
				"Excellente présentation sur l'intelligence artificielle. Concepts bien maîtrisés.",
				17.5, 16.0, 18.0, 17.0, 16.5,
				0.0 // Mean will be calculated automatically
		);
		eval1.calculerMoyenne();

		Evaluation eval2 = new Evaluation(
				null,
				jury2,
				cand2,
				"Bonne présentation sur la cybersécurité. Quelques imprécisions techniques.",
				15.0, 14.5, 16.0, 15.5, 14.0,
				0.0
		);
		eval2.calculerMoyenne();

		Evaluation eval3 = new Evaluation(
				null,
				jury3,
				cand3,
				"Présentation satisfaisante mais manque d'exemples concrets.",
				13.5, 14.0, 12.5, 13.0, 15.0,
				0.0
		);
		eval3.calculerMoyenne();

		Evaluation eval4 = new Evaluation(
				null,
				jury1,
				cand4,
				"Très bonne maîtrise du sujet et excellente réponse aux questions.",
				18.0, 17.5, 17.0, 18.5, 19.0,
				0.0
		);
		eval4.calculerMoyenne();

		Evaluation eval5 = new Evaluation(
				null,
				jury2,
				cand5,
				"Présentation claire mais contenu un peu superficiel.",
				16.0, 13.5, 17.0, 14.0, 15.5,
				0.0
		);
		eval5.calculerMoyenne();

		Stream.of(eval1, eval2, eval3, eval4, eval5)
				.forEach(evaluation -> {
					evaluationRepository.save(evaluation);
				});

		System.out.println("****************---------------°FIN° Ajout des évaluations-----------------****************");
	}

	// @Bean
	// public JavaMailSender javaMailSender() {
	//     JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	//     mailSender.setHost("smtp.example.com");
	//     mailSender.setPort(587);
	//     mailSender.setUsername("yanatremy09@gmail.com");
	//     mailSender.setPassword("azerty");
	//     return mailSender;
	// }
}