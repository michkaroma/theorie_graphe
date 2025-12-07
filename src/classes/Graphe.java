package classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Graphe {
    private HashMap<String, Sommet> sommets;
    private HashMap<String, Sommet> maisons;
    private int capaciteCamion = 15;

    //constructeur
    public Graphe(String fichier) {
        sommets = new HashMap<>();
        recupFichier(fichier);
    }
    public Graphe(HashMap<String,Sommet> graphe) {
        sommets = graphe;
        for(Sommet s:sommets.values()) {
            s.setDegreN(s.getArcsEntrants().size());
            s.setDegreP(s.getArcsSortants().size());
            s.setDiffDegre(s.getDegreP()-s.getDegreN());
        }
    }
    private void actualisationDiffDegre(){
        for (Sommet sommet : sommets.values()) {sommet.setDiffDegre(sommet.getDegreP()-sommet.getDegreN());}
    }

    // Récupère les informations du graphe.txt
    public void recupFichier(String adresse) {
        try {
            File fichier = new File(adresse);
            Scanner sc = new Scanner(fichier);
            while (sc.hasNextLine()) {
                String s = sc.nextLine();
                String[] parts = s.split(";");
                Arc arc = new Arc(parts[0].trim(), parts[1].trim(), Integer.valueOf(parts[2].trim()), Integer.valueOf(parts[3].trim()));
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
                if (!sommets.containsKey(parts[1].trim())) {
                    int type=0;
                    switch(parts[1].trim().charAt(0)){
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
                            System.out.println("ce sommet ne rentre pas dans les crieres de type (switch case):" + parts[1]);
                            break;
                    }
                    sommets.put(parts[1].trim(), new Sommet(parts[1].trim(),type ));
                }
                sommets.get(parts[0].trim()).addArcSortants(arc);
                sommets.get(parts[0].trim()).addDegreP();
                sommets.get(parts[1].trim()).addArcEntrants(arc);
                sommets.get(parts[1].trim()).addDegreN();
            }
            sc.close();
            actualisationDiffDegre();
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
    public void afficherItineraireTh1Pb1Hp1(String depart, String arrive) {
        int longueurTot=0;
        dijkstra(depart);

        Sommet dest = sommets.get(arrive);

        if (dest == null) {
            System.out.println("Sommet d'arrivee inconnu : " + arrive);
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

        longueurTot +=dest.longueur;


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

        longueurTot +=dep.longueur;

        //supperposition des deux chemins
        List<String> chemin = new ArrayList<>(cheminAlle);
        for(int i=1;i<cheminRetour.size();i++)chemin.add(cheminRetour.get(i));

        // Affichage
        System.out.print("Itineraire de " + depart + " à " + arrive + " : ");
        for (int i = 0; i < chemin.size(); i++) {
            System.out.print(chemin.get(i));
            if (i < chemin.size() - 1) System.out.print(" -> ");
        }
        System.out.println();
        System.out.println("Longueur totale = " + longueurTot );
    }

    /// Problématique 1 Hypothèse 2
    //calcul des distances pour aller de n'importe quelle point s à n'importe quelle autre point t (grâce à djikstra)
    public HashMap<Sommet,HashMap<Sommet,Integer>> calculerToutesDistances(HashMap<String,Sommet> departs,HashMap<String,Sommet> arrives){

        HashMap<Sommet,HashMap<Sommet,Integer>> dist=new HashMap<>();

        for(Sommet s: departs.values()){
            dijkstra(s.getId());

            HashMap<Sommet,Integer> distDepuisS=new HashMap<>();
            for(Sommet t: arrives.values()) {
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
        if(depart==null) throw new IllegalArgumentException("Sommet de depart inconnu :"+departId);

        HashMap<Sommet,HashMap<Sommet,Integer>>dist=calculerToutesDistances(maisons,maisons);

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

    private void afficherItineraireTsp(String depart) {

        ResultatCircuit res = tspBranchAndBound(depart);

        if (res == null) {
            System.out.println("Aucune tournee TSP trouvee à partir de " + depart + ".");
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
        System.out.print("Itineraire TSP à partir de " + depart + " : ");

        for (int i = 0; i < cheminComplet.size(); i++) {
            System.out.println(cheminComplet.get(i).getId());
            if (i < cheminComplet.size() - 1) System.out.print(" -> ");
        }
        System.out.println();

        // Affichage de la longueur totale
        System.out.println("Longueur totale de la tournee = " + res.longueur);
    }

    public void afficherItineraireTh1Pb1Hp2(String depart){
        maisons = new HashMap<>();
        maisons.put("centre_de_traitement",sommets.get("centre_de_traitement"));
        Scanner sc=new Scanner(System.in);
        int nbMaisons;
        do {
            System.out.print("entrez le nombre (positif et inferieur à 10) de particulier :");
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

    /// Problématique 2

    private void dijkstraMaisons(String departId,HashMap<String,Sommet> graphe){
        PriorityQueue<Sommet> liste=new PriorityQueue<>();
        for(Sommet sommet : sommets.values()){
            sommet.verif=false;
            sommet.longueur=Integer.MAX_VALUE;
            sommet.predecesseur=null;
        }
        Sommet depart = sommets.get(departId);
        depart.longueur=0;
        liste.add(depart);
        while(!liste.isEmpty()){
            Sommet courant=liste.poll();
            if(courant.verif) continue;
            courant.verif=true;
            if((courant.getType()==1 || courant.getType()==0 ) && courant!=depart)continue;
            for(Arc arc:courant.getArcsSortants()){
                Sommet voisin = sommets.get(arc.getArrive());
                int nouvelleLongueur=courant.getLongueur()+arc.getDistance();
                if(nouvelleLongueur<voisin.getLongueur()){
                    voisin.setLongueur(nouvelleLongueur);
                    voisin.setPredecesseur(courant.getId());
                    liste.add(voisin);
                }
            }
        }
        for(Sommet cible: sommets.values()){
            if(!(cible.getType()==1 || cible.getType()==0 ) || cible==depart || cible.getLongueur()==Integer.MAX_VALUE) continue;
            Arc a = new Arc(departId, cible.getId(), cible.getLongueur());
            graphe.get(departId).addArcSortants(a);
            graphe.get(cible.getId()).addArcEntrants(a);
        }
    }


        public static int[] hungarian(int[][] cost) {
            int n = cost.length;
            int[] u = new int[n + 1];
            int[] v = new int[n + 1];
            int[] p = new int[n + 1];
            int[] way = new int[n + 1];

            for (int i = 1; i <= n; i++) {
                p[0] = i;
                int j0 = 0;
                int[] minv = new int[n + 1];
                boolean[] used = new boolean[n + 1];

                for (int j = 1; j <= n; j++) {
                    minv[j] = Integer.MAX_VALUE;
                }

                do {
                    used[j0] = true;
                    int i0 = p[j0];
                    int delta = Integer.MAX_VALUE;
                    int j1 = 0;

                    for (int j = 1; j <= n; j++) {
                        if (!used[j]) {
                            int cur = cost[i0 - 1][j - 1] - u[i0] - v[j];
                            if (cur < minv[j]) {
                                minv[j] = cur;
                                way[j] = j0;
                            }
                            if (minv[j] < delta) {
                                delta = minv[j];
                                j1 = j;
                            }
                        }
                    }

                    for (int j = 0; j <= n; j++) {
                        if (used[j]) {
                            u[p[j]] += delta;
                            v[j] -= delta;
                        } else {
                            minv[j] -= delta;
                        }
                    }

                    j0 = j1;

                } while (p[j0] != 0);

                do {
                    int j1 = way[j0];
                    p[j0] = p[j1];
                    j0 = j1;
                } while (j0 != 0);
            }

            // p[j] = i => i is assigned to j
            int[] assignment = new int[n];
            for (int j = 1; j <= n; j++) {
                if (p[j] > 0) {
                    assignment[p[j] - 1] = j - 1;
                }
            }
            return assignment;
        }


    private Graphe reductionGraphe(){//Creation d'un graphe sans maison
        HashMap<String, Sommet> grapheCarrefour=new HashMap<>();

        for(Sommet sommet : sommets.values()){
            if(sommet.getType()==1 || sommet.getType()==0 )grapheCarrefour.put(sommet.getId(),new Sommet(sommet.getId(),sommet.getType()));
        }

        for(Sommet s:sommets.values()){
            if(s.getType()==1 || s.getType()==0 ) dijkstraMaisons(s.getId(),grapheCarrefour);
        }
        Graphe gc=new Graphe(grapheCarrefour);
        return gc;
    }

    private void CPP(){
        HashMap<String,Sommet> sommetsN=new HashMap<>();
        HashMap<String,Sommet> sommetsP=new HashMap<>();
        for(Sommet sommet : sommets.values()){
            if(sommet.getDiffDegre()<0) sommetsN.put(sommet.getId(),sommet);
            if(sommet.getDiffDegre()>0) sommetsP.put(sommet.getId(),sommet);
        }
        HashMap<Sommet,HashMap<Sommet,Integer>> dist=calculerToutesDistances(sommetsN,sommetsP);

        // Exemple de construction de listes avec multiplicité
        List<Sommet> gauche = new ArrayList<>(); // sommetsN, répétés |diff|
        List<Sommet> droite = new ArrayList<>(); // sommetsP, répétés |diff|

        for (Sommet s : sommetsN.values()) {
            int k = -s.getDiffDegre(); // par ex. si diff = -2 -> 2 fois
            for (int i = 0; i < k; i++) {
                gauche.add(s);
            }
        }

        for (Sommet s : sommetsP.values()) {
            int k = s.getDiffDegre(); // ex. si diff = +3 -> 3 fois
            for (int i = 0; i < k; i++) {
                droite.add(s);
            }
        }
        final int INF = 1_000_000_000;
        int n = gauche.size();
        int[][] cost = new int[n][n];
        for (int i = 0; i < n; i++) {
            Sommet from = gauche.get(i);
            HashMap<Sommet, Integer> distFrom = dist.get(from);

            if (distFrom == null) {
                // normalement impossible si le graphe réduit est fortement connexe
                throw new IllegalStateException("Aucune distance connue depuis " + from.getId());
            }

            for (int j = 0; j < n; j++) {
                Sommet to = droite.get(j);
                Integer dObj = distFrom.get(to);

                int d;
                if (dObj == null || dObj == Integer.MAX_VALUE) {
                    // pas de chemin trouvé → on met un coût énorme
                    d = INF;
                } else {
                    d = dObj;
                }
                cost[i][j] = d;
            }
        }
        int[] match = hungarian(cost);
        for (int i = 0; i < n; i++) {
            Sommet from = gauche.get(i);
            Sommet to = droite.get(match[i]);

            dijkstra(from.getId());

            ajouterChemin(from, to); // à implémenter
        }

    }

    private void ajouterChemin(Sommet from, Sommet to) {
        Sommet courant = to;

        while (!courant.equals(from)) {
            Sommet pred = sommets.get(courant.getPredecesseur());

            if (pred == null) {
                throw new IllegalStateException("Pas de chemin de " + from.getId() + " à " + to.getId());
            }

            Arc a = trouverArc(pred, courant);
            pred.addArcSortants(a);
            courant.addArcEntrants(a);
            courant = pred;
        }
    }

    private ResultatCircuit circuitEulerien(String departId) {
        Sommet depart = sommets.get(departId);
        if (depart == null) {
            throw new IllegalArgumentException("Sommet de départ inconnu : " + departId);
        }

        // 1. Copie des arcs sortants dans des Deque consumables
        Map<String, Deque<Arc>> adj = new HashMap<>();
        for (Sommet s : sommets.values()) {
            adj.put(s.getId(), new ArrayDeque<>(s.getArcsSortants()));
        }

        Deque<Sommet> pileSommets = new ArrayDeque<>();
        Deque<Arc> pileArcs = new ArrayDeque<>();
        List<Arc> circuitArcs = new ArrayList<>();

        pileSommets.push(depart);

        // 2. Hierholzer : on construit une liste d'arcs dans l'ordre inverse
        while (!pileSommets.isEmpty()) {
            Sommet v = pileSommets.peek();
            Deque<Arc> arcsSortants = adj.get(v.getId());

            if (arcsSortants != null && !arcsSortants.isEmpty()) {
                // On prend un arc sortant encore disponible
                Arc a = arcsSortants.pollFirst();
                Sommet w = sommets.get(a.getArrive());
                pileSommets.push(w);
                pileArcs.push(a); // arc correspondant au déplacement
            } else {
                // Cul-de-sac : on remonte en ajoutant l'arc associé au circuit
                pileSommets.pop();
                if (!pileArcs.isEmpty()) {
                    circuitArcs.add(pileArcs.pop());
                }
            }
        }

        // 3. On a les arcs du circuit dans l'ordre inverse → renverser
        Collections.reverse(circuitArcs);

        // 4. Construire la liste de sommets + calculer la longueur totale
        ArrayList<Sommet> chemin = new ArrayList<>();
        int longueurTotale = 0;

        if (!circuitArcs.isEmpty()) {
            // Premier sommet : origine du premier arc
            Arc first = circuitArcs.get(0);
            Sommet current = sommets.get(first.getOrigine());
            chemin.add(current);

            for (Arc a : circuitArcs) {
                longueurTotale += a.getDistance();
                Sommet next = sommets.get(a.getArrive());
                chemin.add(next);
            }
        }

        return new ResultatCircuit(chemin, longueurTotale);
    }



    public void afficherItineraireTh1Pb2(String departId) {
        Graphe gc=reductionGraphe();
        gc.CPP();
        ResultatCircuit res=gc.circuitEulerien(departId);
        System.out.println("Itinéraire du postier chinois :");
        for (int i = 0; i < res.chemin.size(); i++) {
            Sommet s = res.chemin.get(i);
            System.out.print(s.getId());
            if (i < res.chemin.size() - 1) {
                System.out.println(" -> ");
            }
        }
        System.out.println("\nLongueur totale = " + res.longueur);
    }




    /////////////////////////THEME2//////////////////////////////

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
