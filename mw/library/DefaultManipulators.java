package mw.library;

import net.minecraft.block.Block;

public final class DefaultManipulators {

	public static class Default implements IBlockManipulator {

		@Override
		public Blocks rotate90(Blocks block) {
			return block;
		}

		@Override
		public Blocks rotate180(Blocks block) {
			return block;
		}

		@Override
		public Blocks rotate270(Blocks block) {
			return block;
		}

		@Override
		public Blocks mirrorX(Blocks block) {
			return block;
		}

		@Override
		public Blocks mirrorZ(Blocks block) {
			return block;
		}
	}

	public static class Log extends Default {

		@Override
		public Blocks rotate90(Blocks block) {
			switch (block.metadata & 12) {
			case 4:
				block.metadata += 4;
				break;
			case 8:
				block.metadata -= 4;
				break;
			}
			return block;
		}

		@Override
		public Blocks rotate270(Blocks block) {
			return this.rotate90(block);
		}
	}

	// Manipulates the bits for directionality
	public static class Bits implements IBlockManipulator {

		private final byte	NORTH;
		private final byte	EAST;
		private final byte	SOUTH;
		private final byte	WEST;
		private final byte	directionBitMask;
		private final byte	otherBitMask;

		public Bits(int i, int j, int k, int l, int m) {
			this.NORTH = (byte) j;
			this.EAST = (byte) k;
			this.SOUTH = (byte) l;
			this.WEST = (byte) m;

			this.directionBitMask = (byte) i;
			this.otherBitMask = (byte) ((~i) & 15);
		}

		@Override
		public Blocks rotate90(Blocks block) {
			int metadata = block.metadata & this.directionBitMask;
			block.metadata &= this.otherBitMask;
			if (metadata == this.NORTH) {
				block.metadata |= EAST;
			} else if (metadata == this.EAST) {
				block.metadata |= SOUTH;
			} else if (metadata == this.SOUTH) {
				block.metadata |= WEST;
			} else if (metadata == this.WEST) {
				block.metadata |= NORTH;
			} else {
				block.metadata |= metadata;
			}
			return block;
		}

		@Override
		public Blocks rotate180(Blocks block) {
			int metadata = block.metadata & this.directionBitMask;
			block.metadata &= this.otherBitMask;
			if (metadata == NORTH) {
				block.metadata |= SOUTH;
			} else if (metadata == EAST) {
				block.metadata |= WEST;
			} else if (metadata == SOUTH) {
				block.metadata |= NORTH;
			} else if (metadata == WEST) {
				block.metadata |= EAST;
			} else {
				block.metadata |= metadata;
			}
			return block;
		}

		@Override
		public Blocks rotate270(Blocks block) {
			int metadata = block.metadata & this.directionBitMask;
			block.metadata &= this.otherBitMask;
			if (metadata == NORTH) {
				block.metadata |= WEST;
			} else if (metadata == EAST) {
				block.metadata |= NORTH;
			} else if (metadata == SOUTH) {
				block.metadata |= EAST;
			} else if (metadata == WEST) {
				block.metadata |= SOUTH;
			} else {
				block.metadata |= metadata;
			}
			return block;
		}

		@Override
		public Blocks mirrorX(Blocks block) {
			int metadata = block.metadata & this.directionBitMask;
			block.metadata &= this.otherBitMask;
			if (metadata == EAST) {
				block.metadata |= WEST;
			} else if (metadata == WEST) {
				block.metadata |= EAST;
			} else {
				block.metadata |= metadata;
			}
			return block;
		}

		@Override
		public Blocks mirrorZ(Blocks block) {
			int metadata = block.metadata & this.directionBitMask;
			block.metadata &= this.otherBitMask;
			if (metadata == NORTH) {
				block.metadata |= SOUTH;
			} else if (metadata == SOUTH) {
				block.metadata |= NORTH;
			} else {
				block.metadata |= metadata;
			}
			return block;
		}
	}

	public static class Sign implements IBlockManipulator {

