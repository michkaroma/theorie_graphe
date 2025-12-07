package main;

import classes.Graphe;
import classes.Sommet;

import java.util.*;

public class Interface {

    private final Graphe g = new Graphe("graphe.txt");
    private final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        new Interface().run();
    }

    private void run() {
        while (true) {
            System.out.println("Authentification");
            System.out.println("1) Collectivité");
            System.out.println("2) Entreprise de collecte");
            System.out.println("0) Quitter");
            System.out.print("Choix: ");
            String in = sc.nextLine().trim();
            switch (in) {
                case "1": menuCollectivite(); break;
                case "2": menuEntreprise(); break;
                case "0": System.out.println("Au revoir."); return;
                default: System.out.println("Choix invalide.");
            }
        }
    }

    // Collectivité
    private void menuCollectivite() {
        while (true) {
            System.out.println("\n Menu Collectivité ");
            System.out.println("1) Soumettre le plan du territoire (charger fichier graphe)");
            System.out.println("2) Signaler des modifications de circulation");
            System.out.println("3) Indiquer des contraintes horaires");
            System.out.println("4) Consulter les quantités de déchets récoltés");
            System.out.println("9) Retour");
            System.out.print("Choix: ");
            String c = sc.nextLine().trim();
            switch (c) {
                case "1": soumettrePlan(); break;
                case "2": signalerModifs(); break;
                case "3": contraintesHoraires(); break;
                case "4": consulterQuantites(); break;
                case "9": return;
                default: System.out.println("Choix invalide.");
            }
        }
    }

    private void soumettrePlan() {
        System.out.print("Chemin du fichier graphe (ex: graphe.txt): ");
        String path = sc.nextLine().trim();
        try {
            g.recupFichier(path);
            System.out.println("Plan chargé depuis: " + path);
        } catch (Exception ex) {
            System.out.println("Erreur chargement plan: " + ex.getMessage());
        }
    }

    private void signalerModifs() {
        System.out.println("Décrivez les modifications (travaux, déviations), finissez par une ligne vide:");
        StringBuilder sb = new StringBuilder();
        while (true) {
            String line = sc.nextLine();
            if (line.trim().isEmpty()) break;
            sb.append(line).append('\n');
        }
        System.out.println("Modifications enregistrées (démo):\n" + sb);
    }

    private void contraintesHoraires() {
        System.out.print("Heure début (HH:MM): ");
        String deb = sc.nextLine().trim();
        System.out.print("Heure fin (HH:MM): ");
        String fin = sc.nextLine().trim();
        System.out.println("Contraintes enregistrées (démo): " + deb + " - " + fin);
    }

    private void consulterQuantites() {
        System.out.println("Quantités (démo): Verre 12.3t, Papier/Carton 18.7t, OM 34.1t");
    }

    //  Entreprise
    private void menuEntreprise() {
        while (true) {
            System.out.println("\n--- Menu Entreprise ---");
            System.out.println("1) Calculer les itinéraires de ramassage");
            System.out.println("2) Planifier les jours de collecte");
            System.out.println("3) Programmer les camions");
            System.out.println("4) Consulter les tournées planifiées");
            System.out.println("5) Exporter des rapports");
            System.out.println("9) Retour");
            System.out.print("Choix: ");
            String c = sc.nextLine().trim();
            switch (c) {
                case "1": menuCalculItineraires(); break;
                case "2": planifierJours(); break;
                case "3": programmerCamions(); break;
                case "4": consulterTournees(); break;
                case "5": exporterRapports(); break;
                case "9": return;
                default: System.out.println("Choix invalide.");
            }
        }
    }

    private void menuCalculItineraires() {
        while (true) {
            System.out.println("\n--- Calcul d'itinéraires ---");
            System.out.println("1) Thème 1 · Pb1 · Hypothèse 1 (A/R entre deux points)");
            System.out.println("2) Thème 1 · Pb1 · Hypothèse 2 (TSP sous-ensemble) [console intégrée Graphe]");
            System.out.println("3) Thème 1 · Pb2 (TSP toutes maisons)");
            System.out.println("4) Thème 2 · Approche 1 (PPV)");
            System.out.println("5) Thème 2 · Approche 2 (MST)");
            System.out.println("9) Retour");
            System.out.print("Choix: ");
            String c = sc.nextLine().trim();
            switch (c) {
                case "1": runT1Pb1H1(); break;
                case "2": runT1Pb1H2(); break;
                case "3": runT1Pb2(); break;
                case "4": runPPV(); break;
                case "5": runMST(); break;
                case "9": return;
                default: System.out.println("Choix invalide.");
            }
        }
    }

    private void runT1Pb1H1() {
        System.out.print("Arrivée: "); String arr = sc.nextLine().trim();
        if (arr.isEmpty()) { System.out.println("Champs requis."); return; }
        System.out.println("[Exécution] Th1/Pb1/H1");
        g.afficherItineraireTh1Pb1Hp1("centre_de_traitement", arr);
    }

    private void runT1Pb1H2() {
        System.out.println("Vous allez être guidé par la console interne de Graphe (nombre d'adresses puis leurs IDs).");
        g.afficherItineraireTh1Pb1Hp2("centre_de_traitement");
    }

    private void runT1Pb2() {
        System.out.println("[Exécution] Th1/Pb2");
        g.afficherItineraireTh1Pb2("centre_de_traitement");
    }

    private void runPPV() {
        System.out.print("Départ: "); String dep = sc.nextLine().trim();
        if (dep.isEmpty()) { System.out.println("Départ requis."); return; }
        System.out.println("[Exécution] Th2/PPV");
        List<Sommet> tour = g.tourneePPV(dep);
        printTour(tour);
    }

    private void runMST() {
        System.out.print("Départ: "); String dep = sc.nextLine().trim();
        if (dep.isEmpty()) { System.out.println("Départ requis."); return; }
        System.out.println("[Exécution] Th2/MST");
        List<Sommet> tour = g.tourneeMST(dep);
        printTour(tour);
    }

    // ============================== Autres (Entreprise) ==============================
    private void planifierJours() {
        System.out.print("Jours de collecte (ex: Lun,Mer,Ven): ");
        String jours = sc.nextLine().trim();
        System.out.println("Planification enregistrée (démo): " + jours);
    }

    private void programmerCamions() {
        System.out.print("Nombre de camions: ");
        String nb = sc.nextLine().trim();
        System.out.println("Camions programmés (démo): " + nb);
    }

    private void consulterTournees() {
        System.out.println("Tournées planifiées (démo): afficher ici la liste des tournées.");
    }

    private void exporterRapports() {
        System.out.print("Nom du rapport (ex: rapport.csv): ");
        String nom = sc.nextLine().trim();
        System.out.println("Rapport exporté (démo): " + nom);
    }

    // ============================== Utils ==============================
    private void printTour(List<Sommet> tour) {
        if (tour == null || tour.isEmpty()) { System.out.println("(tour vide)"); return; }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tour.size(); i++) {
            sb.append(tour.get(i).getId());
            if (i < tour.size() - 1) sb.append(" -> ");
        }
        System.out.println(sb);
    }
}