package mechafinch.ssm.assembler;

/**
 * Represents an argument for an instruction
 * 
 * @author Mechafinch
 */
public enum ArgumentType {
	BYTE(1),
	UNSIGNED_BYTE(1),
	WORD(2),
	LABEL(2),
	OFFSET_LABEL(2),
	SHORT_OFFSET_LABEL(2);
	
	public int length;
	
	public int getLength() { return length; } // getty boi for the stream gods
	
	private ArgumentType(int length) {
		this.length = length;
	}
}