import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class SparseGraph {

    private static boolean addRandomEdge(Graph g){
        Random r = new Random();
        int from=r.nextInt(g.getNumOfVertex());
        int to=r.nextInt(g.getNumOfVertex());
        if( from == to || g.contains(from,to))
            return false;
        g.addEdge(from,to);
        return true;
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

//    private static int minDistance(int[] dist, Boolean[] sptSet, int num)
//    {
//        // Init the min value
//        int min = 9999 , min_index=-1;
//        for (int i=0; i<num; i++)
//            if (!sptSet[i] && dist[i] <= min)
//            {
//                min = dist[i];
//                min_index =i;
//            }
//        return min_index;
//    }
//
//     private static int[] dijkstra( int src, Graph g) {
//         int[][] graph=g.getAdjListArray(); //get the adjacency matrix of the graph
//         int INFINITE=99999;
//         int num=g.getNumOfVertex();
//         int[][] dist_matrix= new int[num][num]; //the distance matrix
//         int[] dist = new int[num]; //dist[i] = shortest distance from src to i
//         int[] output=new int[num];// The output array- of calculated distances from src
//
//         // sptSet[i] is true if vertex i is included in shortest
//         // path from src to i when finalized
//         Boolean[] sptSet = new Boolean[num];
//
//         // Initialize
//         for (int i = 0; i < num; i++) {
//             dist[i] = INFINITE;
//             sptSet[i] = false;
//             for(int j=0; j<num; j++)
//                 dist_matrix[i][j]=INFINITE;
//         }
//         //add the weighted edges to the distance matrix
//        for (Edge e:g.getEdges()) {
//            dist_matrix[e.getV_to()][e.getV_from()]=e.getV_weight();
//            dist_matrix[e.getV_from()][e.getV_to()]=e.getV_weight();
//        }
//         // Distance of source is 0
//         dist[src] = 0;
//
//         // Find shortest path for all vertices
//         for (int count = 0; count < num - 1; count++) {
//             // Pick the minimum distance vertex from the set of vertices
//             int u = minDistance(dist, sptSet,num);
//             // Mark the picked vertex as processed
//             sptSet[u] = true;
//             // Update dist value of the adjacent vertices of the picked vertex.
//             for (int v = 0; v < num; v++)
//                 // Update dist[v] only if is not in sptSet, there is an
//                 // edge from u to v, and total weight of path from src to
//                 // v through u is smaller than current value of dist[v]
//                 if (!sptSet[v] && graph[u][v] != 0 && dist[u] != INFINITE && dist[u] + graph[u][v] < dist[v]){
//                     dist[v] = dist[u] + graph[u][v]; //distance in #vertex
//                     output[v]=output[u]+dist_matrix[u][v];//distance in weighted edges
//                 }
//         }
//         return output;
//     }

    private static int[] Dijkstra (Graph g, int src){
        int num=g.getNumOfVertex();
        int[] preD = new int[num];
        int INF = 999,min, nextNode = 0; // min holds the minimum value, nextNode holds the value for the next node.
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
            min = 999;
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

    private static Graph runAlgorithm (Graph g, int t){
        Graph g_output=new Graph(g.getNumOfVertex());
        g.sortEdges();
        LinkedList<Edge> edgeCopy=g.getEdges();
        int[] shortestPaths;
        while(!edgeCopy.isEmpty()){
            Edge e=edgeCopy.pop();
            shortestPaths=Dijkstra(g_output,e.getV_from());//calculate shortest paths to all nodes
            if(t*e.getV_weight() < shortestPaths[e.getV_to()]) //if(r*weight(e) < weight(P(u,v))
                g_output.addEdge(e); //add e to g_output
        }
        return g_output;
    }


    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        System.out.println("enter number of vertex");
        int v=in.nextInt();
        int numOfVertex=v;
        Graph g1=new Graph(v);
        System.out.println("Enter type of Graph:" +
                "\n   1- Random Graph" +
                "\n   2- K-Regular Random graph");
        v=in.nextInt();
        int counterFalse=0,flag=0,k,counterTrue=0;
        switch(v){
            case 1:            //random graph
                int maxEdges=(numOfVertex*(numOfVertex-1))/2;
                for(int i=0; i<maxEdges; i++)
                {
                    if(addRandomEdge(g1))
                        counterTrue++;
                }
                break;
            case 2:     //K-Regular Random graph
                System.out.println("Enter the K element");
                k=in.nextInt();
                int v_num=g1.getNumOfVertex();
                if(k>=v_num)
                    throw new IllegalArgumentException("k must be smaller then num_of_vertex!");
                if(k%2==1 && v_num%2==1)
                    throw  new IllegalArgumentException("Impossible to make K-regular graph while both  num_of_vertex and k are odds!");
                while(flag!=3)
                {
                    while(flag==0){
                        counterFalse++;
                        if(addRandomKEdge(g1,k))
                            counterTrue++;
                        //if all edges are settled- quit the loop
                        if(counterTrue==(v_num*k)/2)
                            flag=1;
                        if(counterFalse==v_num*k*10)
                            flag=2;
                    }
                    //if flag=1 ->succeed ->exit loop
                    if(flag==1)
                        flag=3;
                    //if flag!=1 ->not-succeed -> reset the graph and retry
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

        System.out.println("---------  The original Graph: --------");
        g1.printMatrixGraph(g1.getAdjListArray());
        System.out.println("number of succeeded Edge added:"+counterTrue+"\n");
        g1.printEdges();

        System.out.println("\nEnter the T spanner number");
        int t=in.nextInt();
        Graph result=runAlgorithm(g1,t);



        System.out.println("\n\n---------  The T-Spanner Graph: --------");
        result.printMatrixGraph(result.getAdjListArray());
        result.printEdges();
        System.out.println("number of succeeded Edge added:"+result.getEdges().size());

    }
}
