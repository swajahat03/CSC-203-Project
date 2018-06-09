import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AStarPathingStrategy implements PathingStrategy {

    public class Node { //Using Nodes to store information (g and h distance & previous Node) with Point
        Point p;
        Node previous;
        int g;
        int h;

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (getClass() != o.getClass()) {
                return false;
            }
            final Node other = (Node) o;
            if (this.p.x != other.p.x) {
                return false;
            }
            if (this.p.y != other.p.y) {
                return false;
            }
            return true;

        }


        public Node(Point p) {
            this.p = p;
            this.g = 0;
            this.h = 0;
            this.previous = null;
        }

        public Node getPrevious() {
            if (this.previous == null) {
                Node returnNode = new Node(this.p);
                return returnNode;
            }
            return previous;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "p=" + p +
                    ", previous=" + previous +
                    ", g=" + g +
                    ", h=" + h +
                    '}';
        }
    }

    public Node lowestF(List<Node> inputList) { //Helper method to find the node with lowest F in given list
        Node smalledFNode = inputList.get(0);
        for(int i=0;i<inputList.size();i++){
            if((inputList.get(i).h + inputList.get(i).g) < (smalledFNode.g + smalledFNode.h)){
                smalledFNode = inputList.get(i);
            }
        }
        return smalledFNode;
    }

    public LinkedList<Point> constructPath(Node start, Node end){ //Given the last Node, constructs and returns full path
        LinkedList<Point> path = new LinkedList<>();
        Node current = end;
        boolean flag = false;
        while(!flag){
            path.addFirst(current.p);
            current = current.getPrevious();

            if(current.p.equals(start.p)){
                flag = true;
            }
        }
        return path;
    }

    public boolean isInClosed(List<Node> closedList, Point pt){ //Checks if input point is found in closed list
        boolean flag = false;
        for (Node n : closedList){
            if(n.p.equals(pt)){
                flag = true;
            }
        }
        return flag;
    }

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {

        List<Node> open = new LinkedList<>();
        List<Node> closed = new LinkedList<>();
        Node startNode = new Node(start);
        startNode.h =  Math.abs((start.x - end.x) + Math.abs(start.y - end.y));
        open.add(startNode);
        Node current;
        List<Point> tempList;
        List<Node> nodeList = new LinkedList<>();
        boolean flag = false;
        while (!flag) {
            current = lowestF(open);
            closed.add(current);
            open.remove(current);
            if (current.p.equals(end)) {
                return constructPath(new Node(start), current);
            }

            tempList = potentialNeighbors.apply(current.p).filter(canPassThrough).filter(pt ->
                    !pt.equals(start) && !isInClosed(closed,pt)).collect(Collectors.toList());

            //System.out.println(tempList.toString());
            for (Point thisPt : tempList) { //Transfer to node
                Node newNode = new Node(thisPt);
                newNode.h = Math.abs((newNode.p.x - end.x)) + Math.abs(newNode.p.y - end.y);
                newNode.g = Math.abs((newNode.p.x - start.x)) + Math.abs(newNode.p.y - start.y);
                nodeList.add(newNode);
            }

            if(current.p.x == end.x && (current.p.y + 1) == end.y){ //The point above is end
                Node newNode = new Node(new Point(current.p.x,current.p.y+1));
                newNode.h = Math.abs((newNode.p.x - end.x)) + Math.abs(newNode.p.y - end.y);
                newNode.g = Math.abs((newNode.p.x - start.x)) + Math.abs(newNode.p.y - start.y);
                nodeList.add(newNode); }
            if (current.p.x == end.x && (current.p.y - 1) == end.y){ //down
                Node newNode = new Node(new Point(current.p.x,current.p.y-1));
                newNode.h = Math.abs((newNode.p.x - end.x)) + Math.abs(newNode.p.y - end.y);
                newNode.g = Math.abs((newNode.p.x - start.x)) + Math.abs(newNode.p.y - start.y);
                nodeList.add(newNode); }
            if ((current.p.x + 1) == end.x && (current.p.y) == end.y){
                Node newNode = new Node(new Point(current.p.x + 1,current.p.y)); //right
                newNode.h = Math.abs((newNode.p.x - end.x)) + Math.abs(newNode.p.y - end.y);
                newNode.g = Math.abs((newNode.p.x - start.x)) + Math.abs(newNode.p.y - start.y);
                nodeList.add(newNode); }
            if ((current.p.x - 1) == end.x && (current.p.y) == end.y){
                Node newNode = new Node(new Point(current.p.x - 1,current.p.y));
                newNode.h = Math.abs((newNode.p.x - end.x)) + Math.abs(newNode.p.y - end.y);
                newNode.g = Math.abs((newNode.p.x - start.x)) + Math.abs(newNode.p.y - start.y);
                nodeList.add(newNode);
            }

            for (int i = 0; i < nodeList.size(); i++) {
                Node thisNode = nodeList.get(i);
                if (!open.contains(thisNode)) {
                    thisNode.previous = current;
                    thisNode.h = Math.abs((thisNode.p.x - end.x)) + Math.abs(thisNode.p.y - end.y);
                    thisNode.g = Math.abs((thisNode.p.x - start.x)) + Math.abs(thisNode.p.y - start.y);
                    open.add(thisNode);

                } else {
                    if (thisNode.g > thisNode.getPrevious().g) {
                        thisNode.previous = current;
                        thisNode.g = Math.abs((thisNode.p.x - start.x)) + Math.abs(thisNode.p.y - start.y);
                    }
                }
            }

            nodeList = new LinkedList<>();

            if (open.isEmpty()) {
                return null;
            }
        }
        return null;
    }


}