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
                String ligne = sc.nextLine().trim();

                if (ligne.isEmpty()) continue;

                // Modification ici : on coupe sur les espaces (ou plusieurs espaces)
                String[] parts = ligne.split("\\s+");

                if (parts.length >= 3) {
                    String idOrigine = parts[0];
                    String idArrivee = parts[1];
                    int distance = Integer.parseInt(parts[2]);

                    if (!sommets.containsKey(idOrigine)) {
                        sommets.put(idOrigine, new Sommet(idOrigine));
                    }
                    if (!sommets.containsKey(idArrivee)) {
                        sommets.put(idArrivee, new Sommet(idArrivee));
                    }

                    Arc nouvelArc = new Arc(idOrigine, idArrivee, distance);
                    sommets.get(idOrigine).addArcs(nouvelArc);
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
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

    public void afficherItineraireDijkstra(String depart, String arrive) {
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