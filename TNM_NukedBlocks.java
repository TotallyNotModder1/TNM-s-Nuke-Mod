package net.minecraft.src;

public class TNM_NukedBlocks extends Block {
    private int TexSide;
    private int TexTop;
    private int TexBottom;

    public TNM_NukedBlocks(int id, int index, int index2, int index3, Material material){
        super(id, index, material);
        this.TexSide = index;
        this.TexTop = index2;
        this.TexBottom = index3;
    }
}
