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

    mainBank alter(String name, Branch br, String command) {
        int[] ins = getIndexes(name);
        return DB[ins[0]][ins[1]][ins[2]].alter(name, br, command);
    }

    Bank delete(String name) {
        int[] ins = getIndexes(name);
        return DB[ins[0]][ins[1]][ins[2]].delete(name);
    }

    void listAll() {
        for (int i = 0; i < 26; i++)
            for (int j = 0; j < 26; j++)
                for (int k = 0; k < 26; k++)
                    if (DB[i][j][k].size != 0)
                        DB[i][j][k].printAll();
    }

    mainBank searchEqBr(int x) {
        for (int i = 0; i < 26; i++)
            for (int j = 0; j < 26; j++)
                for (int k = 0; k < 26; k++)
                    if (DB[i][j][k].size != 0) {
                        Bank mb = DB[i][j][k].findEqBr(x);
                        if (mb != null)
                            return (mainBank) mb;
                    }
        return null;
    }
}

class bankLinkedList {
    Node head;
    int size;

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
        this.size = 0;
    }

    void printAll() {
        Node it = this.head;
        while (it != null) {
            System.out.println(City.ANSI_BLUE + it.b.name + "   x: " + it.b.coordinate.x + " ,y: " + it.b.coordinate.y + City.ANSI_RESET);
            it = it.next;
        }
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
        this.size++;
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

    mainBank alter(String name, Branch br, String command) {
        if (this.head == null)
            return null;
        Node last = this.head;
        while (last.next != null) {
            if (last.b.name.equals(name)) {
                if (command.equals("delete"))
                    last.b.branches.delete(br.coordinate);
                else if (command.equals("add"))
                    last.b.branches.add(br);
                return last.b;
            }
            last = last.next;
        }
        if (last.b.name.equals(name)) {
            if (command.equals("delete"))
                last.b.branches.delete(br.coordinate);
            else if (command.equals("add"))
                last.b.branches.add(br);
            return last.b;
        }
        return null;
    }

    Bank delete(String name) {
        if (this.head == null)
            return null;
        if (this.head.b.name.equals(name)) {
            Bank newB = this.head.b;
            this.head = this.head.next;
            this.size--;
            return newB;
        }
        bankLinkedList.Node last = this.head.next;
        bankLinkedList.Node prev = this.head;
        while (last.next != null) {
            if (last.b.name.equals(name)) {
                prev.next = last.next;
                this.size--;
                return last.b;
            }
            prev = last;
            last = last.next;
        }
        if (last.b.name.equals(name)) {
            prev.next = null;
            this.size--;
            return last.b;
        }
        return null;
    }

    Bank findEqBr(int x) {
        for (Node it = this.head; it != null; it = it.next)
            if (it.b.branches.size == x)
                return it.b;
        return null;
    }
}



