package mechafinch.ssm.assembler;

import java.util.Arrays;

/**
 * Represents an opcode
 * 
 * @author Mechafinch
 */
public class Instruction {
	
	public byte opcode;
	
	public String mnemonic;
	
	public ArgumentType[] arguments; // Descriptors for any arguments this takes
	
	public Instruction(byte opcode, String mnemonic, ArgumentType[] arguments) {
		this.opcode = opcode;
		this.mnemonic = mnemonic;
		this.arguments = arguments;
	}
	
	/**
	 * Returns the length in bytes
	 * 
	 * @return Length of the instruction in bytes
	 */
	public int length() {
		return Arrays.stream(arguments).mapToInt(ArgumentType::getLength).reduce(0, Integer::sum);
	}
	
	public String toString() {
		String argstring = "";
		
		for(ArgumentType a : arguments) {
			argstring += ", " + a.toString();
		}
		
		// ex. 2C: ASTORE
		// ex. <opcode>: CALLV word, byte
		// i haven't chosen the opcode for CALLV as of writing but its the only one so far with 2 args
		return String.format("%2s", Integer.toHexString(opcode)).toUpperCase().replace(' ', '0') + ": " + mnemonic + argstring.substring(1);
	}
}
