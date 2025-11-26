package classes;

public class Arc {

    // Attributs
    private String nomRue;
    private Sommet origine;
    private Sommet arrive;
    private int longueur;   // Poids de l'arc (la distance de la rue)


    // Constructeur
    public Arc(Sommet origine, Sommet arrive, int distance ){
        this.origine = origine;
        this.arrive = arrive;
        this.longueur = distance;
    }

    // Accesseurs
    public int getLongueur(){return longueur;}
    public Sommet getOrigine(){return origine;}
    public Sommet getArrive(){return arrive;}
    public String getNomRue(){return nomRue;}
}
