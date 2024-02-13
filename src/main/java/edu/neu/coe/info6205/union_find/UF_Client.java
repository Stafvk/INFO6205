package edu.neu.coe.info6205.union_find;
import java.util.Random;

public class UF_Client {
        public static int count(int n) {
            UF_HWQUPC uf = new UF_HWQUPC(n);
            Random random=new Random();

            int connections = 0;

            while (uf.components() > 1) {
                int p=random.nextInt(n);

                int q = random.nextInt(n);

                if (!uf.connected(p, q)) {
                      uf.union(p, q);

                    connections++;
                }
            }

            return connections;
        }

        public static void main(String[] args) {

            int n = Integer.parseInt(args[0]);

   int connections = count(n);

            System.out.println("Number of connections made: " + connections);
        }
    }

