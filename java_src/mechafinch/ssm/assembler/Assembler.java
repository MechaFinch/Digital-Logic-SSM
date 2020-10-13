package mechafinch.ssm.assembler;

import java.util.Arrays;
import java.util.HashSet;

public class Assembler {
	
	public static void main(String[] args) {
		HashSet<Instruction> set = new HashSet<>();
		
		set.add(new Instruction((byte) 0xFC, "WRITE", Arrays.asList(new ArgumentType[] {})));
		set.add(new Instruction((byte) 0xFD, "WRITEI", Arrays.asList(new ArgumentType[] {ArgumentType.BYTE})));
		set.add(new Instruction((byte) 0xFE, "WRITEW", Arrays.asList(new ArgumentType[] {})));
		set.add(new Instruction((byte) 0xFF, "WRITEIW", Arrays.asList(new ArgumentType[] {ArgumentType.WORD})));
		set.add(new Instruction((byte) 0x0C, "asdf", Arrays.asList(new ArgumentType[] {})));
		
		InstructionSet bruh = new InstructionSet(set);
		
		for(Instruction i : set) System.out.println(i);
		System.out.println();
		
		asdf(bruh.assemble("WRITE", Arrays.asList(new ArgumentType[] {}), new int[] {}));
		asdf(bruh.assemble("WRITEIW", Arrays.asList(new ArgumentType[] {ArgumentType.WORD}), new int[] {0x6968}));
		asdf(bruh.assemble("WRITEI", Arrays.asList(new ArgumentType[] {ArgumentType.BYTE}), new int[] {0x20}));
		asdf(bruh.assemble("asdf", Arrays.asList(new ArgumentType[] {}), new int[] {}));
	}
	
	private static void asdf(byte[] b) {
		for(int i = 0; i < b.length; i++) {
			System.out.print(String.format("%8S", Integer.toHexString(b[i])).substring(6, 8).replace(' ', '0') + " ");
		}
		
		System.out.println();
	}
}
