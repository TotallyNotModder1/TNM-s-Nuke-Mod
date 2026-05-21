package net.minecraft.src;

import java.util.*;

public class TNM_CentrifugeRecipes {
    public static final TNM_CentrifugeRecipes centrifugeBase = new TNM_CentrifugeRecipes();
    public Map<Integer, CentrifugeRecipe> recipeList = new HashMap<Integer, CentrifugeRecipe>();

    public static TNM_CentrifugeRecipes centrifuge() {
        return centrifugeBase;
    }

    public TNM_CentrifugeRecipes() {}

    // Register recipe with guaranteed + weighted pools for slot 5 and slot 6
    public void addCentrifuge(int inputID,
        ItemStack guaranteed,
        WeightedOutput[] poolSlot5,
        WeightedOutput[] poolSlot6) {
        recipeList.put(Integer.valueOf(inputID),
            new CentrifugeRecipe(guaranteed, poolSlot5, poolSlot6));
    }

    public CentrifugeRecipe getCentrifugeResult(int inputID) {
        return recipeList.get(Integer.valueOf(inputID));
    }

    public Map<Integer, CentrifugeRecipe> getCentrifugeList() {
        return recipeList;
    }

    // Inner class: recipe definition
    public static class CentrifugeRecipe {
        public final ItemStack guaranteed;
        public final WeightedOutput[] poolSlot5;
        public final WeightedOutput[] poolSlot6;

        public CentrifugeRecipe(ItemStack guaranteed,
            WeightedOutput[] poolSlot5,
            WeightedOutput[] poolSlot6) {
            this.guaranteed = guaranteed;
            this.poolSlot5 = poolSlot5;
            this.poolSlot6 = poolSlot6;
        }

        public ItemStack getRandomOutputForSlot5(Random rand) {
            return rollWeighted(poolSlot5, rand);
        }

        public ItemStack getRandomOutputForSlot6(Random rand) {
            return rollWeighted(poolSlot6, rand);
        }

        public ItemStack rollWeighted(WeightedOutput[] pool, Random rand) {
            if (pool == null || pool.length == 0) return null;

            int totalWeight = 0;
            for (WeightedOutput w : pool) {
                totalWeight += w.weight;
            }
            if (totalWeight <= 0) return null; // all zero weights → nothing

            int roll = rand.nextInt(totalWeight);
            int cumulative = 0;
            for (WeightedOutput w : pool) {
                cumulative += w.weight;
                if (roll < cumulative) {
                    return w.stack.copy();
                }
            }
            return null;
        }
    }

    // Inner class: weighted random entry
    public static class WeightedOutput {
        public final ItemStack stack;
        public final int weight; // higher = more frequent

        public WeightedOutput(ItemStack stack, int weight) {
            this.stack = stack;
            this.weight = weight;
        }
    }
}
