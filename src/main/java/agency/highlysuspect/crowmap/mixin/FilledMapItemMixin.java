package agency.highlysuspect.crowmap.mixin;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.MapItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MapItem.class)
public class FilledMapItemMixin {
	@ModifyVariable(
		at = @At("HEAD"),
		method = "inventoryTick",
		argsOnly = true
	)
	public EquipmentSlot onEntityTickPre(EquipmentSlot slot) {
		//some time between 1.21.1 and 1.21.5 inventoryTick was changed to
		//pass a nullable InventorySlot corresponding to where the item is
		//ok. so returning MAINHAND will make the map always think it's
		//held in the main hand and therefore call the update function
		return EquipmentSlot.MAINHAND;
	}
}
