package quaternary.crowmap.mixin.client;

import net.minecraft.client.gui.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TextComponent;
import net.minecraft.text.TextFormat;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(FilledMapItem.class)
public class FilledMapItemTooltipMixin {
	@Inject(
		method = "buildTooltip(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Ljava/util/List;Lnet/minecraft/client/item/TooltipContext;)V",
		at = @At("TAIL")
	)
	public void buildTooltipAfter(ItemStack stack, World world, List<TextComponent> tooltip, TooltipContext bigMistake, CallbackInfo ci) {
		if(Screen.hasShiftDown()) {
			tooltip.add(new TranslatableTextComponent("crowmap.tooltip.hello").applyFormat(TextFormat.DARK_AQUA));
			tooltip.add(new TranslatableTextComponent("crowmap.tooltip.info").applyFormat(TextFormat.DARK_AQUA));
		} else {
			tooltip.add(new TranslatableTextComponent("crowmap.tooltip.hide").applyFormat(TextFormat.DARK_GRAY, TextFormat.ITALIC));
		}
	}
}
