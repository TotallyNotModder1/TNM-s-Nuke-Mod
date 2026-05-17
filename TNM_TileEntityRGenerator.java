package net.minecraft.src;

public class TNM_TileEntityRGenerator extends TileEntity implements IInventory {
	private ItemStack[] GeneratorContents = new ItemStack[17];
	public int EnergyOutputPerTick = 0;;
	public int decaytime = 0;
	public int Enrichmentprogress = 0;
	public int maxenrichment = 1000;
	public boolean IsSeized = false;

	@Override
	public int getSizeInventory() {
		return 17;
	}
	// Reactor logic//
	@Override
	public void updateEntity() {
		++decaytime;
	
		///////Fuel Logic///////
		if (decaytime >= 200){
			worldObj.markBlockAsNeedsUpdate(xCoord, yCoord, zCoord);
			int thisblockid = worldObj.getBlockId(xCoord, yCoord, zCoord);
			worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, thisblockid);
			consumeFuel();
			decaytime = 0;
		}
		
		///////Breeding Logic///////

		if (GeneratorContents[13] != null && hasFuel()) {
			++Enrichmentprogress;
			int increment = 1; // default normal speed

			if (EnergyOutputPerTick < 200) {
				increment = 1; // normal
			} else if (EnergyOutputPerTick < 500) {
				increment = 5; // fast
			} else if (EnergyOutputPerTick < 1000) {
				increment = 10; // fastest
			} else if (EnergyOutputPerTick < 2000){
				increment = 15; // cap at fastest
			} else {
				increment = 15;
			}
		
			Enrichmentprogress += increment;
		
			if (Enrichmentprogress >= maxenrichment) {
				runRecipe();
				Enrichmentprogress = 0;
			}
			if (Enrichmentprogress >= maxenrichment) {
				runRecipe();
				Enrichmentprogress = 0;
			}
		} else if (GeneratorContents[13] == null || !hasFuel()){
			IsSeized = false;
			Enrichmentprogress = 0;
		}
	
	}

	private boolean hasFuel() {
		for (int i = 0; i < 13; i++) {
			if (GeneratorContents[i] != null) {
				return true;
			}
		}
		return false;
	}

	private void addEmptyFlask() {
		ItemStack wasteSlot = GeneratorContents[15]; // use slot 15 for waste
		if (wasteSlot == null) {
			GeneratorContents[15] = new ItemStack(mod_NukeModMain.EmptyFlask, 1);
		} else if (wasteSlot.itemID == mod_NukeModMain.EmptyFlask.shiftedIndex && wasteSlot.stackSize < wasteSlot.getMaxStackSize()) {
			wasteSlot.stackSize++;
		} else {
			ItemStack drop = new ItemStack(mod_NukeModMain.EmptyFlask, 1);
			EntityItem entityDrop = new EntityItem(worldObj, xCoord, yCoord + 1.5, zCoord, drop);
			worldObj.entityJoinedWorld(entityDrop);
		}
	}
	
	
	
	private void consumeFuel() {

		EnergyOutputPerTick = 0;
		
		for (int i = 0; i < 13; i++) {
			ItemStack fuelStack = GeneratorContents[i];
			if (fuelStack == null) continue;
	
			int id = fuelStack.itemID;
	
			// Energy contribution
			if (id == mod_NukeModMain.FlaskNuclearWaste.shiftedIndex) {
				EnergyOutputPerTick += 50;
				fuelStack.damageItem(1, null);
				if (fuelStack.stackSize <= 0 || fuelStack.getItemDamage() >= fuelStack.getMaxDamage()) {
					GeneratorContents[i] = null;
					addEmptyFlask();
				}
			}
			if (id == mod_NukeModMain.HeavyRod.shiftedIndex) {
				EnergyOutputPerTick += 5 * fuelStack.stackSize;;
				--fuelStack.stackSize;
				if (fuelStack.stackSize <= 0) {
					GeneratorContents[i] = null;
				}
			}
		}
	
		this.onInventoryChanged();
	}
	

	private void runRecipe() {
		ItemStack input = GeneratorContents[13];
		ItemStack output = GeneratorContents[14];
	
		if (input == null) return;
	
		// --- Enriched Redstone → Plutonium ---
		if (input.itemID == mod_NukeModMain.EnrichedRedstone.shiftedIndex) {
			// Only proceed if output is empty or already Plutonium
			if (output == null || output.itemID == mod_NukeModMain.Plutonium.shiftedIndex) {
				input.stackSize--;
				IsSeized = false;
				if (input.stackSize <= 0) GeneratorContents[13] = null;
	
				if (output == null) {
					GeneratorContents[14] = new ItemStack(mod_NukeModMain.Plutonium, 1);
				} else {
					output.stackSize = Math.min(output.stackSize + 1, output.getMaxStackSize());
				}
			} else {
				// Seize up: do not consume input, just stop
				IsSeized = true;
				return;
			}
		}
	
		// --- Nuclear Waste (full stack) → Uranium ---
		else if (input.itemID == mod_NukeModMain.FlaskNuclearWaste.shiftedIndex) {
			if (output == null || output.itemID == mod_NukeModMain.Uranium.shiftedIndex) {
				GeneratorContents[13] = null;
				IsSeized = false;
					
				if (output == null) {
					GeneratorContents[14] = new ItemStack(mod_NukeModMain.Uranium, 1);
				} else {
					output.stackSize = Math.min(output.stackSize + 1, output.getMaxStackSize());
				}
			} else {
				IsSeized = true;
				return; // seize up
			}
		}
	
		// --- Water Flask → Deuterium ---
		else if (input.itemID == mod_NukeModMain.FlaskWater.shiftedIndex) {
			if (output == null || output.itemID == mod_NukeModMain.FlaskDeuterium.shiftedIndex) {
				input.stackSize--;
				IsSeized = false;
				if (input.stackSize <= 0) GeneratorContents[13] = null;
	
				if (output == null) {
					GeneratorContents[14] = new ItemStack(mod_NukeModMain.FlaskDeuterium, 1);
				} else {
					output.stackSize = Math.min(output.stackSize + 1, output.getMaxStackSize());
				}
			} else {
				IsSeized = true;
				return; // seize up
			}
		}
	}
	
	
	

	// slot logic//
	public ItemStack getStackInSlot(int var1) {
		return this.GeneratorContents[var1];
	}

	public ItemStack decrStackSize(int var1, int var2) {
		int id = worldObj.getBlockId(xCoord, yCoord, zCoord);
		if(this.GeneratorContents[var1] != null) {
			ItemStack var3;
			if(this.GeneratorContents[var1].stackSize <= var2) {
				var3 = this.GeneratorContents[var1];
				this.GeneratorContents[var1] = null;
				this.onInventoryChanged();
				worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, id, 1);
				return var3;
			} else {
				var3 = this.GeneratorContents[var1].splitStack(var2);
				if(this.GeneratorContents[var1].stackSize == 0) {
					this.GeneratorContents[var1] = null;
				}
				
				worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, id, 1);
				this.onInventoryChanged();
				return var3;
			}
		} else {
			return null;
		}
	}

	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (stack == null) return false;

		if (slot < 13 && stack.itemID == mod_NukeModMain.FlaskNuclearWaste.shiftedIndex);
		if (slot == 13) return true;
		if (slot == 14) return false;
		if (slot == 15) return false;
		if (slot == 16) return false;

		return false;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		int id = worldObj.getBlockId(xCoord, yCoord, zCoord);
		if (stack != null && !isItemValidForSlot(slot, stack)) {
			// Reject invalid items
			return;
		}
	
		this.GeneratorContents[slot] = stack;
		if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
			stack.stackSize = this.getInventoryStackLimit();
		}
		
		if (worldObj != null) {
            worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, id, 1);
        }
	
		this.onInventoryChanged();
	}

	public boolean isFull() {
		for (int i = 0; i < this.getSizeInventory(); i++) {
			ItemStack stack = this.GeneratorContents[i];
			if (stack == null || !isItemValidForSlot(i, stack)) {
				return false;
			}
		}
		return true;
	}


	public String getInvName() {
		return "";
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
	
		// save inventory
		NBTTagList items = new NBTTagList();
		for (int i = 0; i < this.GeneratorContents.length; ++i) {
			if (this.GeneratorContents[i] != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				this.GeneratorContents[i].writeToNBT(tag);
				items.setTag(tag);
			}
		}
		nbt.setTag("Rods", items);
	
		// save extra fields
		nbt.setInteger("EnergyOutputPerTick", this.EnergyOutputPerTick);
		nbt.setInteger("decaytime", this.decaytime);
		nbt.setInteger("Enrichmentprogress", this.Enrichmentprogress);
		nbt.setBoolean("IsSeized", this.IsSeized);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
	
		// restore inventory
		NBTTagList items = nbt.getTagList("Rods");
		this.GeneratorContents = new ItemStack[this.getSizeInventory()];
		for (int i = 0; i < items.tagCount(); ++i) {
			NBTTagCompound tag = (NBTTagCompound) items.tagAt(i);
			int slot = tag.getByte("Slot") & 255;
			if (slot >= 0 && slot < this.GeneratorContents.length) {
				this.GeneratorContents[slot] = new ItemStack(tag);
			}
		}
	
		// restore extra fields
		this.EnergyOutputPerTick = nbt.getInteger("EnergyOutputPerTick");
		this.decaytime = nbt.getInteger("decaytime");
		this.Enrichmentprogress = nbt.getInteger("Enrichmentprogress");
		this.IsSeized = nbt.getBoolean("IsSeized");
	}
	

	public int getInventoryStackLimit() {
		return 64;
	}

	public boolean canInteractWith(EntityPlayer var1) {
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : var1.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}
}
