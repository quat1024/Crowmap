package quaternary.crowmap.mixin.client;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(FilledMapItem.class)
public class FilledMapItemTooltipMixin {
	@Inject(
		method = "appendTooltip",
		at = @At("TAIL")
	)
	public void appendTooltipAfter(ItemStack stack, World world, List<Text> tooltip, TooltipContext bigMistake, CallbackInfo ci) {
		if(Screen.hasShiftDown()) {
			tooltip.add(new TranslatableText("crowmap.tooltip.hello").formatted(Formatting.DARK_AQUA));
			tooltip.add(new TranslatableText("crowmap.tooltip.info").formatted(Formatting.DARK_AQUA));
		} else {
			tooltip.add(new TranslatableText("crowmap.tooltip.hide").formatted(Formatting.DARK_GRAY, Formatting.ITALIC));
		}
	}
}
