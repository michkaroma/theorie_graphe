package classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Graphe {
    HashMap<String, Sommet> sommets;

    //constructeur
    public Graphe() {
        sommets = new HashMap<>();
    }


    public void recupFichier(String adresse) {
        try {
            File fichier = new File(adresse);
            Scanner sc = new Scanner(fichier);
            while (sc.hasNextLine()) {
                String s = sc.nextLi
                String[] parts = s.split(";");
                Arc arc = new Arc(parts[0], parts[1], Integer.valueOf(parts[2]));
                if (!sommets.containsKey(parts[0])) sommets.put(parts[0], new Sommet(parts[0]));
                if (!sommets.containsKey(parts[1])) sommets.put(parts[1], new Sommet(parts[1]));
                sommets.get(parts[0]).addArcs(arc);
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

    public void afficherItineraire(String depart, String arrive) {
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
}
