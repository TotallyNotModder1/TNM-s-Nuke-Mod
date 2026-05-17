package net.minecraft.src;

public class TNM_TileEntityWarhead extends TileEntity implements IInventory {
	private ItemStack[] NukeContents = new ItemStack[3];

	@Override
	public int getSizeInventory() {
		return 3;
	}

	public ItemStack getStackInSlot(int var1) {
		return this.NukeContents[var1];
	}

	public ItemStack decrStackSize(int var1, int var2) {
		int id = worldObj.getBlockId(xCoord, yCoord, zCoord);
		if(this.NukeContents[var1] != null) {
			ItemStack var3;
			if(this.NukeContents[var1].stackSize <= var2) {
				var3 = this.NukeContents[var1];
				this.NukeContents[var1] = null;
				this.onInventoryChanged();
				worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, id, 1);
				return var3;
			} else {
				var3 = this.NukeContents[var1].splitStack(var2);
				if(this.NukeContents[var1].stackSize == 0) {
					this.NukeContents[var1] = null;
				}
				
				worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, id, 1);
				this.onInventoryChanged();
				return var3;
			}
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		int id = worldObj.getBlockId(xCoord, yCoord, zCoord);
		if (stack != null && !isItemValidForSlot(slot, stack)) {
			// Reject invalid items
			return;
		}
	
		this.NukeContents[slot] = stack;
		if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
			stack.stackSize = this.getInventoryStackLimit();
		}
		
		if (worldObj != null) {
            worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, id, 1);
        }
	
		this.onInventoryChanged();
	}

	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (stack == null) return false;
	
		// Example: slot 0 only accepts "NukeCore"
		if (slot == 0 && stack.itemID == mod_NukeModMain.NPropellant.shiftedIndex) return true;
	
		// Example: slot 1 only accepts "NukeCasing"
		if (slot == 1 && stack.itemID == mod_NukeModMain.NukeBullet.shiftedIndex) return true;
	
		// Example: slot 2 only accepts "NukeTrigger"
		if (slot == 2 && stack.itemID == mod_NukeModMain.NukeTarget.shiftedIndex) return true;
	
		return false;
	}

	public boolean isFull() {
		for (int i = 0; i < this.getSizeInventory(); i++) {
			ItemStack stack = this.NukeContents[i];
			if (stack == null || !isItemValidForSlot(i, stack)) {
				return false;
			}
		}
		return true;
	}


	public String getInvName() {
		return "";
	}

	public void readFromNBT(NBTTagCompound var1) {
		super.readFromNBT(var1);
		NBTTagList var2 = var1.getTagList("Parts");
		this.NukeContents = new ItemStack[this.getSizeInventory()];

		for(int var3 = 0; var3 < var2.tagCount(); ++var3) {
			NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
			int var5 = var4.getByte("Slot") & 255;
			if(var5 >= 0 && var5 < this.NukeContents.length) {
				this.NukeContents[var5] = new ItemStack(var4);
			}
		}

	}

	public void writeToNBT(NBTTagCompound var1) {
		super.writeToNBT(var1);
		NBTTagList var2 = new NBTTagList();

		for(int var3 = 0; var3 < this.NukeContents.length; ++var3) {
			if(this.NukeContents[var3] != null) {
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte)var3);
				this.NukeContents[var3].writeToNBT(var4);
				var2.setTag(var4);
			}
		}

		var1.setTag("Parts", var2);
	}

	public int getInventoryStackLimit() {
		return 1;
	}

	public boolean canInteractWith(EntityPlayer var1) {
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : var1.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}
}
