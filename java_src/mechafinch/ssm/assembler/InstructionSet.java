package mechafinch.ssm.assembler;

import java.io.File;
import java.util.HashSet;
import java.util.List;

/**
 * Generates a description of the opcodes for the assembler
 * 
 * @author Mechafinch
 */
public class InstructionSet {
	
	//private static boolean debug = true;
	
	private HashSet<Instruction> instructions;
	
	/**
	 * Creates an empty instruction set
	 */
	public InstructionSet()  {
		instructions = new HashSet<Instruction>();
	}
	
	public InstructionSet(HashSet<Instruction> instructions) {
		this.instructions = instructions;
	}
	
	/**
	 * Generates an instruction set from a file
	 * 
	 * @param f the ISA file
	 */
	public InstructionSet(File f) {
		this();
		generate(f);
	}
	
	/**
	 * Generates the instruction set from the ISA file
	 * 
	 * @param f The ISA file
	 */
	private void generate(File f) {
		
	}
	
	/**
	 * Assembles an instruction into bytes
	 * 
	 * @param mnemonic The mnemonic
	 * @param argDescriptors Descriptions of the arguments
	 * @param args The arguments
	 * @return The assembled instruction
	 */
	public byte[] assemble(String mnemonic, List<ArgumentType> argDescriptors, int[] arguments) {
		// Quick sanitization
		if(argDescriptors.size() != arguments.length) throw new IllegalArgumentException("Arguments and descriptors do not match"); 
		
		Instruction inst = null;
		
		// Find correct instruction descriptor
		for(Instruction i : instructions) {
			if(i.matches(mnemonic, argDescriptors)) {
				inst = i;
				break;
			}
		}
		
		// Nothing found
		if(inst == null) throw new IllegalArgumentException("Does not match any instruction");
		
		// Assemble it
		byte[] bytes = new byte[inst.length()];
		
		bytes[0] = inst.opcode;
		
		for(int i = 0, b = 1; i < arguments.length; i++) {
			bytes[b++] = (byte) arguments[i];
			
			if(argDescriptors.get(i).getLength() == 2) { // word
				// technically this can be either kind of right shift but its nice to be clear
				bytes[b++] = (byte) (arguments[i] >>> 8);
			}
		}
		
		return bytes;
	}
}




