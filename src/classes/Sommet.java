package classes;

import java.util.*;

public class Sommet implements Comparable<Sommet>{
    //variables
    String id;
    ArrayList<Arc> arcsSortants;

    //variables algo djikstra
    boolean verif;
    String predecesseur;
    int longueur;

    //variable cul de sac
    boolean cul_de_sac;
    ArrayList<Arc> arcsEntrants;


    //constructeur
    Sommet(String id){
        this.id = id;
        arcsSortants = new ArrayList<>();
    }

    //fonctions de Comparable
    @Override
    public int compareTo(Sommet o){
        return Integer.compare(this.longueur,o.longueur);
    }

    //fonctions arcsSortants
    ArrayList<Arc> addArcsSortants(Arc arc){arcsSortants.add(arc);return arcsSortants;}
    ArrayList<Arc> addArcsEntrants(Arc arc){arcsEntrants.add(arc);return arcsEntrants;}
    void setArcsSortants(ArrayList<Arc> arcsSortants){this.arcsSortants = arcsSortants;}
    ArrayList<Arc> getArcsSortants(){return arcsSortants;}

    //fonction id
    String getId(){return id;}
    void setId(String id){this.id = id;}

    //fonction longueur
    int getLongueur(){return longueur;}
    void setLongueur(int longueur){this.longueur = longueur;}

    //fonction predecesseur
    String getPredecesseur(){return predecesseur;}
    void setPredecesseur(String p){predecesseur = p;}
}
