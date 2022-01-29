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
        TBankNode last = add(b.name);
        last.isEnd = true;
        last.b = b;
    }

    TBankNode add(String key) {
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

    boolean search(Bank b) {
        return search(b.name) != null;
    }

    Bank search(String name) {
        int l = name.length();
        TBankNode it = this.root;
        for (int d = 0; d < l; d++) {
            int i = name.charAt(d) - 'a';
            if (it.children[i] == null)
                return null;
            it = it.children[i];
        }
        if (it.isEnd)
            return it.b;
        return null;
    }

    boolean delete(String name) {
        int l = name.length();
        TBankNode it = this.root;
        for (int d = 0; d < l; d++) {
            int i = name.charAt(d) - 'a';
            if (it.children[i] == null)
                return false;
            it = it.children[i];
        }

        // Now I've found the name and im in the letter of it :
        // I'ma go back this way and delete letters
        for (int d = 0; d < l; d++) {
            int i = name.charAt(d) - 'a';
            it.children[i] = null;
            it = it.children[i];
        }
        return true;

    }
}

class trieTest {
    public static void main(String[] args) {
        TrieTreeMainBanks ttmb = new TrieTreeMainBanks();
        ttmb.add(new Bank(new Coordinate(0, 0), "omid"));
        ttmb.add(new Bank(new Coordinate(1, 1), "reza"));
        ttmb.add(new Bank(new Coordinate(2, 2), "z"));
        System.out.println(ttmb.root.children['o' - 'a'].children['m' - 'a'].children['i' - 'a'].children['d' - 'a'].b.name);
        System.out.println(ttmb.search("omid").coordinate.y);
        ttmb.delete("omid");
        System.out.println(ttmb.root.children[14]);
    }
}
/*
*  static TrieNode remove(TrieNode root, String key, int depth)
    {
        // If tree is empty
        if (root == null)
            return null;

        // If last character of key is being processed
        if (depth == key.length()) {

            // This node is no more end of word after
            // removal of given key
            if (root.isEndOfWord)
                root.isEndOfWord = false;

            // If given is not prefix of any other word
            if (isEmpty(root)) {
                root = null;
            }

            return root;
        }

        // If not last character, recur for the child
        // obtained using ASCII value
        int index = key.charAt(depth) - 'a';
        root.children[index] =
            remove(root.children[index], key, depth + 1);

        // If root does not have any child (its only child got
        // deleted), and it is not end of another word.
        if (isEmpty(root) && root.isEndOfWord == false){
            root = null;
        }

        return root;
    }
    * */



