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
    public Sommet(String id){
        this.id = id;
    }

    //fonctions de Comparable
    @Override
    public int compareTo(Sommet o){
        return Integer.compare(this.longueur,o.longueur);
    }

    //fonctions arcsSortants
    public ArrayList<Arc> addArcs(Arc arc){
        arcsSortants.add(arc);
        return arcsSortants;
    }
    public void setArcsSortants(ArrayList<Arc> arcsSortants){this.arcsSortants = arcsSortants;}
    public ArrayList<Arc> getArcsSortants(){return arcsSortants;}

    //fonction id
    public String getId(){return id;}
    public void setId(String id){this.id = id;}

    //fonction longueur
    public int getLongueur(){return longueur;}
    public void setLongueur(int longueur){this.longueur = longueur;}

    //fonction predecesseur
    public String getPredecesseur(){return predecesseur;}
    public void setPredecesseur(String p){predecesseur = p;}
}
