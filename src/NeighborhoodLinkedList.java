public class NeighborhoodLinkedList {
    Node head;

    static class Node {
        Node next;
        Neighborhood n;

        Node(Neighborhood n) {
            this.n = n;
            this.next = null;
        }
    }

    // AddFirst for saving time cause Order doesn't matter
    public void add(Neighborhood n) {
        Node newNode = new Node(n);
        newNode.next = head;
        head = newNode;
    }

    public void delete(Neighborhood n) {
        Node iterator = this.head;
        Node prev = null;
        boolean found = false;

        if (iterator == null)
            System.out.println("Neighborhood Not Found");
        else {
            while (iterator != null) {
                if (iterator.n.equals(n)) {
                    found = true;
                    break;
                }
                prev = iterator;
                iterator = iterator.next;
            }
            if (found) {
                if (prev == null)
                    this.head = this.head.next;
                else
                    prev.next = iterator.next;
                System.out.println("Neighborhood \"" + n.name + "\" Successfully Deleted");
            } else
                System.out.println("Neighborhood Not Found");
        }
    }

    /*public static void main(String[] args) {
        Neighborhood n0 = new Neighborhood("omid");
        Neighborhood n1 = new Neighborhood("reza");
        Neighborhood n2 = new Neighborhood("borzoei");
        Neighborhood n3 = new Neighborhood("reza");
        linkedList ll = new linkedList();

        ll.add(n0);

        ll.add(n1);
        ll.delete(n3);
        ll.add(n2);
        Node x = ll.head;
        while (x.next != null) {
            System.out.println(x.n.name);
            x = x.next;
        }
        System.out.println(x.n.name);
    }*/
}
