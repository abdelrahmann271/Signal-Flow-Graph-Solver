
import java.util.ArrayList;
import java.util.Vector;

public class GraphUtil {

    public ArrayList<Pair<Integer,Integer>> loops = new ArrayList<Pair<Integer,Integer>>();
    public Vector<Vector<Pair<Integer, Integer>>> forwardPaths = new Vector<Vector<Pair<Integer,Integer>>>();
    public Vector<Vector<Pair<Integer, Integer>>> loopPaths = new Vector<Vector<Pair<Integer,Integer>>>();
    private Boolean is;
    private int v;
    private int loopStart;
    private Pair<Integer,Integer> p ;
    private ArrayList<Pair<Integer, Integer>>[] adjList;
    private Vector<Vector<Integer>> allLoopsComb = new Vector<Vector<Integer>>() ;

    //Constructor.
    public GraphUtil(int vertices){

        this.v = vertices;
        initAdjList();
    }

    //Initialize Graph.
    @SuppressWarnings("unchecked")
    private void initAdjList()
    {
        adjList = new ArrayList[v];

        for(int i = 0; i < v; i++)
        {
            adjList[i] = new ArrayList<>();
        }
    }

    //Insert Loops (Avoiding Duplicates)
    private void insertLoop(Integer l , Integer m) {
        for(int i = 0 ; i < loops.size() ; i++) {
            if(l == loops.get(i).getKey() && m == loops.get(i).getValue() ) {
                return;
            }
        }
        loops.add(new Pair<Integer, Integer>(l,m));
    }

    //Adding Edges
    public void addEdge(int u, int v , int weight)
    {
        // Add v to u's list. 
        Pair<Integer, Integer> j = new Pair<Integer, Integer>(v, weight);
        adjList[u].add(j);
    }

    //Finding All Forward Paths.
    public void printAllPaths(int s, int d)
    {
        is = true;
        boolean[] isVisited = new boolean[v];
        Vector<Pair<Integer,Integer>> pathList = new Vector<>();

        //add source to path[] 
        Pair<Integer, Integer> k = new Pair<Integer, Integer>(s,1);
        pathList.add(k);

        //Call recursive utility 
        printAllPathsUtil(s, d, isVisited, pathList);
    }

    //Finding All Loops.
    public void printAllLoops()
    {
        is = false;
        for(int i = 0 ; i < loops.size() ; i++) {
            boolean[] isVisited = new boolean[v];
            Vector<Pair<Integer,Integer>> loopList = new Vector<>();
            Pair<Integer, Integer> k = new Pair<Integer, Integer>(loops.get(i).getValue(),1);
            loopList.add(k);
            loopStart = loops.get(i).getValue();
            for(int j = 0 ; j < loops.get(i).getValue() ; j++ ) {
                isVisited[j] = true;
            }
            printAllPathsUtil(loops.get(i).getValue(), loops.get(i).getKey(), isVisited, loopList);
        }

    }

