public class Neighborhood {
    double leftEdge, rightEdge;
    double upEdge, downEdge;
    String name;

    Neighborhood(String name, double leftEdge, double downEdge, double rightEdge, double upEdge) {
        this.name = name;
        this.leftEdge = leftEdge;
        this.rightEdge = rightEdge;
        this.upEdge = upEdge;
        this.downEdge = downEdge;
    }

    boolean isInside(Coordinate c) {
        return c.getX() > leftEdge && c.getX() < rightEdge &&
                c.getY() > downEdge && c.getY() < upEdge;
    }

    boolean isEqual(Neighborhood n) {
        return this.name.equals(n.name) &&
                this.leftEdge == n.leftEdge && this.rightEdge == n.rightEdge &&
                this.downEdge == n.downEdge && this.upEdge == n.upEdge;
    }
}

class NeighborhoodDB {
    neighLinkedList[][][] DB;

    NeighborhoodDB() {
        DB = new neighLinkedList[26][26][26];  // memory : ( (26^3) * ( (4*8) + (30*2) ) ) / 2^20 = 1.54 MB
        for (int i = 0; i < 26; i++)
            for (int j = 0; j < 26; j++)
                for (int k = 0; k < 26; k++)
                    DB[i][j][k] = new neighLinkedList();
    }

    String addToName(String n) {
        StringBuilder name = new StringBuilder(n);

        for (int i = n.length(); i < 3; i++)
            name.append((char) ((name.charAt(i - n.length()) * (i + 13)) % 27 + 'a'));

        return name.toString();
    }

    int[] getIndexes(String n) {
        String newName = n.length() < 3 ? addToName(n) : n;
        int[] ins = new int[3]; //indexes
        for (int i = 0; i < 3; i++)
            if (newName.charAt(i) == ' ')
                ins[i] = 'q' - 'a';
            else
                ins[i] = (newName.charAt(i) - 'a');

        return ins;
    }


    boolean add(Neighborhood n) {
        int[] ins = getIndexes(n.name);
        return DB[ins[0]][ins[1]][ins[2]].add(n);

    }

    Neighborhood search(String name) {
        int[] ins = getIndexes(name);
        return DB[ins[0]][ins[1]][ins[2]].search(name);
    }

    void delete(String name){
        int[] ins=getIndexes(name);
        DB[ins[0]][ins[1]][ins[2]].delete(name);
    }
}

class neighLinkedList {
    Node head;

    static class Node {
        Neighborhood n;
        Node next;

        Node(Neighborhood n) {
            this.n = n;
            next = null;
        }
    }

    neighLinkedList() {
        this.head = null;
    }

    boolean add(Neighborhood n) {
        Node newNode = new Node(n);
        if (this.head == null)
            this.head = newNode;
        else {
            Node last = this.head;
            while (last.next != null) {
                if (last.n.name.equals(n.name))
                    return false;
                last = last.next;
            }
            if (last.n.name.equals(n.name))
                return false;
            last.next = newNode;
        }
        return true;
    }

    Neighborhood search(String name) {
        if (this.head == null)
            return null;
        Node last = this.head;
        while (last.next != null) {
            if (last.n.name.equals(name))
                return last.n;
            last = last.next;
        }
        return last.n.name.equals(name) ? last.n : null;
    }

    boolean delete(String name) {
        if (this.head == null)
            return false;
        if (this.head.n.name.equals(name)) {
            this.head = this.head.next;
            return true;
        }
        Node last = this.head.next;
        Node prev = this.head;
        while (last.next != null) {
            if (last.n.name.equals(name)) {
                prev.next = last.next;
                return true;
            }
            prev = last;
            last = last.next;
        }

        if (last.n.name.equals(name)) {
            prev.next = null;
            return true;
        }
        return false;
    }
}


