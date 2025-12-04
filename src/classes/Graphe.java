    package classes;

    import java.io.File;
    import java.io.FileNotFoundException;
    import java.lang.reflect.Array;
    import java.util.*;

public class Graphe {
    private HashMap<String, Sommet> sommets;
    private HashMap<String, Sommet> maisons;
    //constructeur
    public Graphe() {
        sommets = new HashMap<>();
        recupFichier("graphe.txt");

    }

    public void recupFichier(String adresse) {
        try {
            File fichier = new File(adresse);
            Scanner sc = new Scanner(fichier);
            while (sc.hasNextLine()) {
                String s = sc.nextLine();
                String[] parts = s.split(";");
                Arc arc = new Arc(parts[0].trim(), parts[1].trim(), Integer.valueOf(parts[2].trim()));

                if (!sommets.containsKey(parts[0].trim())) {
                    int type=0;
                    switch(parts[0].trim().charAt(0)){
                        case 'c':
                            type=0;
                            break;
                        case 'C':
                            type=1;
                            break;
                        case 'M':
                            type=2;
                            break;
                        case 'P':
                            type=3;
                            break;
                        default:
                            System.out.println("ce sommet ne rentre pas dans les crieres de type (switch case):" + parts[0]);
                            break;
                    }
                    sommets.put(parts[0].trim(), new Sommet(parts[0].trim(), type));
                }
                //if (!sommets.containsKey(parts[1].trim())) sommets.put(parts[1].trim(), new Sommet(parts[1].trim()),);
                sommets.get(parts[0].trim()).addArcSortants(arc);
                //sommets.get(parts[1].trim()).addArcEntrants(arc);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }
    }

    //////////////////////////// THEME 1////////////////////////
    /// Hypothèse 3
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

    /// Problématique 1 Hypothèse 1
    public void afficherItineraireDijkstra(String depart, String arrive) {
        dijkstra(depart);

        Sommet dest = sommets.get(arrive);

        if (dest == null) {
            System.out.println("Sommet d'arrivée inconnu : " + arrive);
            return;
        }


        // Reconstruction du chemin en remontant les prédécesseurs


        List<String> cheminAlle=new ArrayList<>();
        Sommet courant = dest;

        while (courant != null) {
            cheminAlle.add(courant.id);
            if (courant.predecesseur == null) break;
            courant = sommets.get(courant.predecesseur);
        }

        // Le chemin est à l'envers, on le remet dans le bon sens
        Collections.reverse(cheminAlle);

        dijkstra(arrive);

        Sommet dep = sommets.get(depart);

        if (dep == null) {
            System.out.println("Sommet de depart inconnu : " + depart);
            return;
        }

        List<String> cheminRetour=new ArrayList<>();
        courant = dep;

        while (courant != null) {
            cheminRetour.add(courant.id);
            if (courant.predecesseur == null) break;
            courant = sommets.get(courant.predecesseur);
        }

        // Le chemin est à l'envers, on le remet dans le bon sens
        Collections.reverse(cheminRetour);


        //supperposition des deux chemins
        List<String> chemin = new ArrayList<>(cheminAlle);
        for(int i=1;i<cheminRetour.size();i++)chemin.add(cheminRetour.get(i));

        // Affichage
        System.out.print("Itinéraire de " + depart + " à " + arrive + " : ");
        for (int i = 0; i < chemin.size(); i++) {
            System.out.print(chemin.get(i));
            if (i < chemin.size() - 1) System.out.print(" -> ");
        }
        System.out.println();
        System.out.println("Longueur totale = " + dest.longueur);
    }

    /// Problématique 2
    //calcul des distances pour aller de n'importe quelle point s à n'importe quelle autre point t (grâce à djikstra)
    public HashMap<Sommet,HashMap<Sommet,Integer>> calculerToutesDistances(){

        HashMap<Sommet,HashMap<Sommet,Integer>> dist=new HashMap<>();

        for(Sommet s: maisons.values()){
            dijkstra(s.getId());

            HashMap<Sommet,Integer> distDepuisS=new HashMap<>();
            for(Sommet t: maisons.values()) {
                distDepuisS.put(t, t.getLongueur());
            }

            dist.put(s,distDepuisS);

        }

        return dist;
    }

    //sous classe pour plus de practicité dans le stockage des résultats
    public class ResultatCircuit {
        ArrayList<Sommet> chemin;
        int longueur;
        ResultatCircuit(ArrayList<Sommet> meilleurChemin,int meilleurCout){
            chemin=meilleurChemin;
            longueur=meilleurCout;
        }
    }
    private int meilleurCout;
    private ArrayList<Sommet> meilleurChemin;

    private void tspRec(Sommet courant,Sommet depart,HashSet<Sommet> nonVisites,
                        int coutActuel,ArrayList<Sommet> cheminCourant,HashMap<Sommet,
                    HashMap<Sommet,Integer>> dist,HashMap<Sommet,Integer> minSortant){
        int borne = coutActuel;
        for(Sommet s: nonVisites){
            borne+=minSortant.get(s);
        }
        if(borne>=meilleurCout){
            return;
        }

        if(nonVisites.isEmpty()){
            int coutRetour=dist.get(courant).get(depart);
            int coutTotal=coutActuel+coutRetour;
            if(coutTotal<meilleurCout){
                meilleurCout=coutTotal;
                meilleurChemin=new ArrayList<>(cheminCourant);
                meilleurChemin.add(depart);
            }
            return;
        }
        ArrayList<Sommet> candidats = new ArrayList<>(nonVisites);

        for(Sommet suivant :candidats){
            int d=dist.get(courant).get(suivant);
            int nouveauCout=coutActuel+d;
            if(nouveauCout >=meilleurCout) continue;
            nonVisites.remove(suivant);
            cheminCourant.add(suivant);

            tspRec(suivant,depart,nonVisites,nouveauCout,cheminCourant,dist,minSortant);

            cheminCourant.remove(cheminCourant.size()-1);
            nonVisites.add(suivant);
        }
    }

    public ResultatCircuit tspBranchAndBound(String departId){
        Sommet depart=maisons.get(departId);
        if(depart==null) throw new IllegalArgumentException("Sommet de départ inconnu :"+departId);

        HashMap<Sommet,HashMap<Sommet,Integer>>dist=calculerToutesDistances();

        HashMap<Sommet,Integer> minSortant=new HashMap<>();
        for(Sommet s: maisons.values()){
            int min=Integer.MAX_VALUE;
            for(Sommet t: maisons.values()){
                int d=dist.get(s).get(t);
                if(d<min)min=d;
            }
            minSortant.put(s,min);
        }

        meilleurCout=Integer.MAX_VALUE;
        meilleurChemin=null;

        HashSet<Sommet> nonVisites=new HashSet<>(maisons.values());
        nonVisites.remove(depart);

        ArrayList<Sommet> cheminCourant=new ArrayList<>();
        cheminCourant.add(depart);

        tspRec(depart,depart,nonVisites,0,cheminCourant,dist,minSortant);

        if(meilleurChemin==null)return null;
        return new ResultatCircuit(meilleurChemin,meilleurCout);

    }

    private ArrayList<Sommet> cheminDijkstraSommet(String departId, String arriveId) {
        dijkstra(departId);

        Sommet dest = sommets.get(arriveId);
        if (dest == null || dest.longueur == Integer.MAX_VALUE) {
            return null; // pas de chemin
        }

        // Reconstruction du chemin en remontant les prédécesseurs
        ArrayList<Sommet> chemin = new ArrayList<>();
        Sommet courant = dest;

        while (courant != null) {
            chemin.add(courant);
            if (courant.predecesseur == null) break;
            courant = sommets.get(courant.predecesseur);
        }

        // Le chemin est à l'envers, on le remet dans le bon sens
        Collections.reverse(chemin);
        return chemin;
    }

    public void afficherItineraireTsp(String depart) {

        ResultatCircuit res = tspBranchAndBound(depart);

        if (res == null) {
            System.out.println("Aucune tournée TSP trouvée à partir de " + depart + ".");
            return;
        }

        ArrayList<Sommet> tour=res.chemin;

        List<Sommet> cheminComplet=new ArrayList<>();
        for(int i=0;i<tour.size()-1;i++){
            Sommet from=tour.get(i);
            Sommet to=tour.get(i+1);

            ArrayList<Sommet> segment = cheminDijkstraSommet(from.getId(),to.getId());
            if(i==0)for(int j=0;j<segment.size();j++)cheminComplet.add(segment.get(j));
            else for(int j=1;j<segment.size();j++)cheminComplet.add(segment.get(j));
        }

        // Affichage du chemin
        System.out.print("Itinéraire TSP à partir de " + depart + " : ");

        for (int i = 0; i < cheminComplet.size(); i++) {
            System.out.println(cheminComplet.get(i).getId());
            if (i < cheminComplet.size() - 1) System.out.print(" -> ");
        }
        System.out.println();

        // Affichage de la longueur totale
        System.out.println("Longueur totale de la tournée = " + res.longueur);
    }

    public void afficherItineraireTh1Pb1Hp2(String depart){
        maisons = new HashMap<>();
        maisons.put("centre_de_traitement",sommets.get("centre_de_traitement"));
        Scanner sc=new Scanner(System.in);
        int nbMaisons;
        do {
            System.out.print("entrez le nombre (positif et inférieur à 10) de particulier :");
            nbMaisons=sc.nextInt();
            if(nbMaisons>10 ||nbMaisons<0) System.out.println("il faut que le nombre soit >0 et <10");
        }while(nbMaisons>10 || nbMaisons<0);
        for(int i=0;i<nbMaisons;i++) {
            System.out.print("entrez l'id d'un lieu :");
            String buffer = new String();
            buffer = sc.nextLine();
            if (!sommets.containsKey(buffer)) {
                System.out.println("adresse inconnu, recommencez");
                i--;
            } else maisons.put(buffer, sommets.get(buffer));
        }
        sc.close();
        afficherItineraireTsp(depart);
    }

    public void afficherItineraireTh1Pb2(String depart){
        maisons = new HashMap<>();
        for(Sommet sommet:sommets.values()) {
            if(sommet.getType()==2||sommet.getType()==0){//2 pour maison et 0 pour centre de traitement
                maisons.put(sommet.getId(), sommet);
            }
        }
        afficherItineraireTsp(depart);
    }


    /////////////////////////THEME2//////////////////////////////

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

                //if (courant != null) {
                    courant = sommets.get(meilleurArc.getArrive());                        // et le nouveau sommet courant devient celui qui à le plus petit arc
                //}

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



}
