public class TrieTreeMainBanks {
    TBankNode root;

    TrieTreeMainBanks() {
        this.root = new TBankNode();
    }

    static class TBankNode {
        Bank b;
        TBankNode[] children = new TBankNode[26];
        boolean isEnd;

        TBankNode() {
            isEnd = false;
            for (int i = 0; i < 26; i++)
                children[i] = null;
        }
    }

    void add(Bank b) {
        TBankNode last = add(b, b.name);
        last.isEnd = true;
        last.b = b;
    }

    TBankNode add(Bank b, String key) {
        int length = key.length();

        TBankNode iterator = this.root;

        for (int depth = 0; depth < length; depth++) {

            int index = key.charAt(depth) - 'a';
            if (iterator.children[index] == null)
                iterator.children[index] = new TBankNode();

            iterator = iterator.children[index];
        }
        return iterator;
    }
}
/*
class idk {
    public static void main(String[] args) {
        TrieTreeMainBanks ttmb = new TrieTreeMainBanks();
        ttmb.add(new Bank(new Coordinate(0, 0), "omid"));
        ttmb.add(new Bank(new Coordinate(1, 1), "reza"));
        ttmb.add(new Bank(new Coordinate(2, 2), "z"));
        System.out.println(ttmb.root.children[25].b.coordinate.x);
    }
}
*/


