package Converter;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class DFA {
	String[] Q; 				// set of states
	String[] Sigma; 			// set of input symbol
	ArrayList<String[]> delta; 	// transfer function
	String q0; 					// start state
	String F[]; 				// finish state

	Main m;

	DFA(Main m) {
		this.m = m;
		this.delta = new ArrayList<String[]>();
	}

	public void writeText(String url) throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(url);
		System.out.println("   < DFA >");
		System.out.print("Q       ");
		for (int i = 0; i < this.Q.length; i++) {
			System.out.print(this.Q[i] + " ");
			pw.print(this.Q[i] + " ");
		}
		System.out.println();
		pw.println();
		System.out.print("Sigma   ");
		for (int i = 0; i < this.Sigma.length; i++)
			if (this.Sigma[i] != null) {
				System.out.print(this.Sigma[i] + " ");
				pw.print(this.Sigma[i] + " ");
			}
		System.out.println();
		pw.println();
		System.out.println("q0      " + this.q0);
		pw.println(this.q0);
		System.out.print("F       ");
		for (int i = 0; i < this.F.length; i++) {
			System.out.print(this.F[i] + " ");
			pw.print(this.F[i] + " ");
		}
		System.out.println();
		pw.println();
		System.out.println("___delta___");
		for (int i = 0; i < this.delta.size(); i++) {
			for (int j = 0; j < this.delta.get(i).length; j++) {
				System.out.print(" " + this.delta.get(i)[j] + "  ");
				pw.print(this.delta.get(i)[j] + " ");
			}
			System.out.println();
			pw.println();
		}
		pw.close();
	}
}
