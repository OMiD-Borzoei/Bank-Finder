public class Bank {
    Coordinate coordinate;
    String name;

    Bank(Coordinate coordinate, String name) {
        this.coordinate = coordinate;
        this.name = name;
    }
}

class mainBank extends Bank {
    KDTree branches;

    mainBank(Coordinate coordinate, String name) {
        super(coordinate, name);
        this.branches = new KDTree();
    }

    mainBank(Coordinate coordinate, String name, KDTree branches) {
        super(coordinate, name);
        this.branches = branches;
    }
}

class Branch extends Bank {
    String bankName;

    Branch(Coordinate coordinate, String bankName, String name) {
        super(coordinate, name);
        this.bankName = bankName;
    }
}


class MainBanksDB {
    bankLinkedList[][][] DB;

    MainBanksDB() {
        DB = new bankLinkedList[26][26][26];
        for (int i = 0; i < 26; i++)
            for (int j = 0; j < 26; j++)
                for (int k = 0; k < 26; k++)
                    DB[i][j][k] = new bankLinkedList();
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

    boolean add(mainBank b) {
        int[] ins = getIndexes(b.name);
        return DB[ins[0]][ins[1]][ins[2]].add(b);

    }

    mainBank search(String name) {
        int[] ins = getIndexes(name);
        return DB[ins[0]][ins[1]][ins[2]].search(name);
    }

    void alter(String name, Branch br, String command) {
        int[] ins = getIndexes(name);
        DB[ins[0]][ins[1]][ins[2]].alter(name, br, command);
    }

    Bank delete(String name){
        int[] ins=getIndexes(name);
        return DB[ins[0]][ins[1]][ins[2]].delete(name);
    }

}

class bankLinkedList {
    Node head;

    static class Node {
        mainBank b;
        Node next;

        Node(mainBank b) {
            this.b = b;
            next = null;
        }
    }

    bankLinkedList() {
        this.head = null;
    }


    boolean add(mainBank b) {
        Node newNode = new Node(b);
        if (this.head == null)
            this.head = newNode;
        else {
            Node last = this.head;
            while (last.next != null) {
                if (last.b.name.equals(b.name))
                    return false;
                last = last.next;
            }
            if (last.b.name.equals(b.name))
                return false;
            last.next = newNode;
        }
        return true;
    }

    mainBank search(String name) {
        if (this.head == null)
            return null;
        Node last = this.head;
        while (last.next != null) {
            if (last.b.name.equals(name))
                return last.b;
            last = last.next;
        }
        return last.b.name.equals(name) ? last.b : null;
    }

    void alter(String name, Branch br, String command) {
        if (this.head == null)
            return ;
        Node last = this.head;
        while (last.next != null) {
            if (last.b.name.equals(name)) {
                if (command.equals("delete"))
                    last.b.branches.delete(br.coordinate);
                else if (command.equals("add"))
                        last.b.branches.add(br);
                break;
            }
            last = last.next;
        }
        if (last.b.name.equals(name)) {
            if (command.equals("delete"))
                last.b.branches.delete(br.coordinate);
            else if (command.equals("add"))
                    last.b.branches.add(br);
        }
    }

    Bank delete(String name) {
        if (this.head == null)
            return null;
        if (this.head.b.name.equals(name)) {
            Bank newB=this.head.b;
            this.head = this.head.next;
            return newB;
        }
        bankLinkedList.Node last = this.head.next;
        bankLinkedList.Node prev = this.head;
        while (last.next != null) {
            if (last.b.name.equals(name)) {
                prev.next = last.next;
                return last.b;
            }
            prev = last;
            last = last.next;
        }
        if (last.b.name.equals(name)) {
            prev.next = null;
            return last.b;
        }
        return null;
    }
}



