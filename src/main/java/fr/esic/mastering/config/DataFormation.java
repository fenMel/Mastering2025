//package fr.esic.mastering.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import fr.esic.mastering.entities.Formation;
//import fr.esic.mastering.repository.FormationRepository;
//
//    @Component
//public class DataFormation implements CommandLineRunner {
//
//    @Autowired
//    private FormationRepository formationRepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//        // Création des formations
//         // Création des formations
//         Formation BtsSlamFormation = new Formation();
//         BtsSlamFormation.setNom("BTS SLAM");
//         BtsSlamFormation.setNiveau("2");
//         BtsSlamFormation.setCodeRncp("bts00");
//         BtsSlamFormation.setDescription("DEVELOPPEMENT WEB");
//         BtsSlamFormation.setDuree("1 an");
//         BtsSlamFormation.setPrerequis("bac");
//         BtsSlamFormation.setObjectifs("Développer des API");
// 
//         Formation BtsSisrFormation = new Formation();
//         BtsSisrFormation.setNom("BTS SISR");
//         BtsSisrFormation.setNiveau("2");
//         BtsSisrFormation.setCodeRncp("bts01");
//         BtsSisrFormation.setDescription("RESEAU INFORMATIQUE");
//         BtsSisrFormation.setDuree("1 an");
//         BtsSisrFormation.setPrerequis("bac");
//         BtsSisrFormation.setObjectifs("Résoudre des problèmes informatiques");
// 
//         Formation BtsCielFormation = new Formation();
//         BtsCielFormation.setNom("BTS CIEL");
//         BtsCielFormation.setNiveau("2");
//         BtsCielFormation.setCodeRncp("bts02");
//         BtsCielFormation.setDescription("DEVELOPPEMENT INFORMATIQUE");
//         BtsCielFormation.setDuree("1 an");
//         BtsCielFormation.setPrerequis("bac");
//         BtsCielFormation.setObjectifs("Résoudre des problèmes informatiques");
// 
//         formationRepository.save(BtsSlamFormation);
//         formationRepository.save(BtsSisrFormation);
//         formationRepository.save(BtsCielFormation);
// 
//}
//}
