
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.*;
import java.lang.*;
import java.util.*;

public class PercolationStats {

   private double[] thresholds;
   private double mean, stddev, confidenceLo, confidenceHi;
   private int T;

   public PercolationStats(int n, int trials) {   // perform trials independent experiments on an n-by-n grid
       if (n<=0||trials<=0)
          throw new IllegalArgumentException("illegal argument, size shoule be greater than 0");
       T = trials;
       thresholds = new double[trials];
       for (int i=0; i<trials; i++) {
           Percolation perc = new Percolation(n);
           int count = 0, row, col, threshold;
           while (!perc.percolates()) {
               row = StdRandom.uniform(1, n+1);
               col = StdRandom.uniform(1, n+1);
               if (!perc.isOpen(row, col)) {
                   perc.open(row, col);
                   count += 1;
               }
           }
           thresholds[i] = (double) count / (n*n);
       }
       // calculate mean
       double sum = 0;
       for (double threshold : thresholds) {
           sum += threshold;
       }
       mean = sum / T;
       // calculate stddev
       double stdsum = 0;
       for (double threshold : thresholds) {
           stdsum += (threshold - mean)*(threshold - mean);
       }
       stddev = Math.sqrt(stdsum / (T-1));
       // calculate confidence interval
       confidenceLo = mean - 1.96 * stddev / Math.sqrt(T);
       confidenceHi= mean + 1.96 * stddev / Math.sqrt(T);
   }

   public double mean() {                         // sample mean of percolation threshold
       return mean;
   }

   public double stddev() {                       // sample standard deviation of percolation threshold
       return stddev;
   }

   public double confidenceLo() {                 // low  endpoint of 95% confidence interval
       return confidenceLo;
   }

   public double confidenceHi() {                 // high endpoint of 95% confidence interval
       return confidenceHi;
   }

   public static void main(String[] args) {       // test client (described below)
       PercolationStats s = new PercolationStats(200, 10);
       StdOut.println("mean is: " + s.mean);
       StdOut.println("stddev is: " + s.stddev);
       StdOut.println("CI is: [" + s.confidenceLo + ", " + s.confidenceHi + "]");
    //    StdOut.println();
   }

}
