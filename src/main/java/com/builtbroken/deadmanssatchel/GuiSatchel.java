package com.builtbroken.deadmanssatchel;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiSatchel extends GuiContainer {
    public static final int WIDTH = 176;
    public static final int HEIGHT = 151;

    private static final ResourceLocation background = new ResourceLocation(SatchelMod.MODID, "textures/gui/guibackground.png");

    public GuiSatchel(ItemStack stack, ContainerSatchel container) {
        super(container);

        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.drawDefaultBackground();
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        for(int i = 0; i < inventorySlots.getInventory().size() - 36; i++) {
        	int yCoord = i/9 * 18; // 9 slots fit per row, 18 is size of the slot texture
        	int xCoord = i%9 * 18; // 0, 1*18, 2*18, 3*18, loop per row
        	drawTexturedModalRect(guiLeft + 7 + xCoord, guiTop + 5 + yCoord, 7, 68, 18, 18); // 7,68 is a slot on texture
        }
    }

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
    
}