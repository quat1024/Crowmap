package agency.highlysuspect.crowmap.mixin.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(MapItem.class)
public class FilledMapItemHoverTextMixin {
	@Inject(
		method = "appendHoverText",
		at = @At("TAIL")
	)
	public void appendHoverTextAfter(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced, CallbackInfo ci) {
		if(Screen.hasShiftDown()) {
			tooltipComponents.add(Component.translatable("crowmap.tooltip.hello").withStyle(ChatFormatting.DARK_AQUA));
			tooltipComponents.add(Component.translatable("crowmap.tooltip.info").withStyle(ChatFormatting.DARK_AQUA));
		} else {
			tooltipComponents.add(Component.translatable("crowmap.tooltip.hide").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
		}
	}
}
