package net.minecraft.src;

import net.minecraft.src.TNM_CentrifugeRecipes.CentrifugeRecipe;

public class TNM_TileEntityCentrifuge extends TileEntity implements IInventory {
	public ItemStack[] CentrifugeContents = new ItemStack[7];
	public int burntime = 0;
	public int maxburntime = 12800;
	public int centrifugeCookTime = 0;   // progress toward crafting
	public int centrifugeCookTimeTotal = 150; // ticks required per recipe
	public boolean isrunning;
	public boolean IsnextoToReactor;
	public boolean IsnextoTocable;
	public int inferiorheatTimer = 0;
	public int decay = 200;

	public int poweredCounter = 0;

	@Override
	public int getSizeInventory() {
		return 7;
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
		boolean powered = worldObj.isBlockGettingPowered(xCoord, yCoord, zCoord)
		||worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
		//pickup heat logic
		TNM_TileEntityRGenerator reactor = getAdjacentReactor();
		TNM_TileEntityHeatPipe cable = getAdjacentPipe();

		consumeFuel();

		if (reactor != null) {
			IsnextoToReactor = true;
			// Example: siphon energy or trigger breeding logic
			int reactorOutput = reactor.EnergyOutputPerTick;
			burntime += reactorOutput;
			if (burntime > maxburntime) {
				burntime = maxburntime;
			}
		} else {
			IsnextoToReactor = false;
		}

		if (cable != null){
			IsnextoToReactor = true;
			int cableoutput = cable.GefperSecond;
			burntime += cableoutput;
			if (burntime > maxburntime) {
				burntime = maxburntime;
			}
		} else {
			IsnextoToReactor = false;
		}

		if (!IsnextoToReactor || !IsnextoTocable){
			if (isAdjacentTo(Block.stoneOvenActive.blockID) || isAdjacentTo(mod_NukeModMain.Trinitite.blockID) ||
			isAdjacentTo(Block.fire.blockID) || isAdjacentTo(Block.lavaStill.blockID)
			){
				inferiorheatTimer++;
				if (inferiorheatTimer >= 100){
					this.burntime += 50;
					inferiorheatTimer = 0;
				}
			}

			--decay;
			if (decay <= 0) {
				burntime -= 20;
				decay = 40;
				if (burntime <= 0) {
					burntime = 0;
					decay = 200; // reset only when empty
				}
			}
		}

		//internal logic
		if (powered){
			// increment counter while powered
			poweredCounter++;
			worldObj.markBlockAsNeedsUpdate(xCoord, yCoord, zCoord);

			if (decay == 4){
				for (int b = 0; b < 3; b++) {
					worldObj.spawnParticle("smoke", xCoord + 0.5, yCoord + 1.2, zCoord + 0.5, 0.0D, 0.05D, 0.0D);
				}
			}
			
			if (poweredCounter >= 0){
				if (this.poweredCounter > 0){
					isrunning = true;
				}
			}
			
			if (burntime <= 0){
				isrunning = false;
				worldObj.markBlockAsNeedsUpdate(xCoord, yCoord, zCoord);
			}
			
			if (isrunning == true) {
				if (burntime > 0) {
					--burntime;
					
				}
				
	
				if (CentrifugeContents[4] != null && CentrifugeContents[4].stackSize >= 64) {
					isrunning = false;
				}
	
				if (isrunning == true || burntime > 0) {
					CentrifugeRecipe recipe = TNM_CentrifugeRecipes.centrifuge()
						.getCentrifugeResult(
							CentrifugeContents[2] != null ? CentrifugeContents[2].itemID : -1
						);
			
						if (recipe != null && canProcess(recipe)) {
							centrifugeCookTime++;
							if (centrifugeCookTime >= centrifugeCookTimeTotal) {
								centrifugeCookTime = 0;
								processRecipe(recipe);
							}
						} else {
							// Pause instead of reset
							// Only reset if input is gone
							if (CentrifugeContents[2] == null) {
								centrifugeCookTime = 0;
								isrunning = false;
							}
						}
				} else {
					centrifugeCookTime = 0;
					isrunning = false;
				}
	
			}
		} else {
			worldObj.markBlockAsNeedsUpdate(xCoord, yCoord, zCoord);
		}

		if (!powered && CentrifugeContents[2] == null){
			centrifugeCookTime = 0;
		}
	}
	
