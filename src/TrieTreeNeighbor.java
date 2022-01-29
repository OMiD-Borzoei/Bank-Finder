public class TrieTreeNeighbor {
    TNeighborNode root;

    TrieTreeNeighbor() {
        this.root = new TNeighborNode();
    }

    static class TNeighborNode {
        Neighborhood n;
        TNeighborNode[] children = new TNeighborNode[27];
        boolean isEnd;

        TNeighborNode() {
            isEnd = false;
            for (int i = 0; i < 27; i++)
                children[i] = null;
        }
    }

    boolean add(Neighborhood n) {
        TNeighborNode last = add(n.name);
        if (last.isEnd)
            return false;
        last.isEnd = true;
        last.n = n;
        return true;
    }

    TNeighborNode add(String key) {
        int length = key.length();

        TNeighborNode iterator = this.root;

        for (int depth = 0; depth < length; depth++) {
            char c = key.charAt(depth);
            int index = c - 'a';
            if (c == ' ')
                index = 26;
            if (iterator.children[index] == null)
                iterator.children[index] = new TNeighborNode();

            iterator = iterator.children[index];
        }
        return iterator;
    }

    Neighborhood search(String name) {
        int l = name.length();
        TNeighborNode it = this.root;
        for (int d = 0; d < l; d++) {
            char c = name.charAt(d);
            int i = c - 'a';
            if (c == ' ')
                i = 26;
            if (it.children[i] == null)
                return null;
            it = it.children[i];
        }
        if (it.isEnd)
            return it.n;
        return null;
    }

    boolean isEmpty(TNeighborNode t) {
        for (int i = 0; i < 27; i++)
            if (t.children[i] != null)
                return true;
        return false;
    }

    boolean delete(String name) {
        return deleteRec(this.root, name, 0)!=null;
    }

    TNeighborNode deleteRec(TNeighborNode root, String key, int depth) {
        if (root == null)
            return null;

        if (depth == key.length()) {
            if (root.isEnd) {
                System.out.println(root.n.name);
                root.isEnd = false;
                root.n = null;
            }

            if (isEmpty(root))
                root = null;

            return root;

        }
        char c = key.charAt(depth);
        int index = c - 'a';
        if (c == ' ')
            index = 26;
        root.children[index] = deleteRec(root.children[index], key, depth + 1);
        if (isEmpty(root) && !root.isEnd)
            root.n = null;

        return root;
    }


}

class main {
    public static void main(String[] args) {
        TrieTreeNeighbor tt = new TrieTreeNeighbor();
        tt.add(new Neighborhood("omid reza    d", 0, 0, 0, 0));
        tt.add(new Neighborhood("omid reza    s", 1, 3.5, 1, 1));
        tt.add(new Neighborhood("omid reza    x", 1, 4, 1, 1));
        tt.add(new Neighborhood("bc", 2, 2, 2, 2));
        //System.out.println(tt.root.children[1].children[2].n.name);
        //System.out.println(tt.search("omid reza    s").downEdge);
        //System.out.println(tt.search("omid reza    d").upEdge);
        System.out.println(tt.delete( "omid reza    d"));
        System.out.println(tt.search("omid reza    s").downEdge);
        System.out.println(tt.search("omid reza    x").downEdge);
        //System.out.println(tt.search("omid reza    d").upEdge);
    }
}