		@Override
		public Blocks rotate90(Blocks block) {
			block.metadata = (block.metadata + 4) & 15;
			return block;
		}

		@Override
		public Blocks rotate180(Blocks block) {
			block.metadata = (block.metadata + 8) & 15;
			return block;
		}

		@Override
		public Blocks rotate270(Blocks block) {
			block.metadata = (block.metadata + 12) & 15;
			return block;
		}

		@Override
		public Blocks mirrorX(Blocks block) {
			block.metadata = (16 - block.metadata) & 15;
			return block;
		}

		@Override
		public Blocks mirrorZ(Blocks block) {
			block.metadata = (8 - (block.metadata & 7)) | (block.metadata & 8);
			return block;
		}

	}

	public static class Door implements IBlockManipulator {

		@Override
		public Blocks rotate90(Blocks block) {
			if ((block.metadata & 8) == 0) {
				block.metadata = ((block.metadata & 3) + 1) & 3 | (block.metadata & 4);
			}
			return block;
		}

		@Override
		public Blocks rotate180(Blocks block) {
			if ((block.metadata & 8) == 0) {
				block.metadata = ((block.metadata & 3) + 2) & 3 | (block.metadata & 4);
			}
			return block;
		}

		@Override
		public Blocks rotate270(Blocks block) {
			if ((block.metadata & 8) == 0) {
				block.metadata = ((block.metadata & 3) + 3) & 3 | (block.metadata & 4);
			}
			return block;
		}

		@Override
		public Blocks mirrorX(Blocks block) {
			if ((block.metadata & 8) == 0) {
				int metadata = block.metadata & 3;
				if (metadata == 0 || metadata == 2) {
					block.metadata = (metadata + 2) & 3 | (block.metadata & 4);
				}
			} else {
				block.metadata ^= 1;
			}
			return block;
		}

		@Override
		public Blocks mirrorZ(Blocks block) {
			if ((block.metadata & 8) == 0) {
				int metadata = block.metadata & 3;
				if (metadata == 1 || metadata == 3) {
					block.metadata = (metadata + 2) & 3 | (block.metadata & 4);
				}
			} else {
				block.metadata ^= 1;
			}
			return block;
		}
	}

	public static class Rail implements IBlockManipulator {

		private static final byte	NORTHSOUTH	= 0;
		private static final byte	EASTWEST	= 1;
		private static final byte	EAST		= 2;
		private static final byte	WEST		= 3;
		private static final byte	NORTH		= 4;
		private static final byte	SOUTH		= 5;
		private static final byte	NORTHWEST	= 6;
		private static final byte	NORTHEAST	= 7;
		private static final byte	SOUTHEAST	= 8;
		private static final byte	SOUTHWEST	= 9;

		@Override
		public Blocks rotate90(Blocks block) {
			switch (block.metadata) {
			case NORTHSOUTH:
				block.metadata = EASTWEST;
				break;
			case EASTWEST:
				block.metadata = NORTHSOUTH;
				break;
			case EAST:
				block.metadata = SOUTH;
				break;
			case WEST:
				block.metadata = NORTH;
				break;
			case NORTH:
				block.metadata = EAST;
				break;
			case SOUTH:
				block.metadata = WEST;
				break;
			case NORTHWEST:
				block.metadata = NORTHEAST;
				break;
			case NORTHEAST:
				block.metadata = SOUTHEAST;
				break;
			case SOUTHEAST:
				block.metadata = SOUTHWEST;
				break;
			case SOUTHWEST:
				block.metadata = NORTHWEST;
				break;
			}
			return block;
		}

		@Override
		public Blocks rotate180(Blocks block) {
			switch (block.metadata) {
			case EAST:
				block.metadata = WEST;
				break;
			case WEST:
				block.metadata = EAST;
				break;
			case NORTH:
				block.metadata = SOUTH;
				break;
			case SOUTH:
				block.metadata = NORTH;
				break;
			case NORTHWEST:
				block.metadata = SOUTHEAST;
				break;
			case NORTHEAST:
				block.metadata = SOUTHWEST;
				break;
			case SOUTHEAST:
				block.metadata = NORTHWEST;
				break;
			case SOUTHWEST:
				block.metadata = NORTHEAST;
				break;
			}
			return block;
		}

