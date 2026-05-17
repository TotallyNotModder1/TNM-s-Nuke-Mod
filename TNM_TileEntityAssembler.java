package net.minecraft.src;

import TNM_AudioManager.TNM_SoundHelper;

public class TNM_TileEntityAssembler extends TileEntity implements IInventory {
	public ItemStack[] AssemblyContents = new ItemStack[19];
	public int maxuses = 1000;
	public int uses = 0;
	private	int cooldown = 0;
	public boolean IsnextTocable;
	public boolean IsnextoToReactor;

	@Override
	public int getSizeInventory() {
		return 19;
	}


	public int countAdjacentBlocks() {
		int count = 0;
	
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
			int id = worldObj.getBlockId(xCoord + o[0], yCoord + o[1], zCoord + o[2]);
			if (id != 0) { // 0 = air in Beta 1.7.3
				count++;
			}
		}
	
		return count;
	}

	public boolean isAdjacentTo(int targetID) {
		int[][] offsets = {
			{ 1, 0, 0 },  // east
			{ -1, 0, 0 }, // west
			{ 0, 1, 0 },  // up
			{ 0, -1, 0 }, // down
			{ 0, 0, 1 },  // south
			{ 0, 0, -1 }  // north
		};
	
		for (int[] o : offsets) {
			int id = worldObj.getBlockId(xCoord + o[0], yCoord + o[1], zCoord + o[2]);
			if (id == targetID) {
				return true;
			}
		}
		return false;
	}

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

	@Override
	public void updateEntity() {
		TNM_TileEntityRGenerator reactor = getAdjacentReactor();
		TNM_TileEntityHeatPipe cable = getAdjacentPipe(); 

		consumeFuel();

		++cooldown;

		if (cooldown > 200){
			cooldown = 0;
		}

		if (isAdjacentTo(Block.stoneOvenActive.blockID)){
			if (cooldown == 200){
				++uses;
			}
		}

		if (uses > maxuses){
			uses = maxuses;
		}

		if (reactor != null) {
			IsnextoToReactor = true;
			// Example: siphon energy or trigger breeding logic
			int reactorOutput = reactor.EnergyOutputPerTick;
			uses += reactorOutput;
			if (uses > maxuses) {
				uses = maxuses;
			}
		} else {
			IsnextoToReactor = false;
		}

		if (cable != null){
			IsnextoToReactor = true;
			int cableoutput = cable.GefperSecond;
			uses += cableoutput;
			if (uses > maxuses) {
				uses = maxuses;
			}
		} else {
			IsnextoToReactor = false;
		}


		if (uses > 0) {
			ItemStack blueprintSlot = AssemblyContents[16];
			ItemStack result = TNM_AssemblyRecipes.getInstance()
				.findMatchingRecipe(AssemblyContents, blueprintSlot);
	
			// Preview result in slot 15
			if (result != null) {
				AssemblyContents[15] = result.copy();
			} else {
				AssemblyContents[15] = null;
			}
	
			onInventoryChanged();
		}
	}
	
	
	public void consumeInputs() {
		for (int i = 0; i < 15; i++) { // grid slots only
			if (AssemblyContents[i] != null) {
				AssemblyContents[i].stackSize--;
				if (AssemblyContents[i].stackSize <= 0) {
					AssemblyContents[i] = null;
				}
			}
		}
		--uses;
		onInventoryChanged();

		TNM_SoundHelper.playBlockSound(worldObj, xCoord, yCoord, zCoord, "craft.wav", 1.0F);
	}

	private void consumeFuel() {
		ItemStack fuelStack = AssemblyContents[17]; // fuel slot
		if (fuelStack != null && fuelStack.itemID == mod_NukeModMain.FlaskLava.shiftedIndex || fuelStack != null && fuelStack.itemID == mod_NukeModMain.FlaskNuclearWaste.shiftedIndex) {
			ItemStack output = AssemblyContents[18];

			// consume one Lava Flask
			fuelStack.stackSize--;
			if (fuelStack.stackSize <= 0) {
				AssemblyContents[17] = null;
			}

			// add burn time
			if (fuelStack.itemID == mod_NukeModMain.FlaskLava.shiftedIndex){
				uses += 1;
				if (uses > maxuses) {
					uses = maxuses;
				}
			}

			if (fuelStack.itemID == mod_NukeModMain.FlaskNuclearWaste.shiftedIndex){
				uses += 10;
				if (uses > maxuses) {
					uses = maxuses;
				}
			}
			
			// place Empty Flask in slot 0
			if (output == null) {
				AssemblyContents[18] = new ItemStack(mod_NukeModMain.EmptyFlask, 1);
			} else if (output.itemID == mod_NukeModMain.EmptyFlask.shiftedIndex &&
				output.stackSize < output.getMaxStackSize()) {
				output.stackSize++;
			} else {
				// slot full or wrong item → drop into world
				ItemStack pleh = new ItemStack(mod_NukeModMain.EmptyFlask, 1);
				EntityItem drop = new EntityItem(worldObj, xCoord, yCoord + 1.5, zCoord, pleh);
				worldObj.entityJoinedWorld(drop);
			}
			this.onInventoryChanged();
		}
	}
	
	

	public ItemStack getStackInSlot(int var1) {
		return this.AssemblyContents[var1];
	}

	public ItemStack decrStackSize(int var1, int var2) {
		if(this.AssemblyContents[var1] != null) {
			ItemStack var3;
			if(this.AssemblyContents[var1].stackSize <= var2) {
				var3 = this.AssemblyContents[var1];
				this.AssemblyContents[var1] = null;
				this.onInventoryChanged();
				return var3;
			} else {
				var3 = this.AssemblyContents[var1].splitStack(var2);
				if(this.AssemblyContents[var1].stackSize == 0) {
					this.AssemblyContents[var1] = null;
				}

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
	
		this.AssemblyContents[slot] = stack;
		if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
			stack.stackSize = this.getInventoryStackLimit();
		}
		
		if (worldObj != null) {
            worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, id, 1);
        }
	
		this.onInventoryChanged();
	}

	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (slot < 16) return true;
		if (slot == 16) return true;
		if (slot == 17  && stack.itemID == mod_NukeModMain.FlaskLava.shiftedIndex) return true;
		if (slot == 18) return false;
		return false;
	}

	public boolean isFull() {
		for (int i = 0; i < this.getSizeInventory(); i++) {
			ItemStack stack = this.AssemblyContents[i];
			if (stack == null || !isItemValidForSlot(i, stack)) {
				return false;
			}
		}
		return true;
	}


	public String getInvName() {
		return "Assembly Table";
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		// save inventory
		NBTTagList items = new NBTTagList();
		for (int i = 0; i < this.AssemblyContents.length; ++i) {
			if (this.AssemblyContents[i] != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				this.AssemblyContents[i].writeToNBT(tag);
				items.setTag(tag);
			}
		}
		nbt.setTag("AssemblyContents", items);
		nbt.setInteger("maxuses", maxuses);
		nbt.setInteger("uses", uses);
		nbt.setInteger("cooldown", cooldown);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
	
		// restore inventory
		NBTTagList items = nbt.getTagList("AssemblyContents");
		this.AssemblyContents = new ItemStack[this.getSizeInventory()];
		for (int i = 0; i < items.tagCount(); ++i) {
			NBTTagCompound tag = (NBTTagCompound) items.tagAt(i);
			int slot = tag.getByte("Slot") & 255;
			if (slot >= 0 && slot < this.AssemblyContents.length) {
				this.AssemblyContents[slot] = new ItemStack(tag);
			}
		}
		this.maxuses = nbt.getInteger("maxuses");
		this.uses = nbt.getInteger("uses");
		this.cooldown = nbt.getInteger("cooldown");
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public boolean canInteractWith(EntityPlayer var1) {
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : var1.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}
}
