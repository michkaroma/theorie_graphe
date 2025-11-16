import java.util.ArrayList;

public abstract class Sommet {
    String name;
    ArrayList<Arc> arcs=new ArrayList<>();
    Sommet(String name){
        this.name=name;
    }
    ArrayList<Arc> addArcs(Arc arc){
        arcs.add(arc);
        return arcs;
    }
    void setArcs(ArrayList<Arc> arcs){this.arcs=arcs;}
    ArrayList<Arc> getArcs(){return arcs;}
    String getName(){return name;}
    void setName(String name){this.name=name;}
}