		@Override
		public Blocks rotate270(Blocks block) {
			switch (block.metadata) {
			case NORTHSOUTH:
				block.metadata = EASTWEST;
				break;
			case EASTWEST:
				block.metadata = NORTHSOUTH;
				break;
			case EAST:
				block.metadata = NORTH;
				break;
			case WEST:
				block.metadata = SOUTH;
				break;
			case NORTH:
				block.metadata = WEST;
				break;
			case SOUTH:
				block.metadata = EAST;
				break;
			case NORTHWEST:
				block.metadata = SOUTHWEST;
				break;
			case NORTHEAST:
				block.metadata = NORTHWEST;
				break;
			case SOUTHEAST:
				block.metadata = NORTHEAST;
				break;
			case SOUTHWEST:
				block.metadata = SOUTHEAST;
				break;
			}
			return block;
		}

		@Override
		public Blocks mirrorX(Blocks block) {
			switch (block.metadata) {
			case EAST:
				block.metadata = WEST;
				break;
			case WEST:
				block.metadata = EAST;
				break;
			case NORTHWEST:
				block.metadata = NORTHEAST;
				break;
			case NORTHEAST:
				block.metadata = NORTHWEST;
				break;
			case SOUTHEAST:
				block.metadata = SOUTHWEST;
				break;
			case SOUTHWEST:
				block.metadata = SOUTHEAST;
				break;
			}
			return block;
		}

		@Override
		public Blocks mirrorZ(Blocks block) {
			switch (block.metadata) {
			case NORTH:
				block.metadata = SOUTH;
				break;
			case SOUTH:
				block.metadata = NORTH;
				break;
			case NORTHWEST:
				block.metadata = SOUTHWEST;
				break;
			case NORTHEAST:
				block.metadata = SOUTHEAST;
				break;
			case SOUTHEAST:
				block.metadata = NORTHEAST;
				break;
			case SOUTHWEST:
				block.metadata = NORTHWEST;
				break;
			}
			return block;
		}
	}

	public static class PowerableRail extends Rail {

		@Override
		public Blocks rotate90(Blocks block) {
			int powered = block.metadata & 8;
			block.metadata &= 7;
			super.rotate90(block);
			block.metadata |= powered;
			return block;
		}

		@Override
		public Blocks rotate180(Blocks block) {
			int powered = block.metadata & 8;
			block.metadata &= 7;
			super.rotate180(block);
			block.metadata |= powered;
			return block;
		}

		@Override
		public Blocks rotate270(Blocks block) {
			int powered = block.metadata & 8;
			block.metadata &= 7;
			super.rotate270(block);
			block.metadata |= powered;
			return block;
		}

		@Override
		public Blocks mirrorX(Blocks block) {
			int powered = block.metadata & 8;
			block.metadata &= 7;
			super.mirrorX(block);
			block.metadata |= powered;
			return block;
		}

		@Override
		public Blocks mirrorZ(Blocks block) {
			int powered = block.metadata & 8;
			block.metadata &= 7;
			super.mirrorZ(block);
			block.metadata |= powered;
			return block;
		}
	}

	public static class Lever implements IBlockManipulator {

		private static final byte	NORTH			= 4;
		private static final byte	EAST			= 1;
		private static final byte	SOUTH			= 3;
		private static final byte	WEST			= 2;
		private static final byte	GROUNDNORTHSOUTH	= 5;
		private static final byte	GROUNDEASTWEST		= 6;
		private static final byte	CEILINGNORTHSOUTH	= 7;
		private static final byte	CEILINGEASTWEST		= 0;

