package quaternary.crowmap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod(
	modid = Crowmap.MODID,
	name = Crowmap.NAME,
	version = Crowmap.VERSION,
	acceptableRemoteVersions = "*"
)
@Mod.EventBusSubscriber
public class Crowmap {
	public static final String MODID = "crowmap";
	public static final String NAME = "Crowmap";
	public static final String VERSION = "GRADLE:VERSION";
	
	@SubscribeEvent
	public static void playerTickEvent(TickEvent.PlayerTickEvent e) {
		World world = e.player.world;
		if(world.isRemote) return;
		
		EntityPlayer player = e.player;
		
		for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
			if(i == player.inventory.currentItem) continue; //Already holding this map. No need to update it again
			
			ItemStack stack = player.inventory.getStackInSlot(i);
			if(isMap(stack)) {
				//Force the map data to update.
				ItemMap map = (ItemMap) stack.getItem();
				map.updateMapData(world, player, map.getMapData(stack, world));
			}
		}
	}
	
	public static boolean isMap(ItemStack stack) {
		if(CrowmapConfig.onlyVanillaMaps) return stack.getItem() == Items.FILLED_MAP;
		else return stack.getItem() instanceof ItemMap;
	}
	
	@Config(modid = MODID)
	@Mod.EventBusSubscriber
	public static class CrowmapConfig {
		@Config.Name("Display a tooltip on the map item")
		public static boolean tooltip = true;
		
		@Config.Name("Only vanilla maps")
		@Config.Comment("Enable this if you get crashes or something with weird modded maps")
		public static boolean onlyVanillaMaps = false;
		
		@SubscribeEvent
		public static void configChanged(ConfigChangedEvent.OnConfigChangedEvent e) {
			if(e.getModID().equals(MODID)) {
				ConfigManager.sync(MODID, Config.Type.INSTANCE);
			}
		}
	}
}
