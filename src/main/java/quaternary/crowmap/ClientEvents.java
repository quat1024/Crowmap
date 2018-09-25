package quaternary.crowmap;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMap;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = Crowmap.MODID, value = Side.CLIENT)
public class ClientEvents {
	@SubscribeEvent
	public static void tooltip(ItemTooltipEvent e) {
		if(!Crowmap.CrowmapConfig.tooltip) return;
		
		if(e.getItemStack().getItem() == Items.FILLED_MAP) {
			if(GuiScreen.isShiftKeyDown()) {
				e.getToolTip().add(TextFormatting.DARK_AQUA + I18n.translateToLocal("crowmap.tooltip.show.1"));
				e.getToolTip().add(TextFormatting.DARK_AQUA + I18n.translateToLocal("crowmap.tooltip.show.2"));
			} else {
				e.getToolTip().add(TextFormatting.DARK_GRAY + I18n.translateToLocal("crowmap.tooltip.hide"));
			}
		}
	}
}
