package mw.library;

import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class Area {

	private final int[]	coords	= new int[6];
	private World		world;

	public Area(World world, int x1, int y1, int z1, int x2, int y2, int z2) {
		this.setCoords(world, x1, y1, z1, x2, y2, z2);
	}

	public int[] getCoords() {
		return coords.clone();
	}

	public void setCoords(World world, int x1, int y1, int z1, int x2, int y2, int z2) {
		this.world = world;
		this.coords[0] = min(x1, x2);
		this.coords[1] = min(y1, y2);
		this.coords[2] = min(z1, z2);
		this.coords[3] = max(x1, x2);
		this.coords[4] = max(y1, y2);
		this.coords[5] = max(z1, z2);
	}

	public boolean overlap(Area area) {
		return area.world == this.world && !(area.coords[0] > this.coords[3] || area.coords[3] < this.coords[0] || area.coords[1] > this.coords[4] || area.coords[4] < this.coords[1] || area.coords[2] > this.coords[5] || area.coords[5] < this.coords[2]);
	}

	public int width() {
		return this.coords[3] - this.coords[0] + 1; // no zero width, everything contains at least one block
	}

	public int height() {
		return this.coords[4] - this.coords[1] + 1; // no zero height
	}

	public int depth() {
		return this.coords[5] - this.coords[2] + 1; // no zero depth
	}

	public Blocks get(int x, int y, int z) {
		return this.get(new Blocks(), x, y, z);
	}

	public Blocks get(Blocks block, int x, int y, int z) {
		return block.get(this.world, this.coords[0] + x, this.coords[1] + y, this.coords[2] + z);
	}

	public void set(Blocks block, int x, int y, int z) {
		block.set(this.world, this.coords[0] + x, this.coords[1] + y, this.coords[2] + z);
	}

	public void fill(Blocks block) {
		for (int i = this.coords[0]; i <= this.coords[3]; i++) {
			for (int j = this.coords[1]; j <= this.coords[4]; j++) {
				for (int k = this.coords[2]; k <= this.coords[5]; k++) {
					block.set(this.world, i, j, k);
				}
			}
		}
	}

	public void replace(Blocks replace, Blocks with) {
		Blocks bd = new Blocks();
		for (int i = this.coords[0]; i <= this.coords[3]; i++) {
			for (int j = this.coords[1]; j <= this.coords[4]; j++) {
				for (int k = this.coords[2]; k <= this.coords[5]; k++) {
					bd.get(this.world, i, j, k);
					if (bd.equals(replace)) {
						with.set(this.world, i, j, k);
					}
				}
			}
		}
	}

	public boolean copyTo(Area area) {
		if (area.width() != this.width() || area.height() != this.height() || area.depth() != this.depth()) {
			return false;
		}

		int xStart;
		int xEnd;
		int xStep;

		int yStart;
		int yEnd;
		int yStep;

		int zStart;
		int zEnd;
		int zStep;

		Blocks bd = new Blocks();

		if (this.coords[0] < area.coords[0]) {
			xStart = this.width() - 1;
			xEnd = -1;
			xStep = -1;
		} else {
			xStart = 0;
			xEnd = this.width();
			xStep = 1;
		}

		if (this.coords[1] < area.coords[1]) {
			yStart = this.height() - 1;
			yEnd = -1;
			yStep = -1;
		} else {
			yStart = 0;
			yEnd = this.height();
			yStep = 1;
		}

		if (this.coords[2] < area.coords[2]) {
			zStart = this.depth() - 1;
			zEnd = -1;
			zStep = -1;
		} else {
			zStart = 0;
			zEnd = this.depth();
			zStep = 1;
		}

		for (int i = xStart; i != xEnd; i += xStep) {
			for (int j = yStart; j != yEnd; j += yStep) {
				for (int k = zStart; k != zEnd; k += zStep) {
					area.set(this.get(bd, i, j, k), i, j, k);
				}
			}
		}
		return true;
	}

	public void copyInDirection(ForgeDirection direction, int times) {
		int width = this.width();
		int height = this.height();
		int depth = this.depth();

		Blocks bd = new Blocks();
		bd.notifyChange = false;

		for (int i = this.coords[0]; i <= this.coords[3]; i++) {
			for (int j = this.coords[1]; j <= this.coords[4]; j++) {
				for (int k = this.coords[2]; k <= this.coords[5]; k++) {
					bd.get(this.world, i, j, k);
					int a = i;
					int b = j;
					int c = k;
					for (int d = times; d > 0; d--) {
						a += width * direction.offsetX;
						b += height * direction.offsetY;
						c += depth * direction.offsetZ;
						bd.set(world, a, b, c);
					}
				}
			}
		}
		int x1 = min(this.coords[0] + width * direction.offsetX, this.coords[0] + width * direction.offsetX * times);
		int y1 = min(this.coords[1] + height * direction.offsetY, this.coords[1] + height * direction.offsetY * times);
		int z1 = min(this.coords[2] + depth * direction.offsetZ, this.coords[2] + depth * direction.offsetZ * times);
		int x2 = max(this.coords[3] + width * direction.offsetX, this.coords[3] + width * direction.offsetX * times);
		int y2 = max(this.coords[4] + height * direction.offsetY, this.coords[4] + height * direction.offsetY * times);
		int z2 = max(this.coords[5] + depth * direction.offsetZ, this.coords[5] + depth * direction.offsetZ * times);
		this.notifyBlockChanges(x1, y1, z1, x2, y2, z2);
	}

	public boolean rotate90() {
		if (this.width() != this.depth()) {
			return false;
		}
		Blocks topLeft = new Blocks();
		Blocks other = new Blocks();

		topLeft.notifyChange = false;
		other.notifyChange = false;

		int width = this.coords[3] - this.coords[0];

		for (int i = 0; i <= width >> 1; i++) {
			int leftPos = i;
			int rightPos = width - i;
			for (int k = 0; k <= (width - 1) >> 1; k++) {
				int topPos = k;
				int bottomPos = width - k;
				for (int j = this.coords[1]; j <= this.coords[4]; j++) {
					topLeft.get(this.world, this.coords[0] + leftPos, j, this.coords[2] + topPos); // top-left...
					if (leftPos != rightPos || topPos != bottomPos) {
						other.get(this.world, this.coords[0] + topPos, j, this.coords[2] + rightPos).rotate90().set(this.world, this.coords[0] + leftPos, j, this.coords[2] + topPos); // bottom-left -> top-left
						other.get(this.world, this.coords[0] + rightPos, j, this.coords[2] + bottomPos).rotate90().set(this.world, this.coords[0] + topPos, j, this.coords[2] + rightPos); // bottom-right -> bottom-left
						other.get(this.world, this.coords[0] + bottomPos, j, this.coords[2] + leftPos).rotate90().set(this.world, this.coords[0] + rightPos, j, this.coords[2] + bottomPos); // top-right -> bottom-right
					}
					topLeft.rotate90().set(this.world, this.coords[0] + bottomPos, j, this.coords[2] + leftPos); // ... -> top-right
				}
			}
		}

		if ((width & 1) == 0) { // middle blocks
			for (int j = this.coords[1]; j <= this.coords[4]; j++) {
				other.get(this.world, this.coords[0] + (width >> 1), j, this.coords[2] + (width >> 1)).rotate90().set(this.world, this.coords[0] + (width >> 1), j, this.coords[2] + (width >> 1));
			}
		}
		this.notifyBlockChanges(this.coords[0], this.coords[1], this.coords[2], this.coords[3], this.coords[4], this.coords[5]);
		return true;
	}

	public void rotate180() {
		Blocks left = new Blocks();
		Blocks right = new Blocks();

		left.notifyChange = false;
		right.notifyChange = false;

		for (int i = 0; i <= (this.coords[3] - this.coords[0]) >> 1; i++) {
			int leftPos = this.coords[0] + i;
			int rightPos = this.coords[3] - i;
			for (int k = 0; k <= this.coords[5] - this.coords[2]; k++) {
				int topPos = this.coords[2] + k;
				int bottomPos = this.coords[5] - k;
				if (leftPos == rightPos && topPos > bottomPos) {
					break;
				}
				for (int j = this.coords[1]; j <= this.coords[4]; j++) {
					left.get(this.world, leftPos, j, topPos);
					if (leftPos != rightPos || topPos != bottomPos) {
						right.get(this.world, rightPos, j, bottomPos).rotate180().set(this.world, leftPos, j, topPos);
					}
					left.rotate180().set(this.world, rightPos, j, bottomPos);
				}

			}
		}
		this.notifyBlockChanges(this.coords[0], this.coords[1], this.coords[2], this.coords[3], this.coords[4], this.coords[5]);
	}

	public boolean rotate270() {
		if (this.width() != this.depth()) {
			return false;
		}
		Blocks topLeft = new Blocks();
		Blocks other = new Blocks();

		topLeft.notifyChange = false;
		other.notifyChange = false;

		int width = this.coords[3] - this.coords[0];

		for (int i = 0; i <= width >> 1; i++) {
			int leftPos = i;
			int rightPos = width - i;
			for (int k = 0; k <= (width - 1) >> 1; k++) {
				int topPos = k;
				int bottomPos = width - k;
				for (int j = this.coords[1]; j <= this.coords[4]; j++) {
					topLeft.get(this.world, this.coords[0] + leftPos, j, this.coords[2] + topPos); // top-left...
					if (leftPos != rightPos || topPos != bottomPos) {
						other.get(this.world, this.coords[0] + bottomPos, j, this.coords[2] + leftPos).rotate270().set(this.world, this.coords[0] + leftPos, j, this.coords[2] + topPos); // top-right -> top-left
						other.get(this.world, this.coords[0] + rightPos, j, this.coords[2] + bottomPos).rotate270().set(this.world, this.coords[0] + bottomPos, j, this.coords[2] + leftPos); // bottom-right -> top-right
						other.get(this.world, this.coords[0] + topPos, j, this.coords[2] + rightPos).rotate270().set(this.world, this.coords[0] + rightPos, j, this.coords[2] + bottomPos); // bottom-left -> bottom-right
					}
					topLeft.rotate270().set(this.world, this.coords[0] + topPos, j, this.coords[2] + rightPos); // ... -> bottom-left
				}
			}
		}

		if ((width & 1) == 0) { // middle blocks
			for (int j = this.coords[1]; j <= this.coords[4]; j++) {
				other.get(this.world, this.coords[0] + (width >> 1), j, this.coords[2] + (width >> 1)).rotate270().set(this.world, this.coords[0] + (width >> 1), j, this.coords[2] + (width >> 1));
			}
		}
		this.notifyBlockChanges(this.coords[0], this.coords[1], this.coords[2], this.coords[3], this.coords[4], this.coords[5]);
		return true;
	}

	public void mirrorX() {
		Blocks left = new Blocks();
		Blocks right = new Blocks();

		left.notifyChange = false;
		right.notifyChange = false;

		for (int i = 0; i <= (this.coords[3] - this.coords[0]) >> 1; i++) {
			int leftPos = this.coords[0] + i;
			int rightPos = this.coords[3] - i;
			for (int j = this.coords[1]; j <= this.coords[4]; j++) {
				for (int k = this.coords[2]; k <= this.coords[5]; k++) {
					left.get(this.world, leftPos, j, k);
					if (leftPos != rightPos) {
						right.get(this.world, rightPos, j, k).mirrorX().set(this.world, leftPos, j, k);
					}
					left.mirrorX().set(this.world, rightPos, j, k);
				}
			}
		}
		this.notifyBlockChanges(this.coords[0], this.coords[1], this.coords[2], this.coords[3], this.coords[4], this.coords[5]);
	}

	public void mirrorZ() {
		Blocks top = new Blocks();
		Blocks bottom = new Blocks();

		top.notifyChange = false;
		bottom.notifyChange = false;

		for (int k = 0; k <= (this.coords[5] - this.coords[2]) >> 1; k++) {
			int topPos = this.coords[2] + k;
			int bottomPos = this.coords[5] - k;
			for (int i = this.coords[0]; i <= this.coords[3]; i++) {
				for (int j = this.coords[1]; j <= this.coords[4]; j++) {
					top.get(this.world, i, j, topPos);
					if (topPos != bottomPos) {
						bottom.get(this.world, i, j, bottomPos).mirrorZ().set(this.world, i, j, topPos);
					}
					top.mirrorZ().set(this.world, i, j, bottomPos);
				}
			}
		}
		this.notifyBlockChanges(this.coords[0], this.coords[1], this.coords[2], this.coords[3], this.coords[4], this.coords[5]);
	}

	private void notifyBlockChanges(int x1, int y1, int z1, int x2, int y2, int z2) {
		for (int i = x1; i <= x2; i++) {
			for (int j = y1; j <= y2; j++) {
				for (int k = z1; k < z2; k++) {
					this.world.notifyBlockChange(i, j, k, world.getBlockId(i, j, k));
				}
			}
		}
	}

	private static int min(int a, int b) {
		if (a < b) {
			return a;
		}
		return b;
	}

	private static int max(int a, int b) {
		if (a < b) {
			return b;
		}
		return a;
	}

	public boolean sameWorld(World world) {
		return this.world == world;
	}

	public boolean equals(Area area) {
		return area.world == this.world && area.coords[0] == this.coords[0] && area.coords[1] == this.coords[1] && area.coords[2] == this.coords[2] && area.coords[3] == this.coords[3] && area.coords[4] == this.coords[4] && area.coords[5] == this.coords[5];
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Area)) {
			return false;
		}
		return this.equals((Area) o);
	}

}
