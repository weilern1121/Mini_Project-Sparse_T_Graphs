import java.util.*;

public class SparseGraph {

    private static void addRandomEdge(Graph g, int from, int to){
        g.addEdge(from,to);
    }

    private static boolean addRandomKEdge(Graph g, int k){
        Random r = new Random();
        int from=r.nextInt(g.getNumOfVertex());
        int to=r.nextInt(g.getNumOfVertex());
        //extra conditions- can't add an edge if the length of neighbors-list is in the size of k
        if( from == to || g.contains(from,to) || g.getNumOfNeighbors(from)==k || g.getNumOfNeighbors(to)==k)
            return false;
        g.addEdge(from,to);
        return true;
    }

    private static void resetEdges(Graph g){
        int V=g.getNumOfVertex();
        for(int i = 0; i < V ; i++){
            g.getNumOfNeighbors()[i]=0;
            for(int j = 0; j < V ; j++)
                g.getAdjListArray(i)[j]=0;
        }
        LinkedList tmp=g.getEdges();
            while (!tmp.isEmpty()) {
                tmp.removeFirst();
            }

    }

    private static int[] Dijkstra (Graph g, int src){
        int num=g.getNumOfVertex();
        int[] preD = new int[num];
        int INF = 99999,min, nextNode = 0; // min holds the minimum value, nextNode holds the value for the next node.
        int[] distance; // the distance array
        int[][] matrix = new int[num][num]; // the distance matrix
        int[] visited = new int[num]; // the visited array

        //construct the arrays
        for(int i=0; i<num; i++){
            visited[i] = 0; //initialize visited array to zeros
            preD[i] = 0;
            for(int j=0; j<num; j++)
                matrix[i][j]=INF;
        }
        //add the weighted edges to the distance matrix
        for (Edge e:g.getEdges()) {
            matrix[e.getV_to()][e.getV_from()]=e.getV_weight();
            matrix[e.getV_from()][e.getV_to()]=e.getV_weight();

        }
        //g.printMatrixGraph(matrix);
        distance = matrix[src]; //initialize the distance array
        visited[src] = 1; //set the source node as visited
        distance[src] = 0; //set the distance from source to source to zero which is the starting point
        for (int counter = 0; counter < num; counter++)
        {
            min = 99999;
            for (int i = 0; i < num; i++)
            {
                if (min>distance[i] && visited[i]!=1)
                {
                    min=distance[i];
                    nextNode=i;
                }
            }
            visited[nextNode] = 1;
            for (int i=0; i<num; i++)
            {
                if (visited[i]!=1)
                {
                    if (min+matrix[nextNode][i]<distance[i])
                    {
                        distance[i]=min+matrix[nextNode][i];
                        preD[i]=nextNode;
                    }
                }
            }
        }
        return distance;
    }

    public static void sortEdges(LinkedList<Edge> edges){
        //sort the edges by weights
        Collections.sort(edges, Comparator.comparingInt(Edge::getV_weight));
    }

    private static LinkedList<Edge> deepCopyEdges(LinkedList<Edge> edges){
        LinkedList<Edge> output=new LinkedList<>();
        for (Edge e:edges) {
            output.add(new Edge(e.getV_from(), e.getV_to(), e.getV_weight()));
        }
        return output;
    }


    public static Graph runAlgorithm (Graph g, int t){
        Graph g_output=new Graph(g.getNumOfVertex());
        LinkedList<Edge> edgeCopy=deepCopyEdges(g.getEdges());
        sortEdges(edgeCopy);
        int[] shortestPaths;
        while(!edgeCopy.isEmpty()){
            Edge e=edgeCopy.pop();
            shortestPaths=Dijkstra(g_output,e.getV_from());//calculate shortest paths to all nodes
            if(t*e.getV_weight() < shortestPaths[e.getV_to()]) //if(r*weight(e) < weight(P(u,v))
                g_output.addEdge(e); //add e to g_output
        }
        return g_output;
    }

