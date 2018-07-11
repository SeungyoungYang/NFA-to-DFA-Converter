package Converter;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;

public class NFA {
	String[] Q;					// set of states
	String[] Sigma; 			// set of input symbol
	ArrayList<String[]> delta; 	// transfer function
	String q0; 					// start state
	String[] F; 				// finish state

	Main m;

	NFA(Main m) {
		this.m = m;
		this.delta = new ArrayList<String[]>();
	}

	public void readText(String url) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(url), "euc-kr"));
		String line;

		line = br.readLine();
		this.Q = line.split(" ");
		line = br.readLine();
		this.Sigma = line.split(" ");
		this.q0 = br.readLine();
		line = br.readLine();
		this.F = line.split(" ");

		while (true) {
			line = br.readLine();
			if (line == null)
				break;
			String[] d;
			d = line.split(" ");
			this.delta.add(d);
		}
		br.close();
	}

	public void toDFA() {
		boolean ec = false;
		int num_state = 0, num_f = 0;
		for (int i = 0; i < this.Sigma.length; i++) {
			if (this.Sigma[i].equals("e"))
				ec = true;
		}
		ArrayList<String[]> ddelta = new ArrayList<String[]>();
		if (ec) { // e-NFA to DFA
			ArrayList<String[]> eclos = new ArrayList<String[]>();
			for (int i = 0; i < this.Q.length; i++) {
				String[] str = new String[2];
				str[0] = this.Q[i];
				str[1] = this.Q[i];
				eclos.add(str);
			}
			for (int i = 0; i < eclos.size(); i++) { // e-closure
				for (int j = 0; j < eclos.get(i)[1].length(); j++) {
					for (int k = 0; k < this.delta.size(); k++) {
						if (this.delta.get(k)[1].equals("e"))
							if (eclos.get(i)[1].contains(this.delta.get(k)[0])
									&& !eclos.get(i)[1].contains(this.delta.get(k)[2]))
								eclos.get(i)[1] += this.delta.get(k)[2];
					}
				}
			}
			System.out.println("< e-closure >");
			for (int i = 0; i < eclos.size(); i++) // e-closure list
				System.out.println("e-closure(" + eclos.get(i)[0] + ") " + eclos.get(i)[1]);
			System.out.println();

			for (int i = 0; i < eclos.size(); i++) { // 시작상태에 e-closure 추가
				if (eclos.get(i)[0].equals(this.q0)) {
					for (int j = 0; j < this.Sigma.length; j++) {
						if (!this.Sigma[j].equals("e")) {
							String[] str = new String[3];
							str[0] = eclos.get(i)[1];
							str[1] = this.Sigma[j];
							str[2] = "";
							ddelta.add(str);
							for(int k=0;k<this.F.length;k++)
								if(num_f==0 && eclos.get(i)[1].contains(this.F[k])) { // 시작상태가 종결상태인 경우
									num_f++;
							}
						}
					}
					num_state++;
				}
			}

			for (int i = 0; i < ddelta.size(); i++) { // 새로운 상태조합이 발생하면 추가
				for (int j = 0; j < this.delta.size(); j++) {
					if (ddelta.get(i)[0].contains(this.delta.get(j)[0]) && ddelta.get(i)[1].equals(this.delta.get(j)[1])
							&& !ddelta.get(i)[2].contains(this.delta.get(j)[2])) {
						ddelta.get(i)[2] += this.delta.get(j)[2];
					}
				}
				if (ddelta.get(i)[2].equals(""))
					continue;
				for (int j = 0; j < ddelta.get(i)[2].length(); j++) { // 도착상태에 e-closure 추가
					for (int k = 0; k < eclos.size(); k++) {
						if (ddelta.get(i)[2].contains(eclos.get(k)[0])) {
							for (int l = 0; l < eclos.get(k)[1].length(); l++) {
								if (ddelta.get(i)[2].indexOf(eclos.get(k)[1].charAt(l)) == -1) {
									ddelta.get(i)[2] += eclos.get(k)[1].charAt(l);
								}
							}
						}
					}
				}
				add1: for (int k = 0; k < ddelta.size(); k++) { // 새로운 상태 조합인 경우 dfa-delta에 추가
					char[] arr1 = new char[ddelta.get(k)[0].length()];
					char[] arr2 = new char[ddelta.get(i)[2].length()];
					arr1 = ddelta.get(k)[0].toCharArray();
					arr2 = ddelta.get(i)[2].toCharArray();
					Arrays.sort(arr1);
					Arrays.sort(arr2);
					if (arr1.length == arr2.length) {
						for (int a = 0; a < arr1.length; a++) {
							if (arr1[a] != arr2[a])
								break;
							else if (arr1[a] == arr2[a] && a + 1 == arr1.length) { // 이미 존재하는 상태조합인 경우
								break add1;
							}
						}
					}

					if (k == ddelta.size() - 1) { // 새로운 상태조합인 경우
						for (int s = 0; s < this.Sigma.length; s++) {
							if (!this.Sigma[s].equals("e")) {
								String[] str = new String[3];
								str[0] = ddelta.get(i)[2];
								str[1] = this.Sigma[s];
								str[2] = "";
								ddelta.add(str);
							}
						}
						for (int s = 0; s < this.F.length; s++)
							if (ddelta.get(i)[2].contains(this.F[s]))
								num_f++;
						num_state++;
						break;
					}
				}
			}
		}
		// ----------------------------------------------------------------------------------------------------------------------//
		else if (!ec) { // NFA to DFA
			for (int i = 0; i < this.delta.size(); i++) { // dfa-delta에 시작상태 추가
				if (this.delta.get(i)[0].equals(this.q0)) {
					int check = 0;
					for (int j = 0; j < ddelta.size(); j++)
						if (ddelta.get(j)[0].equals(this.delta.get(i)[0])
								&& ddelta.get(j)[1].equals(this.delta.get(i)[1])) { // dfa-delta의 시작상태 중 겹치는게 있는 경우
							ddelta.get(j)[2] += this.delta.get(i)[2];
							check = 1;
							break;
						}
					if (check == 0) {
						String[] str = new String[3];
						str[0] = this.delta.get(i)[0];
						str[1] = this.delta.get(i)[1];
						str[2] = "";
						ddelta.add(str);
					}
					for(int k=0;k<this.F.length;k++)
						if(num_f==0 && this.delta.get(i)[0].contains(this.F[k])) { // 시작상태가 종결상태인 경우
							num_f++;
					}
				}
			}
			num_state++;

			for (int i = 0; i < ddelta.size(); i++) { // 새로운 상태조합이 발생하면 추가
				for (int j = 0; j < this.delta.size(); j++) {
					if (ddelta.get(i)[0].contains(this.delta.get(j)[0]) && ddelta.get(i)[1].equals(this.delta.get(j)[1])
							&& !ddelta.get(i)[2].contains(this.delta.get(j)[2]))
						ddelta.get(i)[2] += this.delta.get(j)[2];
				}
				if (ddelta.get(i)[2].equals(""))
					continue;
				add2: for (int k = 0; k < ddelta.size(); k++) { // 새로운 상태 조합인 경우 dfa-delta에 추가
					char[] arr1 = new char[ddelta.get(k)[0].length()];
					char[] arr2 = new char[ddelta.get(i)[2].length()];
					arr1 = ddelta.get(k)[0].toCharArray();
					arr2 = ddelta.get(i)[2].toCharArray();
					Arrays.sort(arr1);
					Arrays.sort(arr2);
					if (arr1.length == arr2.length) {
						for (int a = 0; a < arr1.length; a++) {
							if (arr1[a] != arr2[a])
								break;
							else if (arr1[a] == arr2[a] && a + 1 == arr1.length) { // 이미 존재하는 상태조합인 경우
								break add2;
							}
						}
					}

					if (k == ddelta.size() - 1) { // 새로운 상태조합인 경우
						for (int s = 0; s < this.Sigma.length; s++) {
							String[] str = new String[3];
							str[0] = ddelta.get(i)[2];
							str[1] = this.Sigma[s];
							str[2] = "";
							ddelta.add(str);
						}
						for (int s = 0; s < this.F.length; s++)
							if (ddelta.get(i)[2].contains(this.F[s]))
								num_f++;
						num_state++;
						break;
					}
				}
			}
		}
		
		int ff=0;
		this.m.dfa.F = new String[num_f];
		for(int i=0;i<this.F.length;i++)
			if(ddelta.get(0)[0].contains(this.F[i])) { // 시작상태가 종결상태인 경우
				char[] arr = new char[ddelta.get(0)[0].length()];
				arr = ddelta.get(0)[0].toCharArray();
				Arrays.sort(arr);
				this.m.dfa.F[ff++]=new String(arr,0,arr.length);
				break;
			}
				
		for (int i = 0; i < ddelta.size(); i++) { // dfa F
			for (int j = 0; j < this.F.length; j++) {
				if (ddelta.get(i)[2].contains(this.F[j]) && ff<num_f) {
					char[] arr2 = new char[ddelta.get(i)[2].length()];
					arr2 = ddelta.get(i)[2].toCharArray();
					Arrays.sort(arr2);
					int k;
					for (k = 0; k < num_f; k++) {
						this.m.dfa.F[ff] = "";
						if(ff>0 && ff>k) {
							if(this.m.dfa.F[k].equals(arr2.toString()))
								break;
						}
						else {
							this.m.dfa.F[ff++]= new String(arr2, 0, arr2.length);
							break;
						}
						if(k==num_f-1)
							this.m.dfa.F[ff++]= new String(arr2, 0, arr2.length);
					}
				}
			}
		}

		for (int i = 0; i < ddelta.size(); i++) { // 문자열 오름차순 정렬
			char[] arr1 = new char[ddelta.get(i)[0].length()];
			char[] arr2 = new char[ddelta.get(i)[2].length()];
			arr1 = ddelta.get(i)[0].toCharArray();
			arr2 = ddelta.get(i)[2].toCharArray();
			Arrays.sort(arr1);
			Arrays.sort(arr2);
			String str1 = "";
			String str2 = "";
			str1 = new String(arr1, 0, arr1.length);
			str2 = new String(arr2, 0, arr2.length);
			ddelta.get(i)[0] = str1;
			ddelta.get(i)[2] = str2;
		}

		HashMap<String, String> map = new HashMap<String, String>();
		this.m.dfa.Q = new String[num_state];
		for (int i = 0; i < num_state; i++) { // dfa에 상태추가
			char ch = (char) (i + 65);
			String st = "";
			st += ch;
			this.m.dfa.Q[i] = "";
			this.m.dfa.Q[i] += st;
		}

		int q = 0;
		for (int i = 0; i < ddelta.size(); i++) { // dfa Q
			if (!map.containsKey(ddelta.get(i)[0]))
				map.put(ddelta.get(i)[0], this.m.dfa.Q[q++]);
		}
		this.m.dfa.Sigma = new String[this.Sigma.length];
		int ss = 0;
		for (int i = 0; i < ddelta.size(); i++) { // dfa Sigma
			if(ss>0) {
				int k;
				for(k=0;k<ss;k++) {
					if(this.m.dfa.Sigma[k].equals(ddelta.get(i)[1]))
						break;
				}
				if(k==ss)
					this.m.dfa.Sigma[ss++] = new String(ddelta.get(i)[1].toCharArray(),0,ddelta.get(i)[1].length());
			}
			else
				this.m.dfa.Sigma[ss++] = new String(ddelta.get(i)[1].toCharArray(),0,ddelta.get(i)[1].length());
		}
		this.m.dfa.q0 = map.get(ddelta.get(0)[0]); // dfa q0
		for (int i = 0; i < ddelta.size(); i++) { // 도착상태가 공백인 경우 제거
			if (ddelta.get(i)[2].equals(""))
				ddelta.remove(i--);
		}
		for(int i=0; i<num_f; i++) { // dfa F 새로운 상태 이름으로 교체
			this.m.dfa.F[i] = map.get(this.m.dfa.F[i]);
		}
		for (int i = 0; i < ddelta.size(); i++) { // dfa delta
			String[] str = new String[3];
			str[0] = "";
			str[0] += map.get(ddelta.get(i)[0]);
			str[1] = "";
			str[1] += ddelta.get(i)[1];
			str[2] = "";
			str[2] += map.get(ddelta.get(i)[2]);
			this.m.dfa.delta.add(str);
		}
		System.out.println("< DFA delta table >");
		for (int i = 0; i < ddelta.size(); i++) // dfa-delta list
			System.out.println(ddelta.get(i)[0] + " " + ddelta.get(i)[1] + " " + ddelta.get(i)[2]);
		System.out.println();
	}
}
