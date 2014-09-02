package mw.library;

public interface IBlockManipulator {
	
	public Blocks rotate90(Blocks block);
	public Blocks rotate180(Blocks block);
	public Blocks rotate270(Blocks block);

	public Blocks mirrorX(Blocks block);
	public Blocks mirrorZ(Blocks block);
}
