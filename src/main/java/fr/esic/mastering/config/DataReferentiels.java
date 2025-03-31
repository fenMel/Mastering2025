//package fr.esic.mastering.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import fr.esic.mastering.entities.Formation;
//import fr.esic.mastering.entities.Referentiels;
//import fr.esic.mastering.repository.FormationRepository;
//import fr.esic.mastering.repository.RefRepository;
//
//@Component
//public class DataReferentiels implements CommandLineRunner {
//
//    @Autowired
//    private RefRepository refRepository;
//    @Autowired
//    private FormationRepository formationRepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//
//          // Création des formations
//        Formation BtsSlamFormation = new Formation();
//        BtsSlamFormation.setNom("BTS SLAM");
//        BtsSlamFormation.setNiveau("2");
//        BtsSlamFormation.setCodeRncp("bts00");
//        BtsSlamFormation.setDescription("DEVELOPPEMENT WEB");
//        BtsSlamFormation.setDuree("1 an");
//        BtsSlamFormation.setPrerequis("bac");
//        BtsSlamFormation.setObjectifs("Développer des API");
//
//        Formation BtsSisrFormation = new Formation();
//        BtsSisrFormation.setNom("BTS SISR");
//        BtsSisrFormation.setNiveau("2");
//        BtsSisrFormation.setCodeRncp("bts01");
//        BtsSisrFormation.setDescription("RESEAU INFORMATIQUE");
//        BtsSisrFormation.setDuree("1 an");
//        BtsSisrFormation.setPrerequis("bac");
//        BtsSisrFormation.setObjectifs("Résoudre des problèmes informatiques");
//
//        Formation BtsCielFormation = new Formation();
//        BtsCielFormation.setNom("BTS CIEL");
//        BtsCielFormation.setNiveau("2");
//        BtsCielFormation.setCodeRncp("bts02");
//        BtsCielFormation.setDescription("DEVELOPPEMENT INFORMATIQUE");
//        BtsCielFormation.setDuree("1 an");
//        BtsCielFormation.setPrerequis("bac");
//        BtsCielFormation.setObjectifs("Résoudre des problèmes informatiques");
//
//        formationRepository.save(BtsSlamFormation);
//        formationRepository.save(BtsSisrFormation);
//        formationRepository.save(BtsCielFormation);
//
//
//        // Création des référentiels
//        Referentiels BtsSioReferentiels = new Referentiels();
//        BtsSioReferentiels.setNom("SLAM");
//        BtsSioReferentiels.setDescription("Intermédiaire");
//        BtsSioReferentiels.setObjectifs("Développement");
//        BtsSioReferentiels.setCriteres("bac");
//        BtsSioReferentiels.setFormation(BtsSlamFormation); // Association
//
//        Referentiels BtsCielReferentiels = new Referentiels();
//        BtsCielReferentiels.setNom("CIEL");
//        BtsCielReferentiels.setDescription("Débutant");
//        BtsCielReferentiels.setObjectifs("Développement");
//        BtsCielReferentiels.setCriteres("bac");
//        BtsCielReferentiels.setFormation(BtsSisrFormation); // Association
//
//        Referentiels BtsReferentiels = new Referentiels();
//        BtsReferentiels.setNom("CIEL");
//        BtsReferentiels.setDescription("Débutant");
//        BtsReferentiels.setObjectifs("Développement");
//        BtsReferentiels.setCriteres("bac");
//        BtsReferentiels.setFormation(BtsCielFormation); // Association
//
//        refRepository.save(BtsSioReferentiels);
//        refRepository.save(BtsCielReferentiels);
//        refRepository.save(BtsReferentiels);
//
//        System.out.println("Données initialisées avec succès !");
//    }
//}
