package main;

import classes.Graphe;
import classes.Sommet;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Graphe graphe = new Graphe();
        graphe.recupFichier("graphe.txt");
        graphe.afficherItineraireTsp("centre_de_traitement");
        graphe.tourneePPV("centre_de_traitement");

        List<Sommet> tour = graphe.tourneeMST("Maison_5"); // exemple : sommet de départ = A

        for (Sommet s : tour) {
            System.out.print(s.getId() + " -> ");
        }
    }
}