		@Override
		public Blocks rotate90(Blocks block) {
			int powered = block.metadata & 8;
			switch (block.metadata & 7) {
			case NORTH:
				block.metadata = EAST;
				break;
			case EAST:
				block.metadata = SOUTH;
				break;
			case SOUTH:
				block.metadata = WEST;
				break;
			case WEST:
				block.metadata = NORTH;
				break;
			case GROUNDNORTHSOUTH:
				block.metadata = GROUNDEASTWEST;
				break;
			case GROUNDEASTWEST:
				block.metadata = GROUNDNORTHSOUTH;
				break;
			case CEILINGNORTHSOUTH:
				block.metadata = CEILINGEASTWEST;
				break;
			case CEILINGEASTWEST:
				block.metadata = CEILINGNORTHSOUTH;
				break;
			}
			block.metadata |= powered;
			return block;
		}

		@Override
		public Blocks rotate180(Blocks block) {
			int powered = block.metadata & 8;
			switch (block.metadata & 7) {
			case NORTH:
				block.metadata = SOUTH;
				break;
			case EAST:
				block.metadata = WEST;
				break;
			case SOUTH:
				block.metadata = NORTH;
				break;
			case WEST:
				block.metadata = EAST;
				break;
			}
			block.metadata |= powered;
			return block;
		}

		@Override
		public Blocks rotate270(Blocks block) {
			int powered = block.metadata & 8;
			switch (block.metadata & 7) {
			case NORTH:
				block.metadata = WEST;
				break;
			case EAST:
				block.metadata = NORTH;
				break;
			case SOUTH:
				block.metadata = EAST;
				break;
			case WEST:
				block.metadata = SOUTH;
				break;
			case GROUNDNORTHSOUTH:
				block.metadata = GROUNDEASTWEST;
				break;
			case GROUNDEASTWEST:
				block.metadata = GROUNDNORTHSOUTH;
				break;
			case CEILINGNORTHSOUTH:
				block.metadata = CEILINGEASTWEST;
				break;
			case CEILINGEASTWEST:
				block.metadata = CEILINGNORTHSOUTH;
				break;
			}
			block.metadata |= powered;
			return block;
		}

		@Override
		public Blocks mirrorX(Blocks block) {
			int powered = block.metadata & 8;
			switch (block.metadata & 7) {
			case EAST:
				block.metadata = WEST;
				break;
			case WEST:
				block.metadata = EAST;
				break;
			}
			block.metadata |= powered;
			return block;
		}

		@Override
		public Blocks mirrorZ(Blocks block) {
			int powered = block.metadata & 8;
			switch (block.metadata & 7) {
			case NORTH:
				block.metadata = SOUTH;
				break;
			case SOUTH:
				block.metadata = NORTH;
				break;
			}
			block.metadata |= powered;
			return block;
		}
	}

	public static class Mushroom implements IBlockManipulator {

		private static final byte	NORTH		= 2;
		private static final byte	NORTHEAST	= 3;
		private static final byte	EAST		= 6;
		private static final byte	SOUTHEAST	= 9;
		private static final byte	SOUTH		= 8;
		private static final byte	SOUTHWEST	= 7;
		private static final byte	WEST		= 4;
		private static final byte	NORTHWEST	= 1;

		@Override
		public Blocks rotate90(Blocks block) {
			switch (block.metadata) {
			case NORTH:
				block.metadata = EAST;
				break;
			case NORTHEAST:
				block.metadata = SOUTHEAST;
				break;
			case EAST:
				block.metadata = SOUTH;
				break;
			case SOUTHEAST:
				block.metadata = SOUTHWEST;
				break;
			case SOUTH:
				block.metadata = WEST;
				break;
			case SOUTHWEST:
				block.metadata = NORTHWEST;
				break;
			case WEST:
				block.metadata = NORTH;
				break;
			case NORTHWEST:
				block.metadata = NORTHEAST;
				break;
			}
			return block;
		}

