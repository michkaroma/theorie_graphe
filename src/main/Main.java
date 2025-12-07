package main;

import classes.Graphe;
import classes.Sommet;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Graphe graphe = new Graphe();
        Graphe grapheC = new Graphe();
        graphe.recupFichier("graphe.txt");
        grapheC.recupFichierCapacite("grapheCapacite.txt");
        //graphe.afficherItineraireDijkstra("centre_de_traitement","Maison_5");


        //graphe.tourneePPV("centre_de_traitement");
        grapheC.tourneeMST("centre_de_traitement");






    }
}