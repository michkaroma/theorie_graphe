package classes;

import java.io.*;
import java.util.*;

public class Sommet implements Comparable<Sommet>{
    //variables
    String id;
    ArrayList<Arc> arcsSortants;
    ArrayList<Arc> arcsEntrants;
    private int type; //0->centre de collecte  1->carrefour  2->maisons  3->points de collectes
    private int degreP;
    private int degreN;
    private int diffDegre;

    //variables algo djikstra
    boolean verif;
    String predecesseur;
    int longueur;

    //variable cul de sac
    boolean cul_de_sac;


    //constructeur
    public Sommet(String id,int type){
        this.id = id;
        this.type=type;
        arcsSortants = new ArrayList<>();
        arcsEntrants = new ArrayList<>();
        degreP=0;
        degreN=0;
        diffDegre=0;
    }

    //fonctions de Comparable
    @Override
    public int compareTo(Sommet o){
        return Integer.compare(this.longueur,o.longueur);
    }

    //fonctions arcs
    public ArrayList<Arc> addArcSortants(Arc arc){arcsSortants.add(arc);return arcsSortants;}
    public void setArcsSortants(ArrayList<Arc> arcsSortants){this.arcsSortants = arcsSortants;}
    public ArrayList<Arc> getArcsSortants(){return arcsSortants;}
    public ArrayList<Arc> addArcEntrants(Arc arc){arcsEntrants.add(arc);return arcsSortants;}

    //fonction id
    public String getId(){return id;}
    public void setId(String id){this.id = id;}

    //fonction longueur
    public int getLongueur(){return longueur;}
    public void setLongueur(int longueur){this.longueur = longueur;}

    //fonction predecesseur
    public String getPredecesseur(){return predecesseur;}
    public void setPredecesseur(String p){predecesseur = p;}

    //fonction type
    public int getType(){return type;}

    //fonction degre
    public int getDegreN(){return degreN;}
    public int getDegreP(){return degreP;}
    public int getDiffDegre(){return diffDegre;}
    public void setDiffDegre(int diffDegre){this.diffDegre = diffDegre;}
    public void setDegreP(int degreP){this.degreP = degreP;}
    public void setDegreN(int degreN){this.degreN = degreN;}
    public void addDegreP(){this.degreP += 1;}
    public void addDegreN(){this.degreN += 1;}
}
