package mechafinch.ssm.generators;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Generates a condensed reference for the ISA file
 * 
 * @author Mechafinch
 */
public class ISAReferenceGenerator {
	
	private static boolean debug = true;
	
	public static void main(String[] args) throws IOException {
		// defaults
		File inputFile = new File("isa.txt"),
			 outputFile = new File("reference.txt");
		
		// command line args
		if(args.length == 2) { // Files specified
			inputFile = new File(args[0]);
			outputFile = new File(args[1]);
		} else if(args.length != 0) {
			printUsage("Invalid arguments");
			System.exit(0);
		}
		
		condenseISA(inputFile, outputFile);
	}
	
	/**
	 * Prints command line usage
	 * 
	 * @param error
	 */
	private static void printUsage(String error) {
		System.err.println("\nError: " + error);
		System.err.println("Usage:" +
						 "\nISAReferenceGenerator" +
						 "\nISAReferenceGenerator input output" +
						 "\n\tinput   The ISA file to  read (default: isa.txt)" +
						 "\n\toutput  The reference file to write (default: reference.txt)");
	}
	
	// I wrote this out before the idea to generate it and copy pasted it
	// so like the format it scuffed but w/e
	private static String header = "\r\n" + 
			"This is a reference for the ISA that only includes the necessary details\r\n" + 
			"in a compact form. Since the main ISA includes all possible information\r\n" + 
			"(like microcode) it's annoying to search sometimes.\r\n" + 
			"\r\n" + 
			"<opcode> MNEMONIC arguments\r\n" + 
			"<STACK EFFECTS>\r\n" + 
			"\r\n" + 
			"This file is generated from the ISA.\r\n" +
			"\r\n" +
			"Total Opcodes: ";
	
	/**
	 * Condenses the ISA into a reference
	 * 
	 * @param ipf input file
	 * @param opf output file
	 * @throws IOException
	 */
	private static void condenseISA(File ipf, File opf) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(ipf));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(opf)));
		
		// Condense
		String line = "",
			   mnemonic = "",
			   opcode = "",
			   stackBefore = "",
			   stackAfter = "",
			   output = "";
		int opcodeCount = 0;
		
		while((line = in.readLine()) != null) {
			// Control characters only
			if(line.startsWith(".")) {
				mnemonic = line.trim().substring(1);
				
				if(debug) System.out.println(mnemonic);
			} else if(line.startsWith(">")) {
				// Isolate opcode
				Matcher matcher = Pattern.compile("[0-9a-fA-F]+").matcher(line);
				matcher.find();
				opcode = matcher.group();
				
				if(debug) System.out.println(opcode);
			} else if(line.trim().startsWith("...") || line.trim().toLowerCase().equals("no change")) {
				String s = line.trim();
				
				// First or second stack line
				if(stackBefore.equals("")) {
					stackBefore = s;
					if(debug) System.out.println(stackBefore);
				} else {
					stackAfter = s;
					if(debug) System.out.println(stackAfter);
				}
			} else if((line.startsWith("~") || line.trim().startsWith("-")) && !mnemonic.equals("")) {
				// End of the relevant information for the instruction, write it
				output += opcode + " " + mnemonic + "\r\n";
				if(!stackBefore.equals("")) output += " " + stackBefore + "\r\n";
				if(!stackAfter.equals("")) output += " " + stackAfter + "\r\n";
				output += "\r\n";
				
				// Clear values cause we don't have them anymore
				opcode = "";
				mnemonic = "";
				stackBefore = "";
				stackAfter = "";
				
				opcodeCount++;
			}
		}
		
		// Print header and file
		out.println(header + opcodeCount + "\r\n\r\n");
		out.println(output);
		
		in.close();
		out.flush();
		out.close();
	}
}
