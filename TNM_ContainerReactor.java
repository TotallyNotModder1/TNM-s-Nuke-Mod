package net.minecraft.src;

public class TNM_ContainerReactor extends Container {
	private TNM_TileEntityRGenerator Reactor;

    public TNM_ContainerReactor(InventoryPlayer playerInv, TNM_TileEntityRGenerator reactor) {
        this.Reactor = reactor;

        // Reactor slots//
        // 3x3 reactor grid, ignoring center
        int startX = 62;  // leftmost x position
        int startY = 23;  // top y position
        int slotIndex = 0;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                // skip the center (row=1, col=1)
                if (row == 1 && col == 1) continue;

                int x = startX + col * 18;
                int y = startY + row * 18;
                this.addSlot(new TNM_SlotReactor(Reactor, slotIndex++, x, y));
            }
        }

        this.addSlot(new TNM_SlotReactor(Reactor, 9, 45, 41));
        this.addSlot(new TNM_SlotReactor(Reactor, 10, 115, 41));
        this.addSlot(new TNM_SlotReactor(Reactor, 11, 80, 6));
        this.addSlot(new TNM_SlotReactor(Reactor, 12, 80, 76));
        //-------------//
        // input output slots//
        this.addSlot(new TNM_SlotReactor(Reactor, 13, 80, 41)); //input
        this.addSlot(new TNM_SlotReactor(Reactor, 14, 8, 68)); //output
        //waste output//
        this.addSlot(new TNM_SlotReactor(Reactor, 15, 133, 8)); //waste rods
        this.addSlot(new TNM_SlotReactor(Reactor, 16, 133, 738)); //flasks

        //inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(
                    new Slot(playerInv,
                        col + row * 9 + 9,      // inventory index
                        8 + col * 18,           // x‑position
                        99 + row * 18));        // y‑position
            }
        }        
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInv, col, 8 + col * 18, 157));
        }
    }

    public boolean isUsableByPlayer(EntityPlayer var1) {
        return this.Reactor.canInteractWith(var1);
    }
}
