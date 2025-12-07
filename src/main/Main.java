package main;

import classes.Graphe;
import classes.Sommet;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Graphe graphe = new Graphe("graphe.txt");
        //theme 1 pb1 Hypothèse 1
        //graphe.afficherItineraireTh1Pb1Hp1("centre_de_traitement","Maison_7");
        //theme 1 pb1 Hypothèse 2
        //graphe.afficherItineraireTh1Pb1Hp2("centre_de_traitement");
        //theme 1 pb2
        //graphe.afficherItineraireTh1Pb2("centre_de_traitement");
        graphe.afficherItineraireTh1Pb2("centre_de_traitement");


        //graphe.tourneeMST("centre_de_traitement");

    }
}