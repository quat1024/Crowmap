package quaternary.crowmap;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = Crowmap.MODID, value = Side.CLIENT)
public class ClientEvents {
	@SubscribeEvent
	public static void tooltip(ItemTooltipEvent e) {
		if(!Crowmap.CrowmapConfig.tooltip || e.getEntityPlayer() == null) return;
		
		if(Crowmap.isMap(e.getItemStack())) {
			if(GuiScreen.isShiftKeyDown()) {
				e.getToolTip().add(TextFormatting.DARK_AQUA + I18n.format("crowmap.tooltip.show.1"));
				e.getToolTip().add(TextFormatting.DARK_AQUA + I18n.format("crowmap.tooltip.show.2"));
			} else {
				e.getToolTip().add(TextFormatting.DARK_GRAY + I18n.format("crowmap.tooltip.hide"));
			}
		}
	}
}
