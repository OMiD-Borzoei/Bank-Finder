public class Control {

    Stack st;

    Control() {
        st = new Stack();
    }

    void Add(String command, String name) {
        st.push(new god(command, name));
    }

    void Add(String command, Branch br) {
        st.push(new god(command, br));
    }

    int undo(int x) {
        if (st.size < x)
            return 0;
        if (st.size == x)
            return 1;
        while (st.size > x) {
            god g = st.pop();
            evaluate(g);
        }
        return 2;
    }

    void evaluate(god g) {
        switch (g.command) {
            case "addN" -> City.cityNeighborhoods.delete(g.name);
            case "addB" -> {
                Bank deleted = City.cityMainBanks.delete(g.name);
                if (deleted != null)
                    City.cityAllBanks.delete(deleted.coordinate);
            }
            case "addBr" -> {
                City.cityAllBanks.delete(g.br.coordinate);
                City.cityMainBanks.alter(g.br.bankName, g.br, "delete");
            }
            case "delBr" -> {
                City.cityAllBanks.add(g.br);
                City.cityMainBanks.alter(g.br.bankName, g.br, "add");
            }
        }
    }

    static class Stack {

        int size;

        Stack() {
            this.size = 0;
        }

        linkedList ll = new linkedList();

        void push(god g) {
            ll.addFirst(g);
            this.size++;
        }

        god pop() {
            this.size--;
            return ll.deleteFirst().g;
        }

        static class linkedList {
            Node head;

            static class Node {
                god g;
                Node next;

                Node(god g) {
                    this.g = g;
                    this.next = null;
                }
            }

            void addFirst(god g) {
                Node newNode = new Node(g);
                newNode.next = this.head;
                this.head = newNode;
            }

            Node deleteFirst() {
                Node first = this.head;
                if (first == null)
                    return null;
                this.head = this.head.next;
                return first;
            }

            void print() {
                int i = 0;
                if (this.head == null)
                    System.out.println(City.ANSI_YELLOW + "U R at Time 0" + City.ANSI_RESET);
                else {
                    Node it = this.head;
                    while (it != null) {
                        System.out.print(City.ANSI_BLUE + it.g.command + " : ");
                        System.out.println(it.g.name == null ? it.g.br.name + " to " + it.g.br.bankName : it.g.name+City.ANSI_RESET);
                        it=it.next;
                    }
                }

            }
        }

    }
}

class god {
    String command;
    String name;
    Branch br;

    god(String command, String name) {
        this.command = command;
        this.name = name;
        this.br = null;
    }

    god(String command, Branch br) {
        this.command = command;
        this.name = null;
        this.br = br;
    }
}
