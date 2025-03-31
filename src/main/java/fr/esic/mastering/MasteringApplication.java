package fr.esic.mastering;

import java.text.SimpleDateFormat;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import fr.esic.mastering.entities.Role;
import fr.esic.mastering.entities.RoleType;
import fr.esic.mastering.entities.User;
import fr.esic.mastering.repository.RoleRepository;
import fr.esic.mastering.repository.UserRepository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@SpringBootApplication
public class MasteringApplication implements CommandLineRunner {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

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
		roleCandidate.setRoleUtilisateur(RoleType.CANDIDATE);

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
		User coord1 = new User(null, "Durand", "Luc", sdf.parse("05/02/1985"), "0601122334", "luc.durand@gmail.com",
				"Lyon", "LucCoord123", roleCoordinateur);
		User coord2 = new User(null, "Morel", "Sophie", sdf.parse("12/03/1984"), "0601234567", "sophie.morel@gmail.com",
				"Nice", "SophieCoord123", roleCoordinateur);
		User coord3 = new User(null, "Martin", "Paul", sdf.parse("23/07/1986"), "0601456789", "paul.martin@gmail.com",
				"Toulouse", "PaulCoord123", roleCoordinateur);

		// Apprenants (5)
		User appr1 = new User(null, "Nguyen", "Linh", sdf.parse("15/09/1998"), "0601987654", "linh.nguyen@gmail.com",
				"Hanoi", "LinhApp123", roleApprenant);
		User appr2 = new User(null, "Toure", "Abdou", sdf.parse("02/02/2000"), "0601122544", "abdou.toure@gmail.com",
				"Abidjan", "AbdouApp123", roleApprenant);
		User appr3 = new User(null, "Petit", "Claire", sdf.parse("30/11/1997"), "0601346723", "claire.petit@gmail.com",
				"Lille", "ClaireApp123", roleApprenant);
		User appr4 = new User(null, "Moulin", "Julien", sdf.parse("19/06/1999"), "0601764532",
				"julien.moulin@gmail.com", "Bordeaux", "JulienApp123", roleApprenant);
		User appr5 = new User(null, "Keita", "Fatou", sdf.parse("08/04/1996"), "0601763456", "fatou.keita@gmail.com",
				"Bamako", "FatouApp123", roleApprenant);

		// Jury (5)
		User jury1 = new User(null, "Benoit", "Elise", sdf.parse("25/12/1975"), "0601452367", "elise.benoit@gmail.com",
				"Strasbourg", "EliseJury123", roleJury);
		User jury2 = new User(null, "Rossi", "Mario", sdf.parse("03/06/1980"), "0601543627", "mario.rossi@gmail.com",
				"Rome", "MarioJury123", roleJury);
		User jury3 = new User(null, "Lopez", "Ines", sdf.parse("22/10/1978"), "0601654738", "ines.lopez@gmail.com",
				"Madrid", "InesJury123", roleJury);
		User jury4 = new User(null, "Dubois", "Arnaud", sdf.parse("18/01/1982"), "0601122433",
				"arnaud.dubois@gmail.com", "Marseille", "ArnaudJury123", roleJury);
		User jury5 = new User(null, "Schmidt", "Laura", sdf.parse("11/11/1985"), "0601457689",
				"laura.schmidt@gmail.com", "Berlin", "LauraJury123", roleJury);

		// Superviseurs (2)
		User sup1 = new User(null, "Ahmed", "Karim", sdf.parse("14/08/1979"), "0601998765", "karim.ahmed@gmail.com",
				"Tunis", "KarimSup123", roleSupervisor);
		User sup2 = new User(null, "Becker", "Hans", sdf.parse("07/05/1983"), "0601223344", "hans.becker@gmail.com",
				"Frankfurt", "HansSup123", roleSupervisor);

		// Support Staff (3)
		User staff1 = new User(null, "Roy", "Camille", sdf.parse("29/03/1990"), "0601334455", "camille.roy@gmail.com",
				"Rennes", "CamilleSupport123", roleSupportStaff);
		User staff2 = new User(null, "Zhou", "Wei", sdf.parse("17/12/1992"), "0601445566", "wei.zhou@gmail.com",
				"Shanghai", "WeiSupport123", roleSupportStaff);
		User staff3 = new User(null, "Ali", "Nour", sdf.parse("10/10/1988"), "0601556677", "nour.ali@gmail.com",
				"Casablanca", "NourSupport123", roleSupportStaff);

		// Candidats (10)
		User cand1 = new User(null, "Sow", "Aminata", sdf.parse("09/01/1995"), "0601667788", "aminata.sow@gmail.com",
				"Dakar", "AminataCand123", roleCandidate);
		User cand2 = new User(null, "Garcia", "Lucas", sdf.parse("20/05/1996"), "0601778899", "lucas.garcia@gmail.com",
				"Madrid", "LucasCand123", roleCandidate);
		User cand3 = new User(null, "Kim", "Jin", sdf.parse("01/07/1994"), "0601889900", "jin.kim@gmail.com", "Seoul",
				"JinCand123", roleCandidate);
		User cand4 = new User(null, "Leblanc", "Eva", sdf.parse("14/02/1993"), "0601990011", "eva.leblanc@gmail.com",
				"Lyon", "EvaCand123", roleCandidate);
		User cand5 = new User(null, "Smith", "Emily", sdf.parse("03/03/1992"), "0601223345", "emily.smith@gmail.com",
				"London", "EmilyCand123", roleCandidate);
		User cand6 = new User(null, "Fernandez", "Carlos", sdf.parse("06/06/1991"), "0601334456",
				"carlos.fernandez@gmail.com", "Barcelone", "CarlosCand123", roleCandidate);
		User cand7 = new User(null, "Kouassi", "Yao", sdf.parse("13/07/1990"), "0601445567", "yao.kouassi@gmail.com",
				"Abidjan", "YaoCand123", roleCandidate);
		User cand8 = new User(null, "Ivanov", "Anna", sdf.parse("22/08/1989"), "0601556678", "anna.ivanov@gmail.com",
				"Moscou", "AnnaCand123", roleCandidate);
		User cand9 = new User(null, "Omar", "Lina", sdf.parse("27/09/1987"), "0601667789", "lina.omar@gmail.com",
				"Rabat", "LinaCand123", roleCandidate);
		User cand10 = new User(null, "Park", "Min", sdf.parse("30/10/1986"), "0601778890", "min.park@gmail.com",
				"Busan", "MinCand123", roleCandidate);

		Stream.of(coord1, coord2, coord3, appr1, appr2, appr3, appr4, appr5, jury1, jury2, jury3, jury4, jury5, sup1,
				sup2, staff1, staff2, staff3, cand1, cand2, cand3, cand4, cand5, cand6, cand7, cand8, cand9, cand10)
				.forEach(user -> {
					userRepository.save(user);
				});

		System.out.println("****************---------------°FIN° Ajout des users-----------------****************");

		/*
		 * -------------------------------- 
		 * Fin d'ajout des Users
		 * --------------------------------
		 */
		
		/*
		 * --------------------------------
		 * Ajout 
		 * --------------------------------
		 */

	}

	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.example.com");
		mailSender.setPort(587);
		mailSender.setUsername("yanatremy09@gmail.com");
		mailSender.setPassword("azerty");
		return mailSender;
	}

}
