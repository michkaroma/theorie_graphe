package main;

import classes.Graphe;

public class Main {
    public static void main(String[] args) {
        Graphe graphe = new Graphe();
        graphe.recupFichier("graphe.txt");
        graphe.afficherItineraireDijkstra("centre_de_traitement","Maison_5");

    }
}