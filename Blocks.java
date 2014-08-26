package mw.library;

import mw.editor.BlockData;
import mw.editor.ModEditor;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import codechicken.multipart.MultipartHelper;

public class Blocks {
	
	protected static boolean forgeMicroParts;
	
	public int blockId;
	public int metadata;
	public NBTTagCompound nbtData;
	
	public Blocks get(World world, int x, int y, int z) {
		this.blockId = world.getBlockId(x, y, z);
		this.metadata = world.getBlockMetadata(x, y, z);
		if (this.blockId > 0 && Block.blocksList[this.blockId].hasTileEntity(this.metadata)) {
			this.nbtData = new NBTTagCompound();
			TileEntity te =	world.getBlockTileEntity(x, y, z);
			te.writeToNBT(this.nbtData);
			this.nbtData.removeTag("x");
			this.nbtData.removeTag("y");
			this.nbtData.removeTag("z");
		} else {
			this.nbtData = null;
		}
		return this;
	}
	
	public void set(World world, int x, int y, int z) {
		world.setBlock(x, y, z, blockId, metadata, 2);
		TileEntity te = this.getTileEntity(world, x, y, z);
		if (te != null) {
            world.setBlockTileEntity(x, y, z, te);
			if (Blocks.forgeMicroParts && nbtData.getString("id").equals("savedMultipart")) {
				MultipartHelper.sendDescPacket(world, te);
			}
			world.markBlockForUpdate(x, y, z);
		}
		world.setBlockMetadataWithNotify(x, y, z, metadata, 3);
	}
	
	private TileEntity getTileEntity(World world, int x, int y, int z) {
		if (this.nbtData != null) {
			NBTTagCompound nbtData = (NBTTagCompound) this.nbtData.copy();
			nbtData.setInteger("x", x);
			nbtData.setInteger("y", y);
	        nbtData.setInteger("z", z);
			if (Blocks.forgeMicroParts && nbtData.getString("id").equals("savedMultipart")) {
				return MultipartHelper.createTileFromNBT(world, nbtData);
			}
			return TileEntity.createAndLoadEntity(nbtData);
		}
		return null;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Blocks)) {
			return false;
		} 
		BlockData b = (BlockData) o;
		if (b.blockId == this.blockId && b.metadata == this.metadata) {
			if (b.nbtData == null && this.nbtData == null) {
				return true;
			} else if (b.nbtData != null && this.nbtData != null) {
				return b.nbtData.equals(this.nbtData);
			}
		}
		return false;
	}
}
