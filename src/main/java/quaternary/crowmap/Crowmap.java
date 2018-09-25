package quaternary.crowmap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod(modid = Crowmap.MODID, name = Crowmap.NAME, version = Crowmap.VERSION)
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
			if(stack.getItem() == Items.FILLED_MAP) {
				//Force the map data to update.
				MapData mapData = Items.FILLED_MAP.getMapData(stack, world);
				Items.FILLED_MAP.updateMapData(world, player, mapData);
			}
		}
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
