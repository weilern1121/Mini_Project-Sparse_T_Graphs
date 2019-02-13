import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Tests {
    private static void test1 (Graph[] g, int[]t2,int[]t3,int[]t5,int[]t10){
        Graph tmp_g=new Graph(g[0].getNumOfVertex());
        for(int i=0; i<g.length; i++){
            Graph.deepCopyGraph(g[i],tmp_g);
            t2[i]=SparseGraph.runAlgorithm(tmp_g,2).getEdges().size();
            Graph.deepCopyGraph(g[i],tmp_g);
            t3[i]=SparseGraph.runAlgorithm(tmp_g,3).getEdges().size();
            Graph.deepCopyGraph(g[i],tmp_g);
            t5[i]=SparseGraph.runAlgorithm(tmp_g,5).getEdges().size();
            Graph.deepCopyGraph(g[i],tmp_g);
            t10[i]=SparseGraph.runAlgorithm(tmp_g,10).getEdges().size();
        }
    }
    private static void convertGraph(int[][]a, Graph g){
        for (Edge e:g.getEdges()) {
            a[e.getV_to()][e.getV_from()]=e.getV_weight();
            a[e.getV_from()][e.getV_to()]=e.getV_weight();
        }
    }
    private static int[][][] test2Mst(Graph[] g){
        int Gsize=g[0].getNumOfVertex();
        int[][][] output=new int[g.length][Gsize][Gsize];
        for(int i=0;i<g.length;i++)
            convertGraph(output[i],g[i]);
        for(int i=0; i<g.length;i++)
            output[i]=SparseGraph.primMST(output[i],g[i].getNumOfVertex());
        return output;
    }
    private  static int calculateSumMst (int[][] a){
        int output=0;
        for(int i=0; i<a.length;i++)
            for(int j=i; j<a.length;j++)
                output+=a[i][j];
         return output;
    }
    private  static void test2(Graph[]g,int[] mstSize,int[] spannerSize2,int[] spannerSize3,int[] spannerSize5,int[] spannerSize10){
        int[][][] mstArr=test2Mst(g);
        Graph tmp_g=new Graph(g[0].getNumOfVertex());
        for(int i=0; i<mstSize.length;i++){
            mstSize[i]=calculateSumMst(mstArr[i]);
            Graph.deepCopyGraph(g[i],tmp_g);
            spannerSize2[i]=SparseGraph.runAlgorithm(tmp_g,2).getEdgesSize();
            spannerSize3[i]=SparseGraph.runAlgorithm(tmp_g,3).getEdgesSize();
            spannerSize5[i]=SparseGraph.runAlgorithm(tmp_g,5).getEdgesSize();
            spannerSize10[i]=SparseGraph.runAlgorithm(tmp_g,10).getEdgesSize();
        }
    }
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        System.out.println("Enter test number:" +
                "\n1- Test1 - size(G)" +
                "\n2- Test2 - weight(G)" +
                "\n3- Test3 - k-regular graph");
        int v=in.nextInt();
        int counterFalse=0,flag=0,k=0,counterTrue=0;
        System.out.println("Enter number of graphs");
        int numOfGraphs=in.nextInt();
        System.out.println("Enter number of vertices");
        int numOfVertex=in.nextInt();
        int sum2=0, sum3=0,sum5=0,sum10=0,sumG=0;
        //construct the graph array
        Graph[] graphs=new Graph[numOfGraphs];
        int[]t2=new int[numOfGraphs];
        int[]t3=new int[numOfGraphs];
        int[]t5=new int[numOfGraphs];
        int[]t10=new int[numOfGraphs];
        int[]counterTrues=new int [numOfGraphs];
        for(int i=0; i<numOfGraphs; i++)
            graphs[i]=new Graph(numOfVertex);
        switch (v){
            case 1:
                for(int i=0; i<numOfGraphs; i++)
                    counterTrues[i]=SparseGraph.constructGraph(graphs[i],counterFalse,flag,k,counterTrue,numOfVertex,1);
                System.out.println("the Graph edges counters: "+ Arrays.toString(counterTrues));
                test1(graphs,t2,t3,t5,t10);
                System.out.println("------test1 results: -----------");
                System.out.println("t=2 :  "+ Arrays.toString(t2));
                System.out.println("t=3 :  "+ Arrays.toString(t3));
                System.out.println("t=5 :  "+ Arrays.toString(t5));
                System.out.println("t=10 :  "+ Arrays.toString(t10));
                System.out.println("------test1 averages: -----------");
                for(int i=0; i<t2.length;i++){
                    sum2+=t2[i];
                    sum3+=t3[i];
                    sum5+=t5[i];
                    sum10+=t10[i];
                    sumG+=counterTrues[i];
                }
                System.out.println("added edges avg=  "+(double)sumG/t2.length);
                System.out.println("t=2 avg=  "+(double)sum2/t2.length);
                System.out.println("t=3 avg=  "+(double)sum3/t2.length);
                System.out.println("t=5 avg=  "+(double)sum5/t2.length);
                System.out.println("t=10 avg=  "+(double)sum10/t2.length);
                break;
            case 2:
                for(int i=0; i<numOfGraphs; i++)
                   SparseGraph.constructGraph(graphs[i],counterFalse,flag,k,counterTrue,numOfVertex,1);
                int[] mstSize=new int[graphs.length];
                int[] spannerSize2=new int[graphs.length];
                int[] spannerSize3=new int[graphs.length];
                int[] spannerSize5=new int[graphs.length];
                int[] spannerSize10=new int[graphs.length];
                double[] upperBound2=new double[graphs.length];
                double[] upperBound3=new double[graphs.length];
                double[] upperBound5=new double[graphs.length];
                double[] upperBound10=new double[graphs.length];
                test2(graphs,mstSize,spannerSize2,spannerSize3,spannerSize5,spannerSize10);
                for(int i=0; i<graphs.length;i++){
                    upperBound2[i]=mstSize[i]*(1+((double)numOfVertex/(2*2)));
                    upperBound3[i]=mstSize[i]*(1+((double)numOfVertex/(2*3)));
                    upperBound5[i]=mstSize[i]*(1+((double)numOfVertex/(2*5)));
                    upperBound10[i]=mstSize[i]*(1+((double)numOfVertex/(2*10)));
                }
                System.out.println("----  size  ----");
                System.out.println("mstSize array:  "+ Arrays.toString(mstSize));
                System.out.println("spannerSize2 array:  "+ Arrays.toString(spannerSize2));
                System.out.println("spannerSize3 array:  "+ Arrays.toString(spannerSize3));
                System.out.println("spannerSize5 array:  "+ Arrays.toString(spannerSize5));
                System.out.println("spannerSize10 array:  "+ Arrays.toString(spannerSize10));
                System.out.println("----  upper bound  ----");
                System.out.println("upperBound2 array:  "+ Arrays.toString(upperBound2));
                System.out.println("upperBound3 array:  "+ Arrays.toString(upperBound3));
                System.out.println("upperBound5 array:  "+ Arrays.toString(upperBound5));
                System.out.println("upperBound10 array:  "+ Arrays.toString(upperBound10));
                //calculate avg
                int sumMST=0,sumUp2=0,sumUp3=0,sumUp5=0,sumUp10=0;
                for(int i=0; i<graphs.length;i++){
                    sumMST+=mstSize[i];
                    sum2+=spannerSize2[i];
                    sum3+=spannerSize3[i];
                    sum5+=spannerSize5[i];
                    sum10+=spannerSize10[i];
                    sumUp2+=upperBound2[i];
                    sumUp3+=upperBound3[i];
                    sumUp5+=upperBound5[i];
                    sumUp10+=upperBound10[i];
                }
                System.out.println("----  avg  ----");
                System.out.println("MST avg=  "+(double)sumMST/numOfGraphs);
                System.out.println("t=2 avg=  "+(double)sum2/numOfGraphs);
                System.out.println("t=3 avg=  "+(double)sum3/numOfGraphs);
                System.out.println("t=5 avg=  "+(double)sum5/numOfGraphs);
                System.out.println("t=10 avg=  "+(double)sum10/numOfGraphs);
                System.out.println("t=2 upper-bound avg=  "+(double)sumUp2/numOfGraphs);
                System.out.println("t=3 upper-bound avg=  "+(double)sumUp3/numOfGraphs);
                System.out.println("t=5 upper-bound avg=  "+(double)sumUp5/numOfGraphs);
                System.out.println("t=10 upper-bound avg=  "+(double)sumUp10/numOfGraphs);
                System.out.println("----  proportion  ----");
                System.out.println("t=2/t=2 upper-bound=  "+(double)(sum2/numOfGraphs)/((double)sumUp2/numOfGraphs));
                System.out.println("t=3/t=3 upper-bound=  "+(double)(sum3/numOfGraphs)/((double)sumUp3/numOfGraphs));
                System.out.println("t=5/t=5 upper-bound=  "+(double)(sum5/numOfGraphs)/((double)sumUp5/numOfGraphs));
                System.out.println("t=10/t=10 upper-bound=  "+(double)(sum10/numOfGraphs)/((double)sumUp10/numOfGraphs));
                break;
            case 3:
                System.out.println("Enter k - regular argument");
                k=in.nextInt();
                for(int i=0; i<numOfGraphs; i++)
                    counterTrues[i]=SparseGraph.constructGraph(graphs[i],counterFalse,flag,k,counterTrue,numOfVertex,2);
                test1(graphs,t2,t3,t5,t10);
                System.out.println("------ test3 results: -----------");
                System.out.println("t=2 :  "+ Arrays.toString(t2));
                System.out.println("t=3 :  "+ Arrays.toString(t3));
                System.out.println("t=5 :  "+ Arrays.toString(t5));
                System.out.println("t=10 :  "+ Arrays.toString(t10));
                System.out.println("------ test3 averages: -----------");
                for(int i=0; i<t2.length;i++){
                    sum2+=t2[i];
                    sum3+=t3[i];
                    sum5+=t5[i];
                    sum10+=t10[i];
                    sumG+=counterTrues[i];
                }
                System.out.println("added edges avg=  "+(double)sumG/t2.length);
                System.out.println("t=2 avg=  "+(double)sum2/t2.length);
                System.out.println("t=3 avg=  "+(double)sum3/t2.length);
                System.out.println("t=5 avg=  "+(double)sum5/t2.length);
                System.out.println("t=10 avg=  "+(double)sum10/t2.length);
                break;
                default:
        }
    }
}
