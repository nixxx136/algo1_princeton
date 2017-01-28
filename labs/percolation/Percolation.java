import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.*;
import java.lang.*;

public class Percolation {
   // private data fileds
   private WeightedQuickUnionUF wquf, wquf2;
   private boolean[] parent;
   private int size;
   private int open_count;

   public Percolation(int n) {               // create n-by-n grid, with all sites blocked
       // if n is equals or small to 0, throw an illegal argument exception
       if (n<=0)
          throw new IllegalArgumentException("illegal argument, size shoule be greater than 0");
       size = n;
       wquf = new WeightedQuickUnionUF(n*n+2); // 0..n*n+1 with virtual top and virtual bottom
       wquf2 = new WeightedQuickUnionUF(n*n+1); // 2nd uf to avoid backwash
       parent = new boolean[n*n];
   }

   public void open(int row, int col) {   // open site (row, col) if it is not open already
       if (row<=0 || row>size || col<=0 || col>size)
          throw new IndexOutOfBoundsException("row and col should between 1 and " + size);
       int index = xyTo1D(row, col);

       if (!parent[index]){
           parent[index] = true;
           open_count += 1;
       }

       if (row==1) wquf.union(0, index+1);
       if (row==1) wquf2.union(0, index+1);
       if (row==size) wquf.union(index+1, size*size+1);

       int connect1 = row-1>0 ? xyTo1D(row-1, col) : index;
       int connect2 = row+1<=size ? xyTo1D(row+1, col) : index;
       int connect3 = col-1>0 ? xyTo1D(row, col-1) : index;
       int connect4 = col+1<=size ? xyTo1D(row, col+1) : index;

       if (connect1 >= 0 && connect1 < size*size && parent[connect1]) {wquf.union(index+1, connect1+1);wquf2.union(index+1, connect1+1);}
       if (connect2 >= 0 && connect2 < size*size && parent[connect2]) {wquf.union(index+1, connect2+1);wquf2.union(index+1, connect2+1);}
       if (connect3 >= 0 && connect3 < size*size && parent[connect3]) {wquf.union(index+1, connect3+1);wquf2.union(index+1, connect3+1);}
       if (connect4 >= 0 && connect4 < size*size && parent[connect4]) {wquf.union(index+1, connect4+1);wquf2.union(index+1, connect4+1);}
   }

   public boolean isOpen(int row, int col) {  // is site (row, col) open?
       if (row<=0 || row>size || col<=0 || col>size)
          throw new IndexOutOfBoundsException("row and col should between 1 and " + size);
       return parent[xyTo1D(row, col)];
   }

   public boolean isFull(int row, int col) {  // is site (row, col) full?
       if (row<=0 || row>size || col<=0 || col>size)
          throw new IndexOutOfBoundsException("row and col should between 1 and " + size);
       // if it is connect with virtual top open site
       return wquf2.connected(0, xyTo1D(row, col)+1);
   }

   public int numberOfOpenSites() {      // number of open sites
       return open_count;
   }

   public boolean percolates() {              // does the system percolate?
       return wquf.connected(0, size*size+1);
   }

   // helper function
   private int xyTo1D(int x, int y) {
       return (x-1) * size + y - 1;
   }

   public static void main(String[] args) {   // test client (optional)
       StdOut.println("hello princeton");
       Percolation p = new Percolation(4);
       p.open(-2, 1);
   }
}
