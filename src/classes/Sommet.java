package classes;

import java.io.*;
import java.util.*;

public class Sommet implements Comparable<Sommet>{
    //variables
    String id;
    ArrayList<Arc> arcsSortants =new ArrayList<>();

    //variables algo djikstra
    boolean verif;
    String predecesseur;
    int longueur;

    //constructeur
    Sommet(String id){
        this.id = id;
    }

    //fonctions de Comparable
    @Override
    public int compareTo(Sommet o){
        return Integer.compare(this.longueur,o.longueur);
    }

    //fonctions arcsSortants
    ArrayList<Arc> addArcs(Arc arc){
        arcsSortants.add(arc);
        return arcsSortants;
    }
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
