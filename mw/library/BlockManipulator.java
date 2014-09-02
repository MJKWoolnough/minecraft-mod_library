package mw.library;

import net.minecraft.block.Block;

public class BlockManipulator {
	
	private static IBlockManipulator[] manipulatorList = new IBlockManipulator[4096];
	private static DefaultManipulators.Default defaultManipulator = new DefaultManipulators.Default();
	
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
	
	static {
		DefaultManipulators.Register();
	}
}
