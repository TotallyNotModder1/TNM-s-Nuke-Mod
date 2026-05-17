package net.minecraft.src;

public class TNM_ContainerAssembler extends Container {
    private TNM_TileEntityAssembler tile;

    public TNM_ContainerAssembler(InventoryPlayer playerInv, TNM_TileEntityAssembler tile) {
        this.tile = tile;

        // --- Crafting grid slots (0–14) ---
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 3; col++) {
                this.addSlot(new Slot(tile, col + row * 3,
                        26 + col * 18, 17 + row * 18));
            }
        }

        // --- Output slot (15) ---
        this.addSlot(new TNM_SlotAssembler(playerInv.player, tile, 15, 120, 35));

        // --- Blueprint slot (16) ---
        this.addSlot(new Slot(tile, 16, 82, 89)); // adjust coords to fit your GUI

        // -- Flask Input (17)
        this.addSlot(new Slot(tile, 17, 59, 111));

        // -- Flask Output (18)
        this.addSlot(new Slot(tile, 18, 28, 111));

        // --- Player inventory (3 rows × 9 cols) ---
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInv, col + row * 9 + 9,
                        8 + col * 18, 140 + row * 18));
            }
        }

        // --- Hotbar (9 slots) ---
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInv, col,
                    8 + col * 18, 198));
        }
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return tile.canInteractWith(player);
    }
}
