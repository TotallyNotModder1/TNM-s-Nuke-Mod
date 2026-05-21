package TNM_RecipeBookClasses;
import org.lwjgl.opengl.GL11;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.TNM_AssemblyRecipes;

public class TNM_GuiRecipes extends GuiContainer {
    private TNM_AssemblyRecipes.RecipeEntry currentRecipe;
    private TNM_ContainerRecipeBook container;

    public TNM_GuiRecipes() {
        super(new TNM_ContainerRecipeBook());
        this.container = (TNM_ContainerRecipeBook) this.inventorySlots;
        this.xSize = 176;
        this.ySize = 222;
        // start with first recipe
        currentRecipe = TNM_AssemblyRecipes.getInstance().getRecipeList().get(0);
        container.setRecipe(currentRecipe);
    }

    private void updateButtonStates() {
        int idx = TNM_AssemblyRecipes.getInstance().getRecipeList().indexOf(currentRecipe);
        // disable prev if at first recipe
        ((GuiButton)this.controlList.get(1)).enabled = idx > 0;
        // disable next if at last recipe
        ((GuiButton)this.controlList.get(0)).enabled = idx < TNM_AssemblyRecipes.getInstance().getRecipeList().size() - 1;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) { // next
            // cycle to next recipe
            int idx = TNM_AssemblyRecipes.getInstance().getRecipeList().indexOf(currentRecipe);
            idx = (idx + 1) % TNM_AssemblyRecipes.getInstance().getRecipeList().size();
            currentRecipe = TNM_AssemblyRecipes.getInstance().getRecipeList().get(idx);
            container.setRecipe(currentRecipe);
        }

        if (button.id == 1) { // prev
            int idx = TNM_AssemblyRecipes.getInstance().getRecipeList().indexOf(currentRecipe);
            if (idx > 0) {
                idx = idx - 1;
                currentRecipe = TNM_AssemblyRecipes.getInstance().getRecipeList().get(idx);
                container.setRecipe(currentRecipe);
            }
        }


        updateButtonStates();
    }


    @Override
    public void initGui() {
        super.initGui();
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        // Add a "Next" button at bottom right
        this.controlList.add(new GuiButton(0, x + 120, y + 190, 40, 20, "Next"));
        this.controlList.add(new GuiButton(1, x + 6, y + 190, 40, 20, "Prev"));

        updateButtonStates();
    }

    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        GL11.glColor4f(1F, 1F, 1F, 1F);
        int tex = mc.renderEngine.getTexture("/NukeTex/assemblytablepage.png");
        mc.renderEngine.bindTexture(tex);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}