		@Override
		public Blocks rotate180(Blocks block) {
			switch (block.metadata) {
			case NORTH:
				block.metadata = SOUTH;
				break;
			case NORTHEAST:
				block.metadata = SOUTHWEST;
				break;
			case EAST:
				block.metadata = WEST;
				break;
			case SOUTHEAST:
				block.metadata = NORTHWEST;
				break;
			case SOUTH:
				block.metadata = NORTH;
				break;
			case SOUTHWEST:
				block.metadata = NORTHEAST;
				break;
			case WEST:
				block.metadata = EAST;
				break;
			case NORTHWEST:
				block.metadata = SOUTHEAST;
				break;
			}
			return block;
		}

		@Override
		public Blocks rotate270(Blocks block) {
			switch (block.metadata) {
			case NORTH:
				block.metadata = WEST;
				break;
			case NORTHEAST:
				block.metadata = NORTHWEST;
				break;
			case EAST:
				block.metadata = NORTH;
				break;
			case SOUTHEAST:
				block.metadata = NORTHEAST;
				break;
			case SOUTH:
				block.metadata = EAST;
				break;
			case SOUTHWEST:
				block.metadata = SOUTHEAST;
				break;
			case WEST:
				block.metadata = SOUTH;
				break;
			case NORTHWEST:
				block.metadata = SOUTHWEST;
				break;
			}
			return block;
		}

		@Override
		public Blocks mirrorX(Blocks block) {
			switch (block.metadata) {
			case NORTHEAST:
				block.metadata = NORTHWEST;
				break;
			case EAST:
				block.metadata = WEST;
				break;
			case SOUTHEAST:
				block.metadata = SOUTHWEST;
				break;
			case SOUTHWEST:
				block.metadata = SOUTHEAST;
				break;
			case WEST:
				block.metadata = EAST;
				break;
			case NORTHWEST:
				block.metadata = NORTHEAST;
				break;
			}
			return block;
		}

		@Override
		public Blocks mirrorZ(Blocks block) {
			switch (block.metadata) {
			case NORTH:
				block.metadata = SOUTH;
				break;
			case NORTHEAST:
				block.metadata = SOUTHEAST;
				break;
			case SOUTHEAST:
				block.metadata = NORTHEAST;
				break;
			case SOUTH:
				block.metadata = NORTH;
				break;
			case SOUTHWEST:
				block.metadata = NORTHWEST;
				break;
			case NORTHWEST:
				block.metadata = SOUTHWEST;
				break;
			}
			return block;
		}
	}

	public static class Vines implements IBlockManipulator {

		private static final byte	NORTH	= 4;
		private static final byte	EAST	= 8;
		private static final byte	SOUTH	= 1;
		private static final byte	WEST	= 2;

		@Override
		public Blocks rotate90(Blocks block) {
			int metadata = block.metadata;
			block.metadata = 0;
			if ((metadata & NORTH) > 0) {
				block.metadata |= EAST;
			}
			if ((metadata & EAST) > 0) {
				block.metadata |= SOUTH;
			}
			if ((metadata & SOUTH) > 0) {
				block.metadata |= WEST;
			}
			if ((metadata & WEST) > 0) {
				block.metadata |= NORTH;
			}
			return block;
		}

		@Override
		public Blocks rotate180(Blocks block) {
			int metadata = block.metadata;
			block.metadata = 0;
			if ((metadata & NORTH) > 0) {
				block.metadata |= SOUTH;
			}
			if ((metadata & EAST) > 0) {
				block.metadata |= WEST;
			}
			if ((metadata & SOUTH) > 0) {
				block.metadata |= NORTH;
			}
			if ((metadata & WEST) > 0) {
				block.metadata |= EAST;
			}
			return block;
		}

		@Override
		public Blocks rotate270(Blocks block) {
			int metadata = block.metadata;
			block.metadata = 0;
			if ((metadata & NORTH) > 0) {
				block.metadata |= WEST;
			}
			if ((metadata & EAST) > 0) {
				block.metadata |= NORTH;
			}
			if ((metadata & SOUTH) > 0) {
				block.metadata |= EAST;
			}
			if ((metadata & WEST) > 0) {
				block.metadata |= SOUTH;
			}
			return block;
		}

