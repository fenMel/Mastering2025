package fr.esic.mastering;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Stream;

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
import fr.esic.mastering.repository.SessionFormationRepository;
import fr.esic.mastering.repository.SessionSoutenanceRepository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
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
	private SessionFormationRepository sessionFormationRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	

	public static void main(String[] args) {
		SpringApplication.run(MasteringApplication.class, args);
		System.out.println("lancement terminé");
	}
	


	@Override
	public void run(String... args) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

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
		 * -------------------------------- Fin d'ajout des roles
		 * --------------------------------
		 */

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

		



		

		/*
		 * -------------------------------- 
		 * Fin d'ajout des Users
		 * --------------------------------
		 */



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
		 * --------------------------------
		 * Fin d'ajout des Formations
		 * --------------------------------
		 */


		/*
		 * -------------------------------- Ajout des sessions de formations
		 * --------------------------------
		 */


		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		System.out.println("****************---------------Ajout des sessions de formations-----------------****************");


			SessionFormation session1 = new SessionFormation(
                null,
                "Session IA - Paris",
                "Session avancée sur l’intelligence artificielle.",
                LocalDate.parse("01/06/2025" , formatter),
                LocalDate.parse("01/12/2025" , formatter),
                iaFormation,
                new ArrayList<>()
        );
				SessionFormation session2 = new SessionFormation(
					null,
					"Session Cybersécurité - Lyon",
					"Session sécurité offensive et défensive.",
					LocalDate.parse("15/06/2025", formatter),
					LocalDate.parse("15/12/2025", formatter),
					cyberFormation,
					new ArrayList<>()
			);
	
			SessionFormation session3 = new SessionFormation(
					null,
					"Session Cloud - Marseille",
					"Session sur les solutions cloud modernes.",
					LocalDate.parse("01/07/2025" , formatter),
					LocalDate.parse("01/01/2026" , formatter),
					cloudFormation,
					new ArrayList<>()
			);
			
	
	
			SessionFormation session4 = new SessionFormation(
					null,
					"Session Data Science - Toulouse",
					"Session pour apprendre à analyser des données.",
					LocalDate.parse("01/09/2025" , formatter),
					LocalDate.parse("01/03/2026" , formatter),
					dataFormation,
					new ArrayList<>()
			);
			
	
			SessionFormation session5 = new SessionFormation(
					null,
					"Session Gestion de Projet - Nantes",
					"Session sur les méthodes agiles et traditionnelles.",
					LocalDate.parse("01/10/2025" , formatter),
					LocalDate.parse("01/04/2026" , formatter),
					gestionFormation,
					new ArrayList<>()
			);


			Stream.of(session1, session2, session3, session4, session5)
				.forEach(session -> {
					sessionFormationRepository.save(session);
				});

		System.out.println("****************---------------°FIN° Ajout des users-----------------****************");

		
		};

		/*
		 * --------------------------------
		 * Fin d'ajout des sessions de formations
		 * --------------------------------
		 */
		
		/*
		 * --------------------------------
		 * Ajout 
		 * --------------------------------
		 */

	


	// @Bean
	// public JavaMailSender javaMailSender() {
	// 	JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	// 	mailSender.setHost("smtp.example.com");
	// 	mailSender.setPort(587);
	// 	mailSender.setUsername("yanatremy09@gmail.com");
	// 	mailSender.setPassword("azerty");
	// 	return mailSender;
	// }

}

