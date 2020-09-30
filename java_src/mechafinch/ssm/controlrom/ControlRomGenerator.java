package mechafinch.ssm.controlrom;

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
 * Generates a Digital .hex file for the control rom from the ISA spec
 * 
 * @author Mechafinch
 */
public class ControlRomGenerator {
	
	private static boolean debug = true;
	
	public static void main(String[] args) throws IOException {
		// defaults yay
		int romSize = 4096,
			sequenceBits = 4;
		File inputFile = new File("isa.txt"),
			 outputFile = new File("control rom.hex");
		
		// deal with command line stuff
		// you either specify nothing, the files, or everything
		if(args.length == 2 || args.length == 4) { // files
			inputFile = new File(args[0]);
			outputFile = new File(args[1]);
		}
		
		if(args.length == 4) { // with size
			romSize = Integer.parseInt(args[2]);
			sequenceBits = Integer.parseInt(args[3]);
		}
		
		if(args.length != 0 && args.length != 2 && args.length != 4) { // bad
			printUsage("Invalid arguments");
		}
		
		// but not yet
		writeFile(generateROM(romSize, sequenceBits, inputFile), outputFile);
	}
	
	/**
	 * Prints command line usage
	 * 
	 * @param error
	 */
	private static void printUsage(String error) {
		System.err.println("\nError: " + error);
		System.err.println("Usage:" + 
						 "\nControlRomGenerator" +
						 "\nControlRomGenerator input output" +
						 "\nControlRomGenerator input output rom-size sequence-bits" +
						 "\n\tinput          The ISA file to read (default: isa.txt)" +
						 "\n\toutput         The hex file to write (default: control rom.hex)" +
						 "\n\trom-size       The number of addresses in the control ROM (default: 4096)" +
						 "\n\tsequence-bits  The number of bits used by the sequencer (default: 4)");
	}
	
	/**
	 * Writes the ROM to a file
	 * 
	 * @param rom The ROM object
	 * @param f The file to write to
	 * @throws IOException
	 */
	private static void writeFile(int[] rom, File f) throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f)));
		out.println("v2.0 raw");
		
		// Loop over all addresses
		for(int i = 1, currentWord = rom[0], blockLength = 1; i < rom.length; i++) {
			// Blocks of the same value are 1 line
			if(rom[i] == currentWord) {
				blockLength++;
			}
			
			// End of block reached, write it
			if(rom[i] != currentWord || (i == rom.length - 1 && rom[i] != 0)) {
				out.println((blockLength == 1 ? "" : (blockLength + "*")) + Integer.toHexString(currentWord));
				currentWord = rom[i];
				blockLength = 1;
			}
		}
		
		out.flush();
		out.close();
	}
	
	/**
	 * Generates the ROM itself, to be converted into the .hex file
	 * 
	 * @param romSize The number of addresses in the ROM
	 * @param sequenceBits The number of lower bits used to sequence instructions
	 * @param f The file to read from
	 * @return The generated ROM
	 * @throws IOException
	 */
	private static int[] generateROM(int romSize, int sequenceBits, File f) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(f));
		int[] rom = new int[romSize];
		
		int index = -1;	// ROM index
		String line;	// current line
		
		// Loop over lines
		while((line = in.readLine()) != null) {
			// We only care about lines starting with control characters
			if(line.startsWith(">")) {
				if(debug) System.out.println(line);
				
				// Start an instruction, use the first hex digits we get
				Matcher matcher = Pattern.compile("[0-9a-fA-F]+").matcher(line);
				matcher.find();
				index = Integer.parseUnsignedInt(matcher.group(), 16) << sequenceBits;
				
				if(debug) System.out.println("Starting instruction at " + Integer.toHexString(index));
			} else if(line.startsWith("~")) {
				// Parse 32 bit control word
				line = line.replaceAll("[^01]", "");
				int word = 0;
				
				if(debug) System.out.println("Constructing " + line);
				
				// convenient shift-add thing
				for(char c : line.toCharArray()) {
					word = (word << 1) + (c - '0');
				}
				
				rom[index++] = word;
				if(debug) System.out.println("Constructed control word " + Integer.toHexString(word));
			}
		}
		
		in.close();
		return rom;
	} 
}