    public static int constructGraph(Graph g1,int counterFalse,int flag,int k,int counterTrue,int numOfVertex,int v){
        Random r = new Random();
        switch(v){
            case 1:            //random graph
                for(int i=0; i<g1.getNumOfVertex();i++){
                    for(int j=i; j<g1.getNumOfVertex();j++){//run on upper-triangle part
                        if(i!=j){
                            if(r.nextInt(2)==1){
                                addRandomEdge(g1,i,j);
                                counterTrue++;
                            }
                        }
                    }
                }
                break;
            case 2:     //K-Regular Random graph
                int v_num=g1.getNumOfVertex();
                if(k>=v_num)
                    throw new IllegalArgumentException("k must be smaller then num_of_vertex!");
                if(k%2==1 && v_num%2==1 &&v_num<=10)
                    throw  new IllegalArgumentException("Impossible to make K-regular graph while both num_of_vertex and k are odds!");
                while(flag!=3)
                {
                    while(flag==0){
                        counterFalse++;
                        if(addRandomKEdge(g1,k))
                            counterTrue++;
                        //if all edges are settled- quit the loop
                        if(counterTrue==(v_num*k)/2)
                            flag=1;
                        //if number of tries is proportional big- reset the graph flag
                        if(counterFalse==v_num*k*10)
                            flag=2;
                    }
                    //if flag=1 ->succeed ->exit loop
                    if(flag==1)
                        flag=3;
                        //if flag!=1 ->not-succeed -> reset the graph and start all over again
                    else{
                        counterTrue=0;
                        counterFalse=0;
                        resetEdges(g1);
                        flag=0;
                    }

                }
                break;
            default: throw new IllegalArgumentException("Illegal input");
        }
        return counterTrue;
    }

    // A utility function to find the vertex with minimum key
    // value, from the set of vertices not yet included in MST
    private static int minKey(int[] key, Boolean[] mstSet, int V)
    {
        // Initialize min value
        int min = Integer.MAX_VALUE, min_index=-1;

        for (int v = 0; v < V; v++)
            if (!mstSet[v] && key[v] < min)
            {
                min = key[v];
                min_index = v;
            }

        return min_index;
    }
    public static int[][] primMST(int graph[][], int V)
    {
        // Array to store constructed MST
        int[] parent = new int[V];
        // Key values used to pick minimum weight edge in cut
        int[] key = new int[V];
        // To represent set of vertices not yet included in MST
        Boolean[] mstSet = new Boolean[V];
        // Initialize all keys as INFINITE
        for (int i=0; i<V; i++){
            key[i] = 99999;
            mstSet[i] = false;
        }
        // Always include first 1st vertex in MST.
        key[0] = 0;     // Make key 0 so that this vertex is
        // picked as first vertex
        parent[0] = -1; // First node is always root of MST
        // The MST will have V vertices
        for (int count=0; count<V-1; count++){
            // Pick thd minimum key vertex from the set of vertices
            // not yet included in MST
            int u = minKey(key, mstSet,V);
            // Add the picked vertex to the MST Set
            mstSet[u] = true;
            // Update key value and parent index of the adjacent
            // vertices of the picked vertex. Consider only those
            // vertices which are not yet included in MST
            for (int v=0; v<V; v++)
                // graph[u][v] is non zero only for adjacent vertices of m
                // mstSet[v] is false for vertices not yet included in MST
                // Update the key only if graph[u][v] is smaller than key[v]
                if (graph[u][v]!=0 && !mstSet[v] && graph[u][v]<key[v])
                {
                    parent[v]=u;
                    key[v]=graph[u][v];
                }
        }
        int[][] output=new int[graph.length][graph.length];
        for (int i = 1; i < V; i++){
            output[parent[i]][i]=graph[i][parent[i]];
            output[i][parent[i]]=graph[i][parent[i]];
            //System.out.println(parent[i]+" - "+ i+"\t"+graph[i][parent[i]]);
        }

        return output;
    }


}