		@Override
		public Blocks mirrorX(Blocks block) {
			int metadata = block.metadata;
			block.metadata = 0;
			if ((metadata & NORTH) > 0) {
				block.metadata |= NORTH;
			}
			if ((metadata & EAST) > 0) {
				block.metadata |= WEST;
			}
			if ((metadata & SOUTH) > 0) {
				block.metadata |= SOUTH;
			}
			if ((metadata & WEST) > 0) {
				block.metadata |= EAST;
			}
			return block;
		}

		@Override
		public Blocks mirrorZ(Blocks block) {
			int metadata = block.metadata;
			block.metadata = 0;
			if ((metadata & NORTH) > 0) {
				block.metadata |= SOUTH;
			}
			if ((metadata & EAST) > 0) {
				block.metadata |= EAST;
			}
			if ((metadata & SOUTH) > 0) {
				block.metadata |= NORTH;
			}
			if ((metadata & WEST) > 0) {
				block.metadata |= WEST;
			}
			return block;
		}
	}

	public static class Skull extends Bits {

		private static final String	rotationString	= "Rot";

		public Skull() {
			super(7, 2, 4, 3, 5);
		}

		@Override
		public Blocks rotate90(Blocks block) {
			if (block.metadata != 1) {
				return super.rotate90(block);
			}
			block.nbtData.setByte(rotationString, (byte) ((block.nbtData.getByte(rotationString) + 4) & 15));
			return block;
		}

		@Override
		public Blocks rotate180(Blocks block) {
			if (block.metadata != 1) {
				return super.rotate180(block);
			}
			block.nbtData.setByte(rotationString, (byte) ((block.nbtData.getByte(rotationString) + 8) & 15));
			return block;
		}

		@Override
		public Blocks rotate270(Blocks block) {
			if (block.metadata != 1) {
				return super.rotate270(block);
			}
			block.nbtData.setByte(rotationString, (byte) ((block.nbtData.getByte(rotationString) + 12) & 15));
			return block;
		}

		@Override
		public Blocks mirrorX(Blocks block) {
			if (block.metadata != 1) {
				return super.mirrorX(block);
			}
			block.nbtData.setByte(rotationString, (byte) ((16 - (block.nbtData.getByte(rotationString) & 3)) & 15));
			return block;
		}

		@Override
		public Blocks mirrorZ(Blocks block) {
			if (block.metadata != 1) {
				return super.mirrorZ(block);
			}
			byte rot = block.nbtData.getByte(rotationString);
			block.nbtData.setByte(rotationString, (byte) ((8 - (rot & 3)) | (rot & 8)));
			return block;
		}
	}