    private void printAllPathsUtil(Integer u, Integer d,
                                   boolean[] isVisited,
                                   Vector<Pair<Integer,Integer>> localPathList) {

        // Mark the current node 
        isVisited[u] = true;

        //Find Loops.
        for (Pair<Integer, Integer> i : adjList[u])
        {
            if (isVisited[i.getKey()] && i.getKey()<= u)  {
                insertLoop(u, i.getKey());
            }
        }

        if (u.equals(d) && is)
        {
            forwardPaths.add(new Vector<>());
            for(int i = 0 ; i < localPathList.size() ; i++) {
                forwardPaths.get(forwardPaths.size()-1).add(new Pair<Integer, Integer>(localPathList.get(i).getKey(),localPathList.get(i).getValue()));
            }
            // if match found then no need to traverse more till depth 
            isVisited[u]= false;
            return ;
        }
        else if (u.equals(d)) {
            loopPaths.add(new Vector<>());
            for(int i = 0 ; i < localPathList.size() ; i++) {
                loopPaths.get(loopPaths.size()-1).add(new Pair<Integer, Integer>(localPathList.get(i).getKey(),localPathList.get(i).getValue()));
            }

            //Add the last Edge of ( the last node to the first node )
            for(int j = 0 ; j < adjList[d].size() ; j++) {
                if(adjList[d].get(j).getKey() == loopStart) {
                    int c = adjList[d].get(j).getValue();
                    loopPaths.get(loopPaths.size()-1).get( loopPaths.get(loopPaths.size()-1).size()-1 ).setValue( c );

                }
            }
            // if match found then no need to traverse more till depth 
            isVisited[u]= false;
            return ;
        }
        //Recursion on Vertices
        for (Pair<Integer, Integer> i : adjList[u])
        {
            if (!isVisited[i.getKey()])
            {
                // store current node  
                // in path[] 
                Pair<Integer, Integer> p = new Pair<Integer, Integer>(i.getKey(), 1);
                localPathList.get(localPathList.size()-1).setValue(i.getValue());
                localPathList.add(p);
                printAllPathsUtil(i.getKey(), d, isVisited, localPathList);
                // remove current node 
                // in path[] 
                localPathList.remove(p);
            }
            else if (isVisited[i.getKey()] && i.getKey()<= u)  {
                insertLoop(u, i.getKey());
            }
        }
        // Mark the current node 
        isVisited[u] = false;
    }
    public Double CalculateMasonFormula() {

        Double delta = (double) 1 ;
        Vector<Double> loopsGains = new Vector<Double>(loopPaths.size());
        Vector<Double> pathsGains = new Vector<Double>();
        Vector<Double> deltaI = new Vector<Double>();

        //Calculate Loops Gains.
        for(int i = 0 ; i < loopPaths.size() ; i++) {
            double gain = 1 ;
            for(int j = 0 ; j < loopPaths.get(i).size() ; j++) {
                gain *= loopPaths.get(i).get(j).getValue();
            }
            loopsGains.add(gain);
        }
        //Print Loops Gains.
        System.out.println("Loops Gains");
        for(int i = 0 ; i < loopsGains.size() ; i++) {
            System.out.print(loopsGains.get(i) + " ");
        }
        System.out.println();

        //Find each two untouched loops.
        Vector<Pair<Integer, Integer>> UntouchedLoopsIndex = new Vector<Pair<Integer,Integer>>();
        for(int i = 0 ; i < loopPaths.size() ; i++) {
            for(int j = i+1 ; j < loopPaths.size() ; j++ ) {
                if(!IsTouched(loopPaths.get(i), loopPaths.get(j))) {
                    UntouchedLoopsIndex.add(new Pair<Integer, Integer>(i, j));
                }
            }
        }
        //Print Untouched Loops.
/*        System.out.println("Untouched loops:");
        for(int i = 0 ; i < UntouchedLoopsIndex.size() ; i++) {
            System.out.println(UntouchedLoopsIndex.get(i).getKey() + " " + UntouchedLoopsIndex.get(i).getValue() );
        }*/

        //Calculate Delta of all the system.
        for(int i = 0 ; i < loopsGains.size() ; i++) {
            delta -= loopsGains.get(i);
        }
        for(int i = 0 ; i < UntouchedLoopsIndex.size() ; i++) {
            delta += loopsGains.get( UntouchedLoopsIndex.get(i).getKey() ) * loopsGains.get( UntouchedLoopsIndex.get(i).getValue() ) ;
        }
        //All comb.
        int[] loops = new int[loopPaths.size()];
        for(int i = 0 ; i < loops.length ; i++) {
            loops[i] = i;
        }
        //All combinations of loops
        //Getting all combinations of suitable size

        for(int r = 3 ; r < loopPaths.size() + 1 ; r++) {

            printCombination(loops, loops.length, r);
        }
        //Adding suitable loops
        for(int i = 0 ; i < allLoopsComb.size() ; i++) {
            int flag = 0;
            for(int j = 0 ; j < allLoopsComb.get(i).size() ;j++) {

/*                System.out.println(allLoopsComb.get(i).get(j));*/
                for(int k = j + 1 ; k < allLoopsComb.get(i).size() ; k++ ) {
                    if( IsTouched(loopPaths.get( allLoopsComb.get(i).get(j) ), loopPaths.get( allLoopsComb.get(i).get(k) ))) {
                        flag = 1;
                        break;
                    }
                }
                if(flag == 1) {
                    break;
                }
            }
            if(flag == 0 ) { //add the combination
                int b = 1;
                for(int l = 0 ; l < allLoopsComb.get(i).size() ; l++ ) {
                    b *= loopsGains.get(allLoopsComb.get(i).get(l)) ;
                }
/*                System.out.println("bbb is " + b);*/
                if( allLoopsComb.get(i).size() % 2 == 1) {

                    delta -= b;
                }
                else {
                    delta += b ;
                }
            }
        }
        //Print Delta of all the system.
        System.out.println("Delta of all the system");
        System.out.println(delta);

        //Calculate forward paths gains.
        for(int i = 0 ; i < forwardPaths.size() ; i++) {
            double gain = 1 ;
            for(int j = 0 ; j < forwardPaths.get(i).size() ; j++) {
                gain *= forwardPaths.get(i).get(j).getValue();
            }
            pathsGains.add(gain);
        }
        //Print all paths gains.
        System.out.println("Paths Gains");
        for(int i = 0 ; i < pathsGains.size() ; i++) {
            System.out.print(pathsGains.get(i) +" ");
        }
        System.out.println();
        //Calculate P(i)s
        for(int i = 0 ; i < forwardPaths.size() ; i++) {

            //Finding untouched loops of the current path.
            Vector<Integer> loopsUntouched = new Vector<Integer>(); //Boolean vector of untouched loops.
            for(int j = 0 ; j < loopPaths.size() ; j++ ) {

                if(!IsTouched(forwardPaths.get(i),  loopPaths.get(j))) {
                    loopsUntouched.add(1);
                }
                else {
                    loopsUntouched.add(0);
                }
            }
            //Calculate deltaI
            double deltap =1;
            //Calculate first term.
            for(int q = 0 ; q < loopsGains.size() ; q++) {
                if(loopsUntouched.get(q) == 1) {
                    deltap -= loopsGains.get(q);
                }
            }
            //Calculate Second Term.
            for(int f = 0 ; f < UntouchedLoopsIndex.size() ; f++) {
                if( loopsUntouched.get(UntouchedLoopsIndex.get(f).getKey()) == 1 && loopsUntouched.get(UntouchedLoopsIndex.get(f).getValue()) == 1 ) {
                    deltap += loopsGains.get( UntouchedLoopsIndex.get(f).getKey() ) * loopsGains.get( UntouchedLoopsIndex.get(f).getValue() ) ;
                }
            }
            //Calculate the other terms.
            for(int k = 0 ; k < allLoopsComb.size() ; k++) {
                int flag = 0 ;
                for(int l = 0 ; l < allLoopsComb.get(i).size() ;l++) {
                    if(IsTouched(loopPaths.get(allLoopsComb.get(k).get(l)), forwardPaths.get(i))) {
                        flag = 1;
                        break;
                    }
                }
                if(flag == 0 ) { //add the combination
                    int b = 1;
                    for(int l = 0 ; l < allLoopsComb.get(k).size() ; l++ ) {
                        b *= loopsGains.get(allLoopsComb.get(k).get(l)) ;
                    }
                    if( allLoopsComb.get(k).size() % 2 == 1) {

                        deltap -= b;
                    }
                    else {
                        deltap += b ;
                    }
                }

            }
            deltaI.add(deltap);
        }
        //Print P(i)s.
        System.out.println("detlta P(i)s");
        for(int i = 0 ; i < forwardPaths.size() ; i++) {
            System.out.print(deltaI.get(i) + " ");
        }
        //Calculate The formula.
        Double answer = (double) 0;
        for(int i = 0 ; i < forwardPaths.size() ; i++) {
            answer += deltaI.get(i) *  pathsGains.get(i) ;
        }
        answer /= delta;
        System.out.println();
        System.out.println("Answer");
        System.out.println(answer);
        return answer;
    }

    public Boolean IsTouched (Vector<Pair<Integer, Integer>> firstVector , Vector<Pair<Integer, Integer>> SecondVector ) {
        for(int i = 0 ; i < firstVector.size() ; i++) {
            for(int j = 0 ; j < SecondVector.size() ; j++) {
                if(firstVector.get(i).getKey() == SecondVector.get(j).getKey() ) {
                    return true;
                }
            }
        }
        return false;
    }


    private void combinationUtil(int arr[], int data[], int start,
                                 int end, int index, int r)
    {
        if (index == r)
        {
            Vector<Integer> vect = new Vector<Integer>();
/*            System.out.println("r is " + r);*/
            for (int j=0; j<r; j++) {
                vect.add(data[j]);
//            	System.out.print(data[j]+" "); 
            }
            allLoopsComb.add(vect);
/*            System.out.println("added");*/
            return;
        }
        for (int i=start; i<=end && end-i+1 >= r-index; i++)
        {
            data[index] = arr[i];
            combinationUtil(arr, data, i+1, end, index+1, r);
        }
    }
    private void printCombination(int arr[], int n, int r)
    {
        int data[]=new int[r];
        combinationUtil(arr, data, 0, n-1, 0, r);
    }


}