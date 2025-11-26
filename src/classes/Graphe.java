package classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Graphe {

    // Attributs
    private HashMap<String, Sommet> sommets;

    // Constructeur
    public Graphe() {
        sommets = new HashMap<>();
    }


    // Méthodes

    // Récupére le graphe (la ville)
    public void recupFichier(String nameFile) throws FileNotFoundException {
        File file = new File(nameFile);
        Scanner sc = new Scanner(file);

        while (sc.hasNextLine()) {  // On lit le fichier tant qu'il reste une ligne à lire

            // On récupère toutes les informations fournies par le fichier texte pour créer le graphe
            String line = sc.nextLine();
            String[] splited = line.split(";",3);
            String idDepart = splited[0];
            String idArrive = splited[1];
            int longueur = Integer.parseInt(splited[2]);  // Convertit la valeur enregistré dans le fichier en un int
                                                          // ATTENTION !!!!  Si la longueur change il faut aussi changer le type de la variable

            // On ajoute ensuite les sommets dans la liste s'ils n'étaient pas dedans
            Sommet origine = sommets.get(idDepart);                     // On regarde si le sommet est contenu dans la liste
            if (origine == null) {                                      // Si non,
                sommets.put(idDepart, origine = new Sommet(idDepart));  // on l'ajoute en tant que nouveau sommet de la liste
            }
            Sommet arrive = sommets.get(idArrive);
            if (arrive == null) {
                sommets.put(idArrive, arrive = new Sommet(idArrive));
            }

            // On n'ajoute que l'arc sortant du sommet d'origine car le fichier fournit l'arc sortant dans les deux sens (s'il y est --> graphe orienté)
            origine.addArcs(new Arc(origine, arrive,longueur));
        }
        sc.close();
        System.out.println("Fichier ville chargé\n");

    }

    // APPROCHE N°1 - PLUS PROCHE VOISINS
    public List<Sommet> tourneePPV(Sommet depart) {
        Sommet courant = depart;

        List<Sommet> visite = new ArrayList<>();    // Liste qui permet de savoir dans quel ordre on était visité les sommets
        visite.add(courant);                        // Ajout du sommet de depart

        Set<Sommet> nonVisites = new HashSet<>(sommets.values()); // Pour parcourir tous nos sommets (c'est un peu comme pour la file/pile des BFS/DFS)
        nonVisites.remove(courant);

        HashMap<Sommet,Arc> parent = new HashMap<>();             // Pour retrouver le chemin et l'afficher ensuite

        while (!nonVisites.isEmpty()) {

            Arc meilleurArc = null;
            int meilleur = Integer.MAX_VALUE;   // MAX_VALUE == infini

            // On cherche le voisin non visité le plus proche
            for (Arc arc : courant.getArcsSortants()) {
                Sommet voisin = arc.getArrive();
                if (nonVisites.contains(voisin) && arc.getLongueur() < meilleur) { // Si le voisin rencontré viens d'un chemin plus court et qu'il est non visité
                    meilleur = arc.getLongueur();                                  // On récupère sa longueur
                    meilleurArc = arc;                                             // et on sauvegarde la taille de ce dernier au cas où il y a un chemin plus court
                }
            }

            if (meilleurArc == null) {
                System.out.println("Erreur : aucun voisin accessible !");
                break;
            }

            // A la fin de la boucle, on change de sommet
            courant = meilleurArc.getArrive();              // Le prochain arc visité est celui avec la plus petite longueur sortante de celui d'avant
            parent.put(courant, meilleurArc);               // on récupère l'arc par lequel vient ce sommet (le nouveau)
            visite.add(courant);                            // on l'ajoute dans les sommets visités
            nonVisites.remove(courant);                     // et on le retire de ceux non visités
        }

        // Pour l'affichage, j'affiche le fait que l'on retourne au point de depart (retour à la base)
        visite.add(depart);
        afficherTournee(visite);

        return visite;
    }

    // Affichage du graphe
    public void afficherTournee(List<Sommet> visite) {
        for (int i = 0; i < visite.size(); i++) {
            System.out.print(visite.get(i).getId());
            if (i < visite.size() - 1) System.out.print(" -> ");
        }
        System.out.println();
    }


    public Map<Sommet,Arc> MST_PRIM(Sommet depart) {
        Sommet courant = depart;

        Map<Sommet,Arc> result = new HashMap<>();
        result.put(depart,null);

        int min = Integer.MAX_VALUE;
        Arc arcMin = null;

        for (Arc arc : courant.getArcsSortants()) {
            Sommet voisin = arc.getArrive();
            if (arc.getLongueur() < min) {
                min = arc.getLongueur();
                arcMin = arc;
            }
        }

        courant = arcMin.getArrive();
        result.put(courant,arcMin);







        return result;
    }






    // Affichage du grpahe (je l'ai repris de l'ancien TP car je pense qu'on en aura besoin pour la suite)
    public void afficherItineraire(HashMap <Sommet, Arc> parent, Sommet D, Sommet A) {
        List<Arc> chemin = new ArrayList<>();

        Sommet courant = A;

        while (!courant.equals(D)) {         // tant que la sommet actuelle n'est pas celui de départ
            Arc arc = parent.get(courant);   // on récupère l'arc sortant qui nous as permit d'arriver jusqu'au sommet
            chemin.add(arc);                 // on ajoute l'arc au chemin
            courant = arc.getOrigine();      // et notre sommet courant devient celui qui est au depart de notre arc (le sommet avant celle où l'on est)
        }

        Collections.reverse(chemin);         // inverse la collection pour avoir le bon ordre des arcs


        System.out.println(D.getId());
        for (int i = 0; i < chemin.size() ;i++) {
            Arc arc = chemin.get(i);
            Sommet prochain= arc.getArrive();

            System.out.println(" --> " + prochain.getId());
        }
        System.out.println(A.getId());
    }


    // Acceseurs
    public Sommet getSommet(String id) { return sommets.get(id); }


}