	protected static void Register() {
		BlockManipulator.registerManipulator(Block.wood, new Log());

		IBlockManipulator uewsn = new Bits(7, 4, 1, 3, 2);
		BlockManipulator.registerManipulator(Block.torchWood, uewsn);
		BlockManipulator.registerManipulator(Block.torchRedstoneIdle, uewsn);
		BlockManipulator.registerManipulator(Block.torchRedstoneActive, uewsn);
		BlockManipulator.registerManipulator(Block.stoneButton, uewsn);
		BlockManipulator.registerManipulator(Block.woodenButton, uewsn);

		IBlockManipulator nesw = new Bits(3, 0, 1, 2, 3);
		BlockManipulator.registerManipulator(Block.bed, nesw);
		BlockManipulator.registerManipulator(Block.redstoneRepeaterIdle, nesw);
		BlockManipulator.registerManipulator(Block.redstoneRepeaterActive, nesw);
		BlockManipulator.registerManipulator(Block.redstoneComparatorIdle, nesw);
		BlockManipulator.registerManipulator(Block.redstoneComparatorActive, nesw);
		BlockManipulator.registerManipulator(Block.cocoaPlant, nesw);
		BlockManipulator.registerManipulator(Block.anvil, nesw);

		IBlockManipulator uunswe = new Bits(7, 2, 5, 3, 4);
		BlockManipulator.registerManipulator(Block.pistonBase, uunswe);
		BlockManipulator.registerManipulator(Block.pistonExtension, uunswe);
		BlockManipulator.registerManipulator(Block.pistonMoving, uunswe);
		BlockManipulator.registerManipulator(Block.pistonStickyBase, uunswe);
		BlockManipulator.registerManipulator(Block.ladder, uunswe);
		BlockManipulator.registerManipulator(Block.signWall, uunswe);
		BlockManipulator.registerManipulator(Block.furnaceIdle, uunswe);
		BlockManipulator.registerManipulator(Block.furnaceBurning, uunswe);
		BlockManipulator.registerManipulator(Block.chest, uunswe);
		BlockManipulator.registerManipulator(Block.dispenser, uunswe);
		BlockManipulator.registerManipulator(Block.dropper, uunswe);
		BlockManipulator.registerManipulator(Block.hopperBlock, uunswe);
		BlockManipulator.registerManipulator(Block.enderChest, uunswe);
		BlockManipulator.registerManipulator(Block.chestTrapped, uunswe);
		// BlockManipulator.registerManipulator(Block.lockedChest,
		// uunswe);

		IBlockManipulator ewsn = new Bits(3, 3, 0, 2, 1);
		BlockManipulator.registerManipulator(Block.stairsWoodOak, ewsn);
		BlockManipulator.registerManipulator(Block.stairsCobblestone, ewsn);
		BlockManipulator.registerManipulator(Block.stairsBrick, ewsn);
		BlockManipulator.registerManipulator(Block.stairsStoneBrick, ewsn);
		BlockManipulator.registerManipulator(Block.stairsNetherBrick, ewsn);
		BlockManipulator.registerManipulator(Block.stairsSandStone, ewsn);
		BlockManipulator.registerManipulator(Block.stairsWoodSpruce, ewsn);
		BlockManipulator.registerManipulator(Block.stairsWoodBirch, ewsn);
		BlockManipulator.registerManipulator(Block.stairsWoodJungle, ewsn);
		BlockManipulator.registerManipulator(Block.stairsNetherQuartz, ewsn);

		BlockManipulator.registerManipulator(Block.signPost, new Sign());

		IBlockManipulator door = new Door();
		BlockManipulator.registerManipulator(Block.doorWood, door);
		BlockManipulator.registerManipulator(Block.doorIron, door);

		BlockManipulator.registerManipulator(Block.rail, new Rail());

		IBlockManipulator rail = new PowerableRail();
		BlockManipulator.registerManipulator(Block.railPowered, rail);
		BlockManipulator.registerManipulator(Block.railDetector, rail);
		BlockManipulator.registerManipulator(Block.railActivator, rail);

		BlockManipulator.registerManipulator(Block.lever, new Lever());

		IBlockManipulator swne = new Bits(3, 2, 3, 0, 1);
		BlockManipulator.registerManipulator(Block.pumpkin, swne);
		BlockManipulator.registerManipulator(Block.pumpkinLantern, swne);
		BlockManipulator.registerManipulator(Block.fenceGate, swne);
		BlockManipulator.registerManipulator(Block.endPortalFrame, swne);
		BlockManipulator.registerManipulator(Block.tripWireSource, swne);

		IBlockManipulator snew = new Bits(3, 1, 2, 0, 3);
		BlockManipulator.registerManipulator(Block.trapdoor, snew);

		IBlockManipulator mushroom = new Mushroom();
		BlockManipulator.registerManipulator(Block.mushroomCapBrown, mushroom);
		BlockManipulator.registerManipulator(Block.mushroomCapRed, mushroom);

		BlockManipulator.registerManipulator(Block.vine, new Vines());

		BlockManipulator.registerManipulator(Block.skull, new Skull());
	}
}
