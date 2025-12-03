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
                    String s = sc.nextLine();
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

        // THEME 2 : APPROCHE 1 (Plus petit voisins proches)
        public List<Sommet> tourneePPV(String depart) {

            Sommet courant = sommets.get(depart);  // On récupère le sommet de depart

            List<Sommet> chemin = new ArrayList<>();  // Ordre dans lequel on était visités chaque sommets
            chemin.add(courant);

            Set<Sommet> nonVisites = new HashSet<>(sommets.values()); // liste qui permet de savoir si le sommet est visités ou non
            nonVisites.remove(courant);


            while (!nonVisites.isEmpty()) {

                Arc meilleurArc = null;
                int meilleur = Integer.MAX_VALUE;

                // On parcourt les arcs sortants de la station courant
                for (Arc arc : courant.getArcsSortants()) {
                    Sommet voisin = sommets.get(arc.getArrive()); // On récupère le sommet voisin à celui courant
                    if (nonVisites.contains(voisin) && arc.getDistance() < meilleur) { // et si il n'a pas été visité, et que son arc est le plus petit
                        meilleur = arc.getDistance();
                        meilleurArc = arc;                                             // on sauvegarde l'arc en question
                    }
                }

                if (meilleurArc == null) {
                    System.out.println("Erreur : aucun voisin accessible !");
                    break;
                }

                courant = sommets.get(meilleurArc.getArrive());                        // et le nouveau sommet courant devient celui qui à le plus petit arc

                chemin.add(courant);                                                   // On ajoute le sommet dans le chemin
                nonVisites.remove(courant);                                            // et on le supprime des sommets non visités
            }

            // Retour au dépôt
            chemin.add(sommets.get(depart));

            // Affichage du chemin
            afficherTournee(chemin);
            return chemin;
        }

        // Fonction pour afficher la tournée effectuer par PPV
        public void afficherTournee(List<Sommet> tournee) {
            for (int i=0; i<tournee.size(); i++) {
                System.out.print(tournee.get(i).getId());
                if (i < tournee.size()-1) System.out.print(" -> ");
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

            while (dedans.size() < sommets.size() && !file.isEmpty()) {  // Tant que le graphe n'est pas "complet"

                Arc arc = file.poll();  // On sort l'arc le plus petit (trié directement dans la file) de la file

                Sommet arrive = sommets.get(arc.getArrive());

                // On ne prend l'arc que s'il ajoute un nouveau sommet
                if (dedans.contains(arrive)) continue;

                // On ajoute l'arc au MST
                mst.add(arc);
                dedans.add(arrive);

                // On ajoute les nouveau sommets voisins à celui "d'arrive"
                for (Arc a : arrive.getArcsSortants()) {
                    Sommet next = sommets.get(a.getArrive());
                    if (!dedans.contains(next)) {
                        file.add(a);
                    }
                }
            }

            return mst;
        }

        // THEME 2 : APPROCHE 2 (MST)
        public List<Sommet> tourneeMST(String depart) {

            List<Arc> mst = primMST(depart);

            // On convertit les arcs du MST en graphe non orienté
            Map<Sommet, List<Sommet>> arbre = new HashMap<>();
            for (Sommet s : sommets.values()) {
                arbre.put(s, new ArrayList<>());
            }

            for (Arc arc : mst) {
                Sommet u = sommets.get(arc.getOrigine());
                Sommet v = sommets.get(arc.getArrive());
                arbre.get(u).add(v);
                arbre.get(v).add(u);
            }

            // DFS pour construire la tournée
            List<Sommet> tournee = new ArrayList<>();
            Set<Sommet> visite = new HashSet<>();

            Sommet start = sommets.get(depart);
            dfsMST(start, arbre, visite, tournee);

            // Retour au dépôt
            tournee.add(start);

            return tournee;
        }

        public void dfsMST(Sommet racine, Map<Sommet, List<Sommet>> arbre, Set<Sommet> visite, List<Sommet> tour) {

            LinkedList<Sommet> pile = new LinkedList<>();
            pile.add(racine);

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
