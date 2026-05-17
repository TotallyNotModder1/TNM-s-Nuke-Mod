package net.minecraft.src;

import java.util.*;

public class TNM_AssemblyRecipes {
    private static final TNM_AssemblyRecipes instance = new TNM_AssemblyRecipes();
    private List<RecipeEntry> recipes = new ArrayList<RecipeEntry>();

    public static TNM_AssemblyRecipes getInstance() {
        return instance;
    }

    private TNM_AssemblyRecipes() {}

    // Inner recipe entry
    private static class RecipeEntry {
        ItemStack output;
        String[] shape; // 5 rows × 3 cols
        Map<Character, ItemStack> mapping;
        ItemStack blueprint;        // may be null
        boolean requiresBlueprint;  // true if blueprint is mandatory

        RecipeEntry(ItemStack output, String[] shape, Map<Character, ItemStack> mapping,
                    ItemStack blueprint, boolean requiresBlueprint) {
            this.output = output;
            this.shape = shape;
            this.mapping = mapping;
            this.blueprint = blueprint;
            this.requiresBlueprint = requiresBlueprint;
        }

        boolean matches(ItemStack[] grid, ItemStack blueprintSlot) {
            if (requiresBlueprint) {
                if (blueprintSlot == null || blueprint == null) return false;
                if (blueprintSlot.itemID != blueprint.itemID) return false;
            }
        
            int gridRows = 15; // assembler grid height
            int gridCols = 3;  // assembler grid width
            int recipeRows = shape.length;
            int recipeCols = shape[0].length();
            
            // Only slide where the recipe fits
            for (int offsetRow = 0; offsetRow <= gridRows - recipeRows; offsetRow++) {
                for (int offsetCol = 0; offsetCol <= gridCols - recipeCols; offsetCol++) {
                    boolean matchedHere = true;
            
                    for (int row = 0; row < recipeRows; row++) {
                        String line = shape[row];
                        for (int col = 0; col < recipeCols; col++) {
                            int index = (col + offsetCol) + (row + offsetRow) * gridCols;
                            if (index < 0 || index >= grid.length) { matchedHere = false; break; }
            
                            char c = line.charAt(col);
                            ItemStack required = mapping.getOrDefault(c, null);
                            ItemStack inSlot = grid[index];
            
                            if (required == null && inSlot == null) continue;
                            if (required == null || inSlot == null) { matchedHere = false; break; }
                            if (inSlot.itemID != required.itemID) { matchedHere = false; break; }
                        }
                        if (!matchedHere) break;
                    }
            
                    if (matchedHere) return true;
                }
            }
            return false;
        }            
    }

    // Add recipe with optional blueprint
    public void addRecipe(ItemStack output, ItemStack blueprint,
                          boolean requiresBlueprint, String[] shape, Object... keys) {
        Map<Character, ItemStack> mapping = new HashMap<Character, ItemStack>();
        for (int i = 0; i < keys.length; i += 2) {
            char symbol = (Character) keys[i];
            Object obj = keys[i + 1];
            if (obj instanceof Item) mapping.put(symbol, new ItemStack((Item)obj));
            else if (obj instanceof Block) mapping.put(symbol, new ItemStack((Block)obj));
            else if (obj instanceof ItemStack) mapping.put(symbol, (ItemStack)obj);
        }
        recipes.add(new RecipeEntry(output, shape, mapping, blueprint, requiresBlueprint));
    }

    public ItemStack findMatchingRecipe(ItemStack[] grid, ItemStack blueprintSlot) {
        for (RecipeEntry recipe : recipes) {
            if (recipe.matches(grid, blueprintSlot)) {
                return recipe.output.copy();
            }
        }
        return null;
    }
}
