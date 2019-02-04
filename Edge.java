import java.util.Random;

public class Edge {

    //fields
    private int V_from;
    private int V_to;
    private int V_weight;

    //constructors
    public Edge (int from, int to, int weight){
        this.V_from=from;
        this.V_to=to;
        this.V_weight=weight;
    }
    public Edge (int from, int to){
        this.V_to=to;
        this.V_from=from;
        // create random object
        Random ran = new Random();
        this.V_weight= ran.nextInt(50);
    }

    //methods


    public int getV_from() {
        return V_from;
    }

    public int getV_to() {
        return V_to;
    }

    public int getV_weight() {
        return V_weight;
    }
}
