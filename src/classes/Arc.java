package classes;

public class Arc {
    String origine;
    String arrive;
    int distance;
    int dechets;

    Arc (String origine, String arrive, int distance) {
        this.origine = origine;
        this.arrive = arrive;
        this.distance = distance;
    }

    Arc(String origine, String arrive, int poids, int dechets){
        this.origine = origine;
        this.arrive = arrive;
        this.distance =poids;
        this.dechets = dechets ;
    }

    int getDistance(){return distance;}
    String getOrigine(){return origine;}
    String getArrive(){return arrive;}
    int getDechets(){return dechets;}
}
