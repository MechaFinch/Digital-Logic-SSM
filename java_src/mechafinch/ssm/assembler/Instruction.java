package mechafinch.ssm.assembler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents an opcode
 * 
 * @author Mechafinch
 */
public class Instruction {
	
	public byte opcode;
	
	public String[] mnemonics;
	
	public ArrayList<ArgumentType> arguments; // Descriptors for any arguments this takes
	
	/**
	 * Creates an instruction descriptor with a single mnemonic
	 * 
	 * @param opcode
	 * @param mnemonic
	 * @param arguments
	 */
	public Instruction(byte opcode, String mnemonic, List<ArgumentType> arguments) {
		this.opcode = opcode;
		this.mnemonics = new String[] {mnemonic.toUpperCase()};
		this.arguments = new ArrayList<>(arguments);
	}
	
	/**
	 * Creates an instruction descriptor with multiple mnemonics, the first mnemonic is the primary one
	 * 
	 * @param opcode
	 * @param mnemonics
	 * @param arguments
	 */
	public Instruction(byte opcode, String[] mnemonics, List<ArgumentType> arguments) {
		this.opcode = opcode;
		this.mnemonics = Arrays.stream(mnemonics).map(s -> s.toUpperCase()).collect(Collectors.toList()).toArray(new String[0]);
		this.arguments = new ArrayList<>(arguments);
	}
	
	/**
	 * Determines if the given mnemonic and argument combination matches this instruction
	 * 
	 * @param mnemonic The mnemonic
	 * @param argCount Number of bytes of arguments
1	 * @return Whether the instruction matches
	 */
	public boolean matches(String mnemonic, int argLength) {
		return Arrays.asList(mnemonics).contains(mnemonic.toUpperCase()) && argLength == (length() - 1);
	}
	
	/**
	 * Determines if the given mnemonic and argument combination matches this instruction
	 * 
	 * @param mnemonic The mnemonic
	 * @param args Argument descriptors
	 * @return Whether the instruction matches
	 */
	public boolean matches(String mnemonic, List<ArgumentType> args) {
		return Arrays.asList(mnemonics).contains(mnemonic.toUpperCase()) && arguments.equals(args); 
	}
	
	/**
	 * Returns the length in bytes
	 * 
	 * @return Length of the instruction in bytes
	 */
	public int length() {
		return arguments.stream().mapToInt(ArgumentType::getLength).reduce(0, Integer::sum) + 1;
	}
	
	public String toString() {
		String argstring = "";
		
		for(ArgumentType a : arguments) {
			argstring += ", " + a.toString().toLowerCase();
		}
		
		// ex. 2C: ASTORE
		// ex. <opcode>: CALLV word, byte
		// i haven't chosen the opcode for CALLV as of writing but its the only one so far with 2 args
		return String.format("%8S", Integer.toHexString(opcode)).substring(6, 8).replace(' ', '0') + ": " + mnemonics[0] + (argstring.length() != 0 ? argstring.substring(1) : "");
	}
}
