package classes;

import java.io.*;
import java.util.*;

public class Sommet {

    // Attributs
    private String id;
    private int dechets;
    private ArrayList<Arc> arcsSortants = new ArrayList<>();

    // Constructeur
    Sommet(String id){
        this.id = id;
        this.dechets = -1;
    }

    // Méthodes
    // Ajoute un arc sortants à un sommet
    public void addArcs(Arc arc){
        arcsSortants.add(arc);
    }

    // Accesseurs
    public void setArcsSortants(ArrayList<Arc> arcsSortants){this.arcsSortants = arcsSortants;}
    public ArrayList<Arc> getArcsSortants(){return arcsSortants;}
    public String getId(){return id;}
    public void setId(String id){this.id = id;}
    public int getDechets(){return dechets;}


    // Surchage de méthodes
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sommet)) return false;
        Sommet s = (Sommet)o;
        return id.equals(s.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }


}
