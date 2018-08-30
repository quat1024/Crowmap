package quaternary.crowmap;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.List;

@Mod(modid = Crowmap.MODID, name = Crowmap.NAME, version = Crowmap.VERSION)
@Mod.EventBusSubscriber
public class Crowmap {
	public static final String MODID = "crowmap";
	public static final String NAME = "Crowmap";
	public static final String VERSION = "GRADLE:VERSION";
	
	public static final Logger LOG = LogManager.getLogger(NAME);
	
	@SubscribeEvent
	public static void items(RegistryEvent.Register<Item> e) {
		IForgeRegistry<Item> reg = e.getRegistry();
		
		LOG.info("Registering a replacement map item - Forge will print a warning");
		
		reg.register(new ItemMap(){
			@Override
			public void onUpdate(ItemStack stack, World worldIn, Entity entity, int itemSlot, boolean isSelected) {
				if (!worldIn.isRemote) {
					MapData mapdata = this.getMapData(stack, worldIn);
					
					if (entity instanceof EntityPlayer) {
						EntityPlayer player = (EntityPlayer)entity;
						mapdata.updateVisiblePlayers(player, stack);
						
						//The main attraction.
						//Unconditionally update map data, instead of checking for whether the item is held or not.
						this.updateMapData(worldIn, entity, mapdata);
					}
				}
			}
			
			@SideOnly(Side.CLIENT)
			@Override
			public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag mistake) {
				super.addInformation(stack, world, tooltip, mistake);
				if(!CrowmapConfig.tooltip) return;
				
				if(GuiScreen.isShiftKeyDown()) {
					tooltip.add(TextFormatting.DARK_AQUA + I18n.translateToLocal("crowmap.tooltip.show.1"));
					tooltip.add(TextFormatting.DARK_AQUA + I18n.translateToLocal("crowmap.tooltip.show.2"));
				} else {
					tooltip.add(TextFormatting.DARK_GRAY + I18n.translateToLocal("crowmap.tooltip.hide"));
				}
			}
		}.setRegistryName(Items.FILLED_MAP.getRegistryName()).setTranslationKey("map"));
	}
	
	@Config(modid = MODID)
	@Mod.EventBusSubscriber
	public static class CrowmapConfig {
		@Config.Name("Display a tooltip on the map item")
		public static boolean tooltip = true;
		
		@SubscribeEvent
		public static void configChanged(ConfigChangedEvent.OnConfigChangedEvent e) {
			if(e.getModID().equals(MODID)) {
				ConfigManager.sync(MODID, Config.Type.INSTANCE);
			}
		}
	}
}
