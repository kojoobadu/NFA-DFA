import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class NfaToDfa {
	// Location of input file
	public static String location = "C:\\Users\\Kojo\\Documents\\Harrisburg U\\CISC610\\nfa-page873.csv";
	// List of lists to store truth table for NFA
	public static List<List<String>> nfa = new ArrayList<>();
	// List of lists to store truth table for DFA
	public static List<List<String>> dfa = new ArrayList<>();
	
	// Main method
	public static void main(String args[]) throws FileNotFoundException, IOException {
		getNFA();
		nfa_dfa();
	}
	
	// Method to extract truth table from csv and store in nfa variable
	public static List<List<String>> getNFA() throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(location))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        String[] values = line.split(",");
		        nfa.add(Arrays.asList(values));
		    }
		}
		return nfa;
	}
	
	// Method to sort values in a list
	public static List<String> sort(List<String> list){
		Collections.sort(list, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return extractInt(o1) - extractInt(o2);
			}
			int extractInt(String s) {
				String num = s.replaceAll("\\D", "");
				return num.isEmpty() ? 0 : Integer.parseInt(num);
			}
		});
		return list;
	}
	
	// Method to parse a string
	public static String insertPeriodically(String text, String insert, int period){
		StringBuilder builder = new StringBuilder(text.length() + insert.length() * (text.length()/period)+1);
		    int index = 0;
		    String prefix = "";
		    while (index < text.length()){
		        builder.append(prefix);
		        prefix = insert;
		        builder.append(text.substring(index, 
		            Math.min(index + period, text.length())));
		        index += period;
		    }
		    return builder.toString();
	}
	
	// Method to convert a string to a list
	public static List<String> listify(String value){
		List<String> list = new ArrayList<>();
		value = insertPeriodically(value, ",", 2);
		String[] array = value.split(",");
		list = Arrays.asList(array);
		return list;
	}
	
	// Function to convert an NFA to a DFA
	public static void nfa_dfa() {
		int dfaCounter = 0;
		String f0 = "";
		String f1 = "";
		List<String> function = new ArrayList<>();
		List<String> currentRow = new ArrayList<>();
		List<String> read = new ArrayList<>();
		List<String> current = new ArrayList<>();
		
		//Base case
		dfa.add(nfa.get(0));
		dfa.add(nfa.get(1));
		String currentCell = nfa.get(1).get(0);
		read.add(currentCell);
		dfaCounter += 2;
	
		for(int i = 1; i <= (dfaCounter - read.size()); i++) {  
			currentRow = dfa.get(i);
			System.out.println("Current row is : "+currentRow);
			for(int j = 0; j < 3; j++) {   
				currentCell = currentRow.get(j);  
				if(read.contains(currentCell) != true) {  
					if(currentCell.length() > 2) {      
						System.out.println("DFA to start is : "+dfa);
						System.out.println("Current cell is : "+currentCell);
						read.add(currentCell);
						dfaCounter++;
						current = listify(currentCell);
						for(int k = 0; k < current.size(); k++) {
							for(int l = 1; l < 5; l++) {
								if(nfa.get(l).get(0).equals(current.get(k))) {
									f0 += nfa.get(l).get(1);
									f1 += nfa.get(l).get(2);
								}
							}
						}
						function = listify(f0);
						function = sort(function);
						function = function.stream().distinct().collect(Collectors.toList());
						f0 = String.join("",function);
						function.clear();
						function = listify(f1);
						function = sort(function);
						function = function.stream().distinct().collect(Collectors.toList());
						f1 = String.join("", function);
						function.clear();
						dfa.add(List.of(currentCell,f0,f1));
						dfaCounter++;
						System.out.println("DFA after add :"+dfa);
						System.out.println("Read states are : "+read);
						f0 = "";
						f1 = "";
						System.out.println();
					}
					else if(currentCell.length() == 2) {
						System.out.println("Current cell is : "+currentCell);
						read.add(currentCell);
						dfaCounter++;
						for(int l = 1; l < 5; l++) {
							if(nfa.get(l).get(0).equals(currentCell)) {
								f0 += nfa.get(l).get(1);
								f1 += nfa.get(l).get(2);
							}
						}
						function = listify(f0);
						function = sort(function);
						function = function.stream().distinct().collect(Collectors.toList());
						f0 = String.join("",function);
						function.clear();
						function = listify(f1);
						function = sort(function);
						function = function.stream().distinct().collect(Collectors.toList());
						f1 = String.join("", function);
						function.clear();
						dfa.add(List.of(currentCell,f0,f1));
						dfaCounter++;
						System.out.println("DFA after add :"+dfa);
						System.out.println("Read states are : "+read);
						f0 = "";
						f1 = "";
						System.out.println();
					}
					else {
						System.out.println("Empty");
					}
				}
			}
		}
	}
		
}
