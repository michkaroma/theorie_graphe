package classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Graphe {
    private HashMap<String, Sommet> sommets;
    private int capaciteCamion = 15;

    //constructeur
    public Graphe() {
        sommets = new HashMap<>();
    }

    // Récupère les informations du graphe.txt
    public void recupFichier(String adresse) {
        try {
            File fichier = new File(adresse);
            Scanner sc = new Scanner(fichier);
            while (sc.hasNextLine()) {
                String s = sc.nextLine();
                String[] parts = s.split(";");
                Arc arc = new Arc(parts[0].trim(), parts[1].trim(), Integer.valueOf(parts[2].trim()));
                if (!sommets.containsKey(parts[0].trim())) sommets.put(parts[0].trim(), new Sommet(parts[0].trim()));
                if (!sommets.containsKey(parts[1].trim())) sommets.put(parts[1].trim(), new Sommet(parts[1].trim()));
                sommets.get(parts[0].trim()).addArcSortants(arc);
                sommets.get(parts[1].trim()).addArcEntrants(arc);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }
    }

    // Récupère les informations du grapheCapacite.txt
    public void recupFichierCapacite(String adresse) {
        try {
            File fichier = new File(adresse);
            Scanner sc = new Scanner(fichier);
            while (sc.hasNextLine()) {
                String s = sc.nextLine();
                String[] parts = s.split(";");
                Arc arc = new Arc(parts[0].trim(), parts[1].trim(), Integer.valueOf(parts[2].trim()), Integer.valueOf(parts[3].trim()));
                if (!sommets.containsKey(parts[0].trim())) sommets.put(parts[0].trim(), new Sommet(parts[0].trim()));
                if (!sommets.containsKey(parts[1].trim())) sommets.put(parts[1].trim(), new Sommet(parts[1].trim()));
                sommets.get(parts[0].trim()).addArcSortants(arc);
                sommets.get(parts[1].trim()).addArcEntrants(arc);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }
    }


    public void dijkstra(String depart) {
        PriorityQueue<Sommet> liste = new PriorityQueue<>();
        for (Sommet sommet : sommets.values()) {
            sommet.verif = false;
            sommet.longueur = Integer.MAX_VALUE;
            sommet.predecesseur = null;
        }
        sommets.get(depart).longueur = 0;
        liste.add(sommets.get(depart));

        while (!liste.isEmpty()) {
            Sommet courant = liste.poll();
            if(courant.verif) continue;
            courant.verif = true;

            for (Arc arc : courant.getArcsSortants()) {
                if(courant.getLongueur()+arc.getDistance()<sommets.get(arc.getArrive()).getLongueur()){
                    sommets.get(arc.getArrive()).setLongueur(arc.getDistance()+courant.getLongueur());
                    sommets.get(arc.getArrive()).setPredecesseur(courant.id);
                    liste.add(sommets.get(arc.getArrive()));
                }
            }
        }
    }


    public void cul_de_sac(Sommet sommet){

    }

    public void afficherItineraireDijkstra(String depart, String arrive) {
        // On lance Dijkstra à partir du sommet de départ
        dijkstra(depart);

        Sommet dest = sommets.get(arrive);
        if (dest == null) {
            System.out.println("Sommet d'arrivée inconnu : " + arrive);
            return;
        }

        if (dest.longueur == Integer.MAX_VALUE) {
            System.out.println("Aucun chemin de " + depart + " à " + arrive);
            return;
        }

        // Reconstruction du chemin en remontant les prédécesseurs
        List<String> chemin = new ArrayList<>();
        Sommet courant = dest;

        while (courant != null) {
            chemin.add(courant.id);
            if (courant.predecesseur == null) break;
            courant = sommets.get(courant.predecesseur);
        }

        // Si on n'est jamais passé par le depart, problème
        if (!chemin.get(chemin.size() - 1).equals(depart)) {
            System.out.println("Aucun chemin de " + depart + " à " + arrive);
            return;
        }

        // Le chemin est à l'envers, on le remet dans le bon sens
        Collections.reverse(chemin);

        // Affichage
        System.out.print("Itinéraire de " + depart + " à " + arrive + " : ");
        for (int i = 0; i < chemin.size(); i++) {
            System.out.print(chemin.get(i));
            if (i < chemin.size() - 1) System.out.print(" -> ");
        }
        System.out.println();
        System.out.println("Longueur totale = " + dest.longueur);
    }




    // THEME 2 : APPROCHE 1 (Plus petit voisins proches)
    public List<Sommet> tourneePPV(String depart) {

        Sommet courant = sommets.get(depart);  // On récupère le sommet de depart

        List<Sommet> chemin = new ArrayList<>();  // Ordre dans lequel on été visités chaque sommets
        chemin.add(courant);

        Set<Sommet> visites = new HashSet<>(); // liste qui permet de savoir si le sommet est visités ou non
        visites.add(courant);

        // La distance de départ est de 0
        int distance = 0;

        // Tant que tous les sommets n'ont pas été visités
        while (visites.size() != sommets.size()) {

            Arc meilleurArc = null;
            int meilleur = Integer.MAX_VALUE;

            // On parcourt les arcs sortants de la station courant
            for (Arc arc : courant.getArcsSortants()) {
                Sommet voisin = sommets.get(arc.getArrive()); // On récupère un des sommets voisin à celui de courant
                if (!visites.contains(voisin) && arc.getDistance() < meilleur) { // et si il n'a pas été visité, et que son arc est le plus petit de tous les voisins
                    meilleur = arc.getDistance();
                    meilleurArc = arc;                                           // on sauvegarde l'arc en question --> Plus proche voisin
                }
            }

            // On ajoute la distance de l'arc le plus court dans le chemin total
            distance += meilleur;

            // Cas pour un sommet isolé rencontré
            if (meilleurArc == null) {
                System.out.println("Attention : sommet isolé : " + courant.getId());
                distance+=meilleur;
                break;
            }

            // Le nouveau sommet courant devient celui qui à le plus petit arc (le plus proche voisin)
            courant = sommets.get(meilleurArc.getArrive());

            chemin.add(courant);                                // On ajoute le sommet dans le chemin
            visites.add(courant);                               // et on l'ajoute à la liste des sommets visités
        }

        // Retour au dépôt
        chemin.add(sommets.get(depart));

        // Affichage du chemin
        afficherTourneePPV(chemin);
        distance = distance*2;
        System.out.println("Distance totale = " + distance);
        return chemin;
    }

    // Affiche la tournée effectuer par PPV
    public void afficherTourneePPV(List<Sommet> tournee) {
        for (int i=0; i<tournee.size(); i++) {
            System.out.print(tournee.get(i).getId());
            if (i < tournee.size()-1) System.out.println(" -> ");
        }
        System.out.println();
    }


    // Créer un arbre couvrant de poids minimum
    public List<Arc> primMST(String depart) {

        // Stockage du MST final
        List<Arc> mst = new ArrayList<>();

        // Sommets déjà dans l'arbre
        Set<Sommet> dedans = new HashSet<>();

        // File de priorité des arcs candidats qui les trie dans un ordre croissant
        PriorityQueue<Arc> file = new PriorityQueue<>(Comparator.comparingInt(Arc::getDistance));

        Sommet start = sommets.get(depart);
        dedans.add(start);

        // Ajoute toutes les arêtes sortantes du sommet de départ
        file.addAll(start.getArcsSortants());

        // Tant que le graphe n'est pas "complet"
        while (dedans.size() < sommets.size() && !file.isEmpty()) {

            Arc arc = file.poll();  // On sort l'arc le plus petit (trié directement dans la file) de la file

            Sommet arrive = sommets.get(arc.getArrive());

            // On ne prend l'arc que s'il ajoute un nouveau sommet sinon cela crée un cycle
            if (dedans.contains(arrive)) continue;

            // On ajoute l'arc au MST
            mst.add(arc);
            dedans.add(arrive);

            // On ajoute dans la file les nouveau sommets voisins à celui "d'arrive"
            for (Arc a : arrive.getArcsSortants()) {
                Sommet next = sommets.get(a.getArrive());
                if (!dedans.contains(next)) {               // seulement si le sommet voisin n’est pas encore dans le MST
                    file.add(a);
                }
            }
        }

        return mst;
    }


    // THEME 2 : APPROCHE 2 (MST) --> convertit l'arbre en tournée pour le camion
    public List<Sommet> tourneeMST(String depart) {

        List<Arc> mst = primMST(depart);

        // On convertit les arcs du MST en graphe non orienté
        Map<Sommet, List<Sommet>> arbre = new HashMap<>();
        for (Sommet s : sommets.values()) {
            arbre.put(s, new ArrayList<>());
        }
        // Ajout des arcs dans les deux sens
        for (Arc arc : mst) {
            Sommet u = sommets.get(arc.getOrigine());
            Sommet v = sommets.get(arc.getArrive());
            arbre.get(u).add(v);
            arbre.get(v).add(u);
        }

        // DFS pour construire la tournée (préordre)
        List<Sommet> tournee = new ArrayList<>();  // tournée == une visite de chaque sommet une fois
        Set<Sommet> visite = new HashSet<>();

        Sommet start = sommets.get(depart);
        dfsMST(start, arbre, visite, tournee);

        // Retour au dépôt
        tournee.add(start);

        // Tournee sans "shortcutting"
        //afficherTourneeMST(tournee);
        //return tournee;

        // Tournée avec "shortcutting"
        List<Sommet> optimisee = shortcutting(tournee);

        // Affichage du graphe
        afficherTourneeMSTC(optimisee);
        return optimisee;
    }

    // Reconstruit le chemin pour revenir au centre de traitement
    public List<Sommet> reconstruireChemin(Sommet dest) {

        List<Sommet> chemin = new ArrayList<>();
        Sommet courant = dest;

        while (courant != null) {
            chemin.add(courant);

            if (courant.predecesseur == null)
                break;

            courant = sommets.get(courant.predecesseur);
        }

        Collections.reverse(chemin);
        return chemin;
    }

    // Permet de faire le shortcutting entre les sommets
    public List<Sommet> shortcutting(List<Sommet> tournee) {
        if (tournee.isEmpty())
            return tournee;

        List<Sommet> optimisee = new ArrayList<>();

        Sommet prev = tournee.get(0);
        optimisee.add(prev);

        for (int i = 1; i < tournee.size(); i++) {

            Sommet courant = tournee.get(i);

            // Calcul du plus court chemin prev -> courant
            dijkstra(prev.getId());

            // Reconstruction du chemin
            List<Sommet> chemin = reconstruireChemin(courant);

            // On ajoute la route sans répéter prev
            if (chemin.size() > 1)
                optimisee.addAll(chemin.subList(1, chemin.size()));
            else
                optimisee.add(courant);

            prev = courant;
        }

        return optimisee;
    }


    public void dfsMST(Sommet racine, Map<Sommet, List<Sommet>> arbre, Set<Sommet> visite, List<Sommet> tour) {
        // Création d'une liste (qui permet de faire file/pile)
        LinkedList<Sommet> pile = new LinkedList<>();
        pile.add(racine);

        // Tant que la pile n'est pas vide --> tant que tous les sommets n'ont pas été traités
        while (!pile.isEmpty()) {
            Sommet courant = pile.poll();

            if (!visite.contains(courant)) {
                visite.add(courant);
                tour.add(courant);

                // On ajoute les voisins dans l'ordre inverse, car la pile inverse l’ordre d’exploration
                List<Sommet> voisins = arbre.get(courant);
                if (voisins != null) {
                    // copie pour éviter de modifier l’arbre
                    List<Sommet> copie = new ArrayList<>(voisins);  // La liste copie celle de "voisins"
                    Collections.reverse(copie);

                    for (Sommet voisin : copie) {
                        if (!visite.contains(voisin)) {
                            pile.push(voisin);
                        }
                    }
                }
            }
        }
    }

    public int distanceTournee(List<Sommet> tournee) {
        if (tournee.size() < 2)
            return 0;

        int total = 0;
        for (int i = 0; i < tournee.size() - 1; i++) {

            Sommet u = tournee.get(i);
            Sommet v = tournee.get(i + 1);

            // Lancer Dijkstra depuis le sommet u
            dijkstra(u.getId());

            int dist = sommets.get(v.getId()).getLongueur();

            total += dist;
        }
        return total;
    }

    public void afficherTourneeMST(List<Sommet> tournee) {

        System.out.print("Tournee MST : ");

        for (int i = 0; i < tournee.size(); i++) {
            System.out.print(tournee.get(i).getId());
            if (i < tournee.size() - 1) {
                System.out.print(" -> ");
            }
        }

        int total = distanceTournee(tournee);
        System.out.println("\nDistance totale : " + total);
    }


    private Arc trouverArc(Sommet u, Sommet v) {
        for (Arc a : u.getArcsSortants()) {
            if (a.getArrive().equals(v.getId()))
                return a;
        }
        return null; // normalement jamais ici si le shortcutting a bien fonctionné
    }

    public void afficherTourneeMSTC(List<Sommet> tournee) {
        int charge = 0;
        int distanceTotale = 0;
        int nbRetours = 0;

        System.out.println("Tournee MST avec capacite");
        System.out.println("Capacite du camion : " + capaciteCamion);
        System.out.print("Chemin : ");

        for (int i = 0; i < tournee.size() - 1; i++) {

            Sommet u = tournee.get(i);
            Sommet v = tournee.get(i + 1);
            Arc arc = trouverArc(u, v);

            int dechets = arc.getDechets();
            int dist = arc.getDistance();

            // Test de dépassement de capacité
            if (charge + dechets > capaciteCamion) {
                // Retour au centre
                System.out.println("Retour au " + tournee.get(0).getId() + " -> ");
                nbRetours++;
                charge = 0; // camion vidé
            }

            // Charger les déchets
            charge += dechets;
            distanceTotale += dist;

            System.out.print(u.getId() + "[" + charge + "]" + " -> ");
            if (i % 4 == 0) {
                System.out.println();
            }
        }

        // Dernier sommet
        System.out.println(tournee.get(tournee.size() - 1).getId());

        System.out.println("\nDistance totale      : " + distanceTotale);
        System.out.println("Retours au depot     : " + nbRetours);
        System.out.println("Charge restante      : " + charge + "/" + capaciteCamion);
    }


}
