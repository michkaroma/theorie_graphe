package main;

import classes.Graphe;

public class Main {
    public static void main(String[] args) {
        Graphe graphe = new Graphe();
        graphe.recupFichier("petite_ville.txt");
        graphe.afficherItineraire("B","E");
    }
}