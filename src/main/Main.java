package main;

import classes.Graphe;
import classes.Sommet;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Graphe graphe = new Graphe();
        graphe.recupFichier("graphe.txt");
        graphe.cul_de_sac();//initialisation des culs de sac
        graphe.afficherItineraireDijkstra("centre_de_traitement", "Maison_5");
        graphe.tourneePPV("centre_de_traitement");

        List<Sommet> tour = graphe.tourneeMST("Maison_5"); // exemple : sommet de départ = A

        for (Sommet s : tour) {
            System.out.print(s.getId() + " -> ");
        }

        Graphe.ResultatCircuit res = graphe.tspBranchAndBound("centre_de_traitement");

        if (res == null) {
            System.out.println("");
            System.out.println("Aucun tour hamiltonien trouvé à partir du centre de traitement.");
        } else {
            System.out.println("Longueur minimale : " + res.longueur);
            System.out.print("Chemin : ");
            for (Sommet s : res.chemin) {
                System.out.println(s.getId() + " ");
            }


        }
    }
}