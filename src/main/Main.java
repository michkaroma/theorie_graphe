package main;

import classes.Graphe;
import classes.Sommet;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Graphe graphe = new Graphe();
        graphe.recupFichier("petite_ville.txt");
        graphe.afficherItineraire("B","E");
        graphe.tourneePPV("B");

        List<Sommet> tour = graphe.tourneeMST("A"); // exemple : sommet de départ = A

        for (Sommet s : tour) {
            System.out.print(s.getId() + " -> ");
        }




    }
}