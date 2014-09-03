package mw.library;

import net.minecraft.block.Block;

public class BlockManipulator {
	
	private static final IBlockManipulator[] manipulatorList = new IBlockManipulator[4096];
	private static final IBlockManipulator defaultManipulator = new DefaultManipulators.Default();
	
	public static boolean registerManipulator(Block block, IBlockManipulator bm) {
		int blockId = block.blockID;
		if (manipulatorList[blockId] != null) {
			return false;
		}
		manipulatorList[blockId] = bm;
		return true;
	}
	
	public static IBlockManipulator getManipulator(int blockId) {
		if (manipulatorList[blockId] != null) {
			return manipulatorList[blockId];
		}
		return defaultManipulator;
	}
	
	public static Blocks rotate90(Blocks block) {
		return getManipulator(block.blockId).rotate90(block);
	}
	
	public static Blocks rotate180(Blocks block) {
		return getManipulator(block.blockId).rotate180(block);
	}
	
	public static Blocks rotate270(Blocks block) {
		return getManipulator(block.blockId).rotate270(block);
	}
	
	public static Blocks mirrorX(Blocks block) {
		return getManipulator(block.blockId).mirrorX(block);
	}
	
	public static Blocks mirrorZ(Blocks block) {
		return getManipulator(block.blockId).mirrorZ(block);
	}
	
	static {
		DefaultManipulators.Register();
	}
}
