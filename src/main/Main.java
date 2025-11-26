package main;

import classes.Graphe;
import classes.Sommet;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Graphe graphe = new Graphe();
        graphe.recupFichier("petite_ville.txt");

        Sommet sommet = graphe.getSommet("B");

        graphe.tourneePPV(sommet);
    }
}