package classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Graphe {
    private HashMap<String, Sommet> sommets;

    public Graphe() {
        this.sommets = new HashMap<>();
    }

    public void recupFichier(String adresse) {
        try {
            File fichier = new File(adresse);
            Scanner sc = new Scanner(fichier);
            while (sc.hasNextLine()) {
                String s = sc.nextLine();
                String[] parts = s.split(";");
                Arc arc = new Arc(parts[0], parts[1], Integer.valueOf(parts[2]));
                if (!sommets.containsKey(parts[0])) sommets.put(parts[0], new Sommet(parts[0]));
                if (!sommets.containsKey(parts[1])) sommets.put(parts[1], new Sommet(parts[1]));
                sommets.get(parts[0]).addArcs(arc);
                sommets.get(parts[1]).addArcs(arc);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }
    }

    public void dijkstra(String depart) {
        if (!sommets.containsKey(depart)) return;

        for (Sommet s : sommets.values()) {
            s.setLongueur(Integer.MAX_VALUE);
            s.verif = false;
            s.setPredecesseur(null);
        }

        Sommet sDepart = sommets.get(depart);
        sDepart.setLongueur(0);

        PriorityQueue<Sommet> file = new PriorityQueue<>();
        file.add(sDepart);

        while (!file.isEmpty()) {
            Sommet courant = file.poll();

            if (courant.verif) continue;
            courant.verif = true;

            for (Arc arc : courant.getArcsSortants()) {
                Sommet voisin = sommets.get(arc.getArrive());

                if (!voisin.verif) {
                    int nouveauChemin = courant.getLongueur() + arc.getDistance();

                    if (nouveauChemin < voisin.getLongueur()) {
                        voisin.setLongueur(nouveauChemin);
                        voisin.setPredecesseur(courant.getId());

                        file.remove(voisin);
                        file.add(voisin);
                    }
                }
            }
        }
    }

    public void afficherItineraire(String depart, String arrive) {
        dijkstra(depart);

        if (!sommets.containsKey(arrive)) {
            System.out.println("Sommet d'arrivée inconnu.");
            return;
        }

        Sommet sArrive = sommets.get(arrive);

        if (sArrive.getLongueur() == Integer.MAX_VALUE) {
            System.out.println("Aucun chemin trouvé.");
            return;
        }

        List<String> chemin = new ArrayList<>();
        String courantId = arrive;

        while (courantId != null) {
            chemin.add(courantId);
            courantId = sommets.get(courantId).getPredecesseur();
        }

        Collections.reverse(chemin);

        System.out.println("Itinéraire : " + String.join(" -> ", chemin));
        System.out.println("Distance : " + sArrive.getLongueur());
    }

    public HashMap<String, Sommet> getSommets() {
        return sommets;
    }
}