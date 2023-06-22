import java.util.List;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.LinkedList;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.HashMap;
public class AstarPathingStrategy implements PathingStrategy{
    private static class Node {
        private Point point;
        private int g;
        private int h;
        private int f;
        private Node parent;

        public Node(Point point, int g, int h, Node parent) {
            this.point = point;
            this.g = g;
            this.h = h;
            this.f = g + h;
            this.parent = parent;
        }
    }

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {
        List<Point> path = new LinkedList<>();
        /*define closed list
          define open list
          while (true){
            Filtered list containing neighbors you can actually move to
            Check if any of the neighbors are beside the target
            set the g, h, f values
            add them to open list if not in open list
            add the selected node to close list
          return path*/
        List<Node> closedList = new ArrayList<>();
        //lambda function determines which node to move to based on f value (lowest is chosen)
        PriorityQueue<Node> openList = new PriorityQueue<>((n1, n2) -> Double.compare(n1.f, n2.f));
        //Add start node to the open list and mark it as the current node
        Node startNode = new Node(start, 0, hCost(start, end), null);
        openList.add(startNode);

        HashMap<Point, Node> nodeMap = new HashMap<>();
        nodeMap.put(start, openList.peek());

        while (!openList.isEmpty()){
            Node current = openList.poll();
            Point currentPoint = current.point;
            //check if current Point touches the end
            if (withinReach.test(currentPoint, end)){
                Node pathNode = current;
                //Going backwards to create the path
                while (pathNode != startNode){
//                    path.add(0, pathNode.point);
                    path.add(0, pathNode.point);
                    pathNode = pathNode.parent;
                }
                break;
            }
            //add the current node to the closed list
            closedList.add(current);
            potentialNeighbors.apply(currentPoint)
                    //check if not an obstacle
                    .filter(canPassThrough)
                    .forEach(neighbor -> {
                        int g = current.g + 1;
                        int h = hCost(neighbor, end);
                        int f = g + h;
                        Node newNode = nodeMap.get(neighbor);
//                        //search through closedList to
//                        for (Node node: closedList){
//                            if (node.point.equals(neighbor)){
//                                newNode = node;
//
//                            }
//                        }
                        if (newNode != null && g >= newNode.g){
                            return;
                        }
                        //search through openList for
//                        for (Node node: openList){
//                            if (node.point.equals(neighbor)){
//                                newNode = node;
//
//                            }
//                        }
                        if (newNode == null) {
                            //best node has been found
                            newNode = new Node(neighbor, g, h, current);
                            openList.add(newNode);
                            nodeMap.put(neighbor, newNode);
                        }
                        else {
                            newNode.g = g;
                            newNode.h = h;
                            newNode.f = f;
                            newNode.parent = current;
                        }
                    });
        }
        return path;
    }

    private int hCost(Point current, Point end){
        return Math.abs(current.x - end.x) + Math.abs(current.y - end.y);
    }
}