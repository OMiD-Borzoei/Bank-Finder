public class KDTree {
    final static int k = 2;
    static Node best;
    static double bestDist;
    static int found, pCtr;
    int size = 0;
    Node root;

    static class Node {
        Bank bank;
        double[] point = new double[k];
        Node left;
        Node right;

        Node(Bank bank) {
            this.bank = bank;
            this.point[0] = bank.coordinate.getX();
            this.point[1] = bank.coordinate.getY();
            this.left = null;
            this.right = null;
        }
    }

    void add(Bank b) {
        this.root = addRec(this.root, new Node(b), 0);
        this.size++;
    }

    private Node addRec(Node root, Node n, int axis) {
        if (root == null)
            return n;

        //current axis (ca)
        int ca = axis % k;

        if (n.point[ca] < root.point[ca])
            root.left = addRec(root.left, n, axis + 1);
        else
            root.right = addRec(root.right, n, axis + 1);

        return root;
    }

    Bank search(Coordinate coordinate) {
        return searchRec(this.root, coordinate, 0);
    }

    private Bank searchRec(Node root, Coordinate coordinate, int axis) {
        if (root == null)
            return null;
        if (coordinate.isEqual(root.bank.coordinate))
            return root.bank;

        if (axis == 0)
            if (coordinate.getX() < root.point[0])
                return searchRec(root.left, coordinate, axis + 1);
            else
                return searchRec(root.right, coordinate, axis + 1);

        else if (coordinate.getY() < root.point[1])
            return searchRec(root.left, coordinate, axis - 1);
        else
            return searchRec(root.right, coordinate, axis - 1);
    }

    //axisToCompare = atc  the axis I want to find minimum value in
    Node minNode(Node x, Node y, Node z, int atc) {
        Node minSoFar = x;
        if (y != null && y.point[atc] < minSoFar.point[atc])
            minSoFar = y;
        if (z != null && z.point[atc] < minSoFar.point[atc])
            minSoFar = z;
        return minSoFar;
    }

    Node findMin(Node root, int axisToCompare) {
        return findMinRec(root, axisToCompare, 0);
    }

    private Node findMinRec(Node root, int atc, int axis) {
        if (root == null)
            return null;

        //current axis
        int ca = axis % k;

        if (ca == atc) {
            if (root.left == null)
                return root;
            return findMinRec(root.left, atc, axis + 1);
        }

        return minNode(root,
                findMinRec(root.left, atc, axis + 1),
                findMinRec(root.right, atc, axis + 1),
                atc);
    }

    private boolean areSame(double[] point1, double[] point2) {
        for (int i = 0; i < k; i++)
            if (point1[i] != point2[i])
                return false;
        return true;
    }

    private void copyNode(Node from, Node to) {
        for (int i = 0; i < k; i++)
            to.point[i] = from.point[i];
        to.bank = from.bank;
    }

    void delete(Coordinate c) {
        double[] point = new double[]{c.getX(), c.getY()};
        this.root = deleteRec(this.root, point, 0);
        this.size--;
    }

    private Node deleteRec(Node root, double[] point, int axis) {
        if (root == null)
            return null;


        //current axis (ca)
        int ca = axis % k;

        if (areSame(root.point, point)) {
            if (root.right != null) {
                Node min = findMin(root.right, ca);

                copyNode(min, root);

                root.right = deleteRec(root.right, min.point, axis + 1);

            } else if (root.left != null) {
                Node min = findMin(root.left, ca);
                copyNode(min, root);
                root.right = deleteRec(root.left, min.point, axis + 1);
                root.left = null;

            } else
                return null;
            return root;
        }
        if (point[ca] < root.point[ca])
            root.left = deleteRec(root.left, point, axis + 1);
        else
            root.right = deleteRec(root.right, point, axis + 1);
        return root;
    }

    Bank findNearest(Coordinate coordinate) {
        best = null;
        bestDist = Double.MAX_VALUE;
        findNearestRec(this.root, coordinate, 0);
        if (best == null)
            return null;
        return best.bank;
    }

    private void findNearestRec(Node root, Coordinate goal, int axis) {
        if (root == null) return;

        double distance, axisDistance, axisDistance2;
        int ca = axis % k;
        distance = goal.getDistance(root.bank.coordinate);
        if (ca == 0)
            axisDistance = root.bank.coordinate.getHorizontalDistance(goal);
        else
            axisDistance = root.bank.coordinate.getVerticalDistance(goal);
        axisDistance2 = axisDistance * axisDistance;

        if (best == null || distance < bestDist) {
            best = root;
            bestDist = distance;
        }

        if (bestDist == 0) return;

        findNearestRec(axisDistance > 0 ? root.left : root.right, goal, axis + 1);
        if (axisDistance2 >= bestDist) return;
        findNearestRec(axisDistance > 0 ? root.right : root.left, goal, axis + 1);
    }

    void allInNeighbor(Neighborhood n) {
        found = 0;
        allInNeighborRec(this.root, n, 0);
        if (found == 0)
            System.out.println(City.ANSI_YELLOW + "There Are No Banks In \"" + n.name + "\" Neighborhood !" + City.ANSI_RESET);
    }

    private void allInNeighborRec(Node root, Neighborhood n, int axis) {
        if (root == null) return;
        double x = root.bank.coordinate.x, y = root.bank.coordinate.y;
        if (axis == 0) {
            if (n.rightEdge <= x)
                allInNeighborRec(root.left, n, 1);
            else if (n.leftEdge >= x)
                allInNeighborRec(root.right, n, 1);
            else {
                if (n.downEdge < y && y < n.upEdge)
                    printBank(root.bank);
                allInNeighborRec(root.left, n, 1);
                allInNeighborRec(root.right, n, 1);
            }
        } else {
            if (n.upEdge <= y)
                allInNeighborRec(root.left, n, 0);
            else if (n.downEdge >= y)
                allInNeighborRec(root.right, n, 0);
            else {
                if (n.leftEdge < x && x < n.rightEdge)
                    printBank(root.bank);
                allInNeighborRec(root.left, n, 0);
                allInNeighborRec(root.right, n, 0);
            }
        }
    }

    void allInRadius(Coordinate c, double r) {
        found = 0;
        allInRadiusRec(this.root, c, r, 0);
        if (found == 0)
            System.out.println(City.ANSI_YELLOW + "No Banks Found In that Region !" + City.ANSI_RESET);
    }

    private void allInRadiusRec(Node root, Coordinate c, double r, int axis) {
        if (root == null) return;

        double x = root.bank.coordinate.x, y = root.bank.coordinate.y;
        double lE = c.getX() - r, rE = c.getX() + r;
        double dE = c.getY() - r, uE = c.getY() + r;

        if (axis == 0) {
            if (rE <= x)
                allInRadiusRec(root.left, c, r, 1);
            else if (lE >= x)
                allInRadiusRec(root.right, c, r, 1);
            else {
                if (dE < y && y < uE && c.getDistance(root.bank.coordinate) < r)
                    printBank(root.bank);
                allInRadiusRec(root.left, c, r, 1);
                allInRadiusRec(root.right, c, r, 1);
            }
        } else {
            if (uE <= y)
                allInRadiusRec(root.left, c, r, 0);
            else if (dE >= y)
                allInRadiusRec(root.right, c, r, 0);
            else {
                if (lE < y && y < rE && c.getDistance(root.bank.coordinate) < r)
                    printBank(root.bank);
                allInRadiusRec(root.left, c, r, 0);
                allInRadiusRec(root.right, c, r, 0);
            }
        }
    }

    private void printBank(Bank b) {
        if (b instanceof Branch)
            System.out.println(City.ANSI_BLUE + ++found + ". Branch \"" + b.name +
                    "\" from mainBank \"" + ((Branch) b).bankName +
                    "\" with Coordinates x: " + b.coordinate.x + " ,y: " + b.coordinate.y + City.ANSI_RESET);
        else
            System.out.println(City.ANSI_BLUE + ++found + ". Bank \"" + b.name +
                    "\" with Coordinates x: " + b.coordinate.x + " ,y: " + b.coordinate.y + City.ANSI_RESET);
    }

    boolean print() {
        pCtr = 0;
        if (this.root == null) {
            System.out.println(City.ANSI_YELLOW + "Nothing Found !" + City.ANSI_RESET);
            return false;
        }
        print(this.root);
        return true;
    }

    private void print(Node root) {
        if (root == null)
            return;
        System.out.println(City.ANSI_BLUE + ++pCtr + ". " + root.bank.name + "  x: " + root.bank.coordinate.getX() + " ,y: " + root.bank.coordinate.getY() + City.ANSI_RESET);
        print(root.left);
        print(root.right);
    }
}

