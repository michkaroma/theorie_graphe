import java.util.ArrayList;

public abstract class Sommet {
    String id;
    ArrayList<Arc> arcsSortants =new ArrayList<>();
    Sommet(String id){
        this.id = id;
    }
    ArrayList<Arc> addArcs(Arc arc){
        arcsSortants.add(arc);
        return arcsSortants;
    }
    void setArcsSortants(ArrayList<Arc> arcsSortants){this.arcsSortants = arcsSortants;}
    ArrayList<Arc> getArcsSortants(){return arcsSortants;}
    String getId(){return id;}
    void setId(String id){this.id = id;}
}