	private void consumeFuel() {
		ItemStack fuelStack = CentrifugeContents[1]; // fuel slot
		if (fuelStack != null && fuelStack.itemID == mod_NukeModMain.FlaskLava.shiftedIndex || fuelStack != null && fuelStack.itemID == mod_NukeModMain.FlaskNuclearWaste.shiftedIndex) {
			ItemStack output = CentrifugeContents[0];

			// consume one Lava Flask
			fuelStack.stackSize--;
			if (fuelStack.stackSize <= 0) {
				CentrifugeContents[1] = null;
			}

			// add burn time
			if (fuelStack.itemID == mod_NukeModMain.FlaskLava.shiftedIndex){
				burntime += 80;
				if (burntime > maxburntime) {
					burntime = maxburntime;
				}
			}

			if (fuelStack.itemID == mod_NukeModMain.FlaskNuclearWaste.shiftedIndex){
				burntime += 50;
				if (burntime > maxburntime) {
					burntime = maxburntime;
				}
			}
			
			// place Empty Flask in slot 0
			if (output == null) {
				CentrifugeContents[0] = new ItemStack(mod_NukeModMain.EmptyFlask, 1);
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

	private boolean canProcess(CentrifugeRecipe recipe) {
		if (CentrifugeContents[2] == null) return false; // no input
	
		ItemStack guaranteed = recipe.guaranteed;
		ItemStack randomOut1 = recipe.getRandomOutputForSlot5(worldObj.rand);
		ItemStack randomOut2 = recipe.getRandomOutputForSlot6(worldObj.rand);
	
		return canAcceptOutput(4, guaranteed) &&
		(randomOut1 == null || canAcceptOutput(5, randomOut1)) &&
		(randomOut2 == null || canAcceptOutput(6, randomOut2));
	}

	private boolean canAcceptOutput(int slot, ItemStack stack) {
		ItemStack current = CentrifugeContents[slot];
		if (current == null) return true;
		if (current.itemID != stack.itemID) return false;
		return current.stackSize + stack.stackSize <= current.getMaxStackSize();
	}

	private void processRecipe(CentrifugeRecipe recipe) {
		// consume input
		CentrifugeContents[2].stackSize--;
		if (CentrifugeContents[2].stackSize <= 0) {
			CentrifugeContents[2] = null;
		}
	
		// guaranteed → slot 4
		addToSlot(4, recipe.guaranteed.copy());
	
		// random pool for slot 5
		ItemStack randomOut5 = recipe.getRandomOutputForSlot5(worldObj.rand);
		if (randomOut5 != null && canAcceptOutput(5, randomOut5) && CentrifugeContents[3] != null) {
			CentrifugeContents[3].stackSize--;
			if (CentrifugeContents[3].stackSize <= 0){
				CentrifugeContents[3] = null;
			}
			addToSlot(5, randomOut5);
		}
	
		// random pool for slot 6
		ItemStack randomOut6 = recipe.getRandomOutputForSlot6(worldObj.rand);
		int roll = worldObj.rand.nextInt(100);
		if (canAcceptOutput(6, randomOut6)) {
			if (recipe.guaranteed.copy().itemID == mod_NukeModMain.EnrichedRedstone.shiftedIndex){
				if (roll == 1){
					addToSlot(6, randomOut6);
				}
			} else {
				addToSlot(6, randomOut6);
			}
		}
	
		this.onInventoryChanged();
	}
	
	

	private void addToSlot(int slot, ItemStack stack) {
		if (CentrifugeContents[slot] == null) {
			CentrifugeContents[slot] = stack;
		} else {
			int newSize = CentrifugeContents[slot].stackSize + stack.stackSize;
			int max = CentrifugeContents[slot].getMaxStackSize();
	
			if (newSize > max) {
				// Fill the slot to max
				CentrifugeContents[slot].stackSize = max;
	
				// Drop the overflow into the world
				int overflow = newSize - max;
				if (overflow > 0) {
					ItemStack extra = new ItemStack(stack.itemID, overflow, stack.getItemDamage());
					worldObj.entityJoinedWorld(
						new EntityItem(worldObj, xCoord, yCoord + 1.5, zCoord, extra)
					);
				}
			} else {
				// Safe to just increase stack size
				CentrifugeContents[slot].stackSize = newSize;
			}
		}
	}
	
	

	public ItemStack getStackInSlot(int var1) {
		return this.CentrifugeContents[var1];
	}

	public ItemStack decrStackSize(int var1, int var2) {
		int id = worldObj.getBlockId(xCoord, yCoord, zCoord);
		if(this.CentrifugeContents[var1] != null) {
			ItemStack var3;
			if(this.CentrifugeContents[var1].stackSize <= var2) {
				var3 = this.CentrifugeContents[var1];
				this.CentrifugeContents[var1] = null;
				this.onInventoryChanged();
				worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, id, 1);
				return var3;
			} else {
				var3 = this.CentrifugeContents[var1].splitStack(var2);
				if(this.CentrifugeContents[var1].stackSize == 0) {
					this.CentrifugeContents[var1] = null;
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
	
		this.CentrifugeContents[slot] = stack;
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
	
		//Input
		if (slot == 2) return true;
		if (slot == 1 && stack.itemID == mod_NukeModMain.FlaskLava.shiftedIndex || slot == 1 && stack.itemID == mod_NukeModMain.FlaskNuclearWaste.shiftedIndex) return true;
		if (slot == 3 && stack.itemID == mod_NukeModMain.EmptyFlask.shiftedIndex) return true;

		//output

		if (slot == 4) return false;
		if (slot == 5) return false;
		if (slot == 6) return false;
	
		return false;
	}

	public boolean isFull() {
		for (int i = 0; i < this.getSizeInventory(); i++) {
			ItemStack stack = this.CentrifugeContents[i];
			if (stack == null || !isItemValidForSlot(i, stack)) {
				return false;
			}
		}
		return true;
	}


	public String getInvName() {
		return "Centrifuge";
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
	
		// save burntime and progress
		nbt.setInteger("BurnTime", this.burntime);
		nbt.setInteger("PoweredCounter", this.poweredCounter);
	
		// save inventory
		NBTTagList items = new NBTTagList();
		for (int i = 0; i < this.CentrifugeContents.length; ++i) {
			if (this.CentrifugeContents[i] != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				this.CentrifugeContents[i].writeToNBT(tag);
				items.setTag(tag);
			}
		}
		nbt.setTag("CentrifugeContents", items);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
	
		// restore burntime and progress
		this.burntime = nbt.getInteger("BurnTime");
		this.poweredCounter = nbt.getInteger("PoweredCounter");
	
		// restore inventory
		NBTTagList items = nbt.getTagList("CentrifugeContents");
		this.CentrifugeContents = new ItemStack[this.getSizeInventory()];
		for (int i = 0; i < items.tagCount(); ++i) {
			NBTTagCompound tag = (NBTTagCompound) items.tagAt(i);
			int slot = tag.getByte("Slot") & 255;
			if (slot >= 0 && slot < this.CentrifugeContents.length) {
				this.CentrifugeContents[slot] = new ItemStack(tag);
			}
		}
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public boolean canInteractWith(EntityPlayer var1) {
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : var1.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}
}
