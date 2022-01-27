public class TrieTreeNeighbor {
    TNeighborNode root;

    TrieTreeNeighbor(){
        this.root=new TNeighborNode();
    }

    static class TNeighborNode {
        Neighborhood n;
        TNeighborNode[] children = new TNeighborNode[26];
        boolean isEnd;

        TNeighborNode() {
            isEnd = false;
            for (int i = 0; i < 26; i++)
                children[i] = null;
        }
    }

    void add(Neighborhood n) {
        TNeighborNode last = add(n, n.name);
        last.isEnd = true;
        last.n = n;
    }

    TNeighborNode add(Neighborhood n, String key) {
        int length = key.length();

        TNeighborNode iterator = this.root;

        for (int depth = 0; depth < length; depth++) {

            int index = key.charAt(depth) - 'a';
            if (iterator.children[index] == null)
                iterator.children[index] = new TNeighborNode();

            iterator = iterator.children[index];
        }
        return iterator;
    }
}

/*class main{
    public static void main(String[] args) {
        TrieTree tt=new TrieTree();
        tt.add(new Neighborhood("omid",0,0,0,0));
        tt.add(new Neighborhood("reza",1,1,1,1));
        tt.add(new Neighborhood("bc",2,2,2,2));
        System.out.println(tt.root.children[1].children[2].n.name);
    }
}*/
