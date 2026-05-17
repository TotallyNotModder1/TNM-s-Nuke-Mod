package net.minecraft.src;

import java.util.HashSet;
import java.util.Set;

public class TNM_TileEntityHeatPipe extends TileEntity{
	int GefperSecond;
	int ticksexisted = 0;

	public TNM_TileEntityRGenerator getAdjacentReactor() {
		// Offsets for the 6 directions
		int[][] offsets = {
			{ 1, 0, 0 },  // east
			{ -1, 0, 0 }, // west
			{ 0, 1, 0 },  // up
			{ 0, -1, 0 }, // down
			{ 0, 0, 1 },  // south
			{ 0, 0, -1 }  // north
		};
	
		for (int[] o : offsets) {
			TileEntity te = worldObj.getBlockTileEntity(
				xCoord + o[0],
				yCoord + o[1],
				zCoord + o[2]
			);
			if (te instanceof TNM_TileEntityRGenerator) {
				return (TNM_TileEntityRGenerator) te;
			}

		}
		return null; // no reactor found
	}

	public TNM_TileEntityHeatPipe getAdjacentPipe() {
		// Offsets for the 6 directions
		int[][] offsets = {
			{ 1, 0, 0 },  // east
			{ -1, 0, 0 }, // west
			{ 0, 1, 0 },  // up
			{ 0, -1, 0 }, // down
			{ 0, 0, 1 },  // south
			{ 0, 0, -1 }  // north
		};
	
		for (int[] o : offsets) {
			TileEntity te = worldObj.getBlockTileEntity(
				xCoord + o[0],
				yCoord + o[1],
				zCoord + o[2]
			);
			if (te instanceof TNM_TileEntityHeatPipe) {
				return (TNM_TileEntityHeatPipe) te;
			}

		}
		return null; // no reactor found
	}

	public TNM_TileEntityCentrifuge getAdjecentCentrifuge() {
		// Offsets for the 6 directions
		int[][] offsets = {
			{ 1, 0, 0 },  // east
			{ -1, 0, 0 }, // west
			{ 0, 1, 0 },  // up
			{ 0, -1, 0 }, // down
			{ 0, 0, 1 },  // south
			{ 0, 0, -1 }  // north
		};
	
		for (int[] o : offsets) {
			TileEntity te = worldObj.getBlockTileEntity(
				xCoord + o[0],
				yCoord + o[1],
				zCoord + o[2]
			);
			if (te instanceof TNM_TileEntityCentrifuge) {
				return (TNM_TileEntityCentrifuge) te;
			}

		}
		return null; // no reactor found
	}

	public int getPowerFromChain(Set<String> visited) {
		String key = xCoord + "," + yCoord + "," + zCoord;
		if (visited.contains(key)) {
			return 0; // stop recursion if already visited
		}
		visited.add(key);
	
		int totalPower = 0;
	
		int[][] offsets = {
			{ 1, 0, 0 }, { -1, 0, 0 },
			{ 0, 1, 0 }, { 0, -1, 0 },
			{ 0, 0, 1 }, { 0, 0, -1 }
		};
	
		for (int[] o : offsets) {
			TileEntity te = worldObj.getBlockTileEntity(
				xCoord + o[0],
				yCoord + o[1],
				zCoord + o[2]
			);
	
			if (te instanceof TNM_TileEntityRGenerator) {
				totalPower += ((TNM_TileEntityRGenerator) te).EnergyOutputPerTick;
			} else if (te instanceof TNM_TileEntityHeatPipe) {
				totalPower += ((TNM_TileEntityHeatPipe) te).getPowerFromChain(visited);
			}
		}
	
		return totalPower;
	}


	@Override
	public void updateEntity() {
		ticksexisted++;

		if (ticksexisted == 5) {
			int thisblockid = worldObj.getBlockId(xCoord, yCoord, zCoord);
			worldObj.markBlockAsNeedsUpdate(xCoord, yCoord, zCoord);
			worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, thisblockid);
		}

		// Start recursion with a fresh visited set
		this.GefperSecond = getPowerFromChain(new HashSet<String>());
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
	
		// restore burntime and progress
		this.GefperSecond = nbt.getInteger("GefperSecond");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("GefperSecond", this.GefperSecond);
	}


	public int getInventoryStackLimit() {
		return 64;
	}

	public boolean canInteractWith(EntityPlayer var1) {
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : var1.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}
}
