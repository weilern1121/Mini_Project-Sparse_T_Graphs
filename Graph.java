import java.util.*;

public class Graph {
    private int numOfVertex;
    private int[][] adjListArray;
    private LinkedList<Edge> edges;
    private int[] numOfNeighbors;

    // constructor
    Graph(int V)
    {
        this.numOfVertex = V;
        // define the size of array as
        // number of vertices
        this.adjListArray = new int[V][V];
        this.numOfNeighbors=new int[V];

        // Create a new adjacency matrix
        // reset the new value
        for(int i = 0; i < V ; i++){
            this.numOfNeighbors[i]=0;
            for(int j = 0; j < V ; j++)
            this.adjListArray[i][j]=0;
        }
        edges=new LinkedList();
    }

    //methods
    public int getNumOfVertex() {
        return numOfVertex;
    }

    public LinkedList<Edge> getEdges() {
        return edges;
    }

    public int[] getAdjListArray(int i) {
        return adjListArray[i];
    }

    public int[][] getAdjListArray() {
        return adjListArray;
    }

    public int[] getNumOfNeighbors() {
        return numOfNeighbors;
    }

    public boolean contains (int from, int to)
    {
        return adjListArray[from][to]==1;
    }

    public void addEdge (Integer a, Integer b ){
        Edge e=new Edge(a,b);
        adjListArray[a][b]=1;
        adjListArray[b][a]=1;
        numOfNeighbors[a]++;
        numOfNeighbors[b]++;
        edges.add(e);
    }
    public void addEdge(Edge e){
        adjListArray[e.getV_to()][e.getV_from()]=1;
        adjListArray[e.getV_from()][e.getV_to()]=1;
        numOfNeighbors[e.getV_to()]++;
        numOfNeighbors[e.getV_from()]++;
        edges.add(e);
    }

    public static void printMatrixGraph(int[][] adjListArray){
        int numOfVertex=adjListArray[0].length;
        System.out.println("The Graph:");
        System.out.print("    ");
        for (int i=0; i< numOfVertex; i++)
            System.out.print("V"+i+"   ");
        System.out.println(" ");
        for (int i=0; i< numOfVertex; i++){
            for(int j=-1; j<numOfVertex; j++){
                if(j==-1){
                    if(i>9)
                        System.out.print("V"+i+"  ");
                    else
                        System.out.print("V"+i+"   ");
                }
                else
                System.out.print(adjListArray[i][j]+"    ");
                if(j>9)
                    System.out.print(" ");
            }
                System.out.println(" ");
        }
    }

    public void sortEdges(){
        //sort the edges by weights
        Collections.sort(this.edges, Comparator.comparingInt(Edge::getV_weight));
    }

    public void printEdges(){
        sortEdges();
        System.out.println("The edges:");
        edges.forEach((tmp) -> {
            System.out.println("v_from: "+tmp.getV_from()+"  ;v_to: "+tmp.getV_to()+"  ;v_weight:"+tmp.getV_weight());
        });
    }
    public  int getNumOfNeighbors(int a){
        return numOfNeighbors[a];
    }

    public static void deepCopyGraph(Graph from, Graph to){
        int max=from.numOfVertex;

        for(int i=0; i<max;i++){
            to.getNumOfNeighbors()[i]=from.getNumOfNeighbors()[i];
            for(int j=0; j<max;j++)
                to.getAdjListArray()[i][j]=from.getAdjListArray()[i][j];
        }

        //reset the edges in to
        while(!to.getEdges().isEmpty())
            to.getEdges().pop();
        //push edges to to
        for (Edge tmp:from.getEdges()) {
            int tmp_from=tmp.getV_from();
            int tmp_to=tmp.getV_to();
            to.getEdges().add(new Edge(tmp_from,tmp_to,tmp.getV_weight()));
            to.getNumOfNeighbors()[tmp_from]++;
            to.getNumOfNeighbors()[tmp_to]++;
        }
        //copy the number of neighbors
        for(int i=0; i<max; i++)
            to.getNumOfNeighbors()[i]=to.getNumOfNeighbors()[i]/2;;
    }

    public int getEdgesSize(){
        int output=0;
        for (Edge e:this.getEdges()) {
            output+=e.getV_weight();
        }
        return output;
    }

}
