package agency.highlysuspect.crowmap.mixin.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public class ItemStackHoverTextMixin {
	
	@Unique
	int tooltipState = -1;
	
	@Unique long byeMillis;
	
	@Unique private static final Component HOLD_SHIFT =
		Component.translatable("crowmap.tooltip.hide").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC);
	@Unique private static final Component INFO1 =
		Component.translatable("crowmap.tooltip.hello").withStyle(ChatFormatting.DARK_AQUA);
	@Unique private static final Component INFO2 =
		Component.translatable("crowmap.tooltip.info").withStyle(ChatFormatting.DARK_AQUA);
	@Unique private static final Component BYE =
		Component.translatable("crowmap.tooltip.bye").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC);
	
	@Unique private void addHoldShiftPrompt(Consumer<Component> tooltipComponents) {
		tooltipComponents.accept(HOLD_SHIFT);
	}
	
	@Unique private void addInfo(Consumer<Component> tooltipComponents) {
		tooltipComponents.accept(INFO1);
		tooltipComponents.accept(INFO2);
	}
	
	@Unique private void addBye(Consumer<Component> tooltipComponents) {
		tooltipComponents.accept(BYE);
	}
	
	@Unique private boolean isMap() {
		@SuppressWarnings("DataFlowIssue")
		ItemStack THIS = (ItemStack) (Object) this;
		return THIS.getItem() instanceof MapItem;
	}
	
	@SuppressWarnings({"UnnecessaryContinue"})
	@Inject(
		method = "addDetailsToTooltip(Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/item/component/TooltipDisplay;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/TooltipFlag;Ljava/util/function/Consumer;)V",
		at = @At("TAIL")
	)
	public void appendHoverText(Item.TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, @Nullable Player player, TooltipFlag tooltipFlag, Consumer<Component> tooltipComponents, CallbackInfo ci) {
		if(!isMap()) return;
		if(tooltipContext == null || tooltipContext == Item.TooltipContext.EMPTY) return;
		
		boolean shifting = Screen.hasShiftDown();
		
		//it's a state machine! "continue" transitions states.
		//'i' instead of 'while(true)' is just defensive against
		// my shit code causing infinite loops
		
		for(int i = 0; i <= 3; i++) {
			switch(tooltipState) {
				//state -1: haven't checked to see if languages work yet.
				//goes to state 0 if they do, and -2 if they don't. (-2 is a dead end)
				case -1 -> {
					if(!Language.getInstance().has("crowmap.tooltip.hello")) {
						tooltipState = -2;
						return;
					} else {
						tooltipState = 0;
						continue;
					}
				}
				
				//state 0: haven't pressed shift yet.
				//displays <Hold Shift> and goes to state 1 when they do.
				case 0 -> {
					if(!shifting) {
						addHoldShiftPrompt(tooltipComponents);
						return;
					} else {
						tooltipState = 1;
						continue;
					}
				}
				
				//state 1: holding shift, looking at the message.
				//displays the message and goes to state 2 when the release shift.
				case 1 -> {
					if(shifting) {
						addInfo(tooltipComponents);
						return;
					} else {
						tooltipState = 2;
						byeMillis = System.currentTimeMillis();
						continue;
					}
				}
				
				//state 2: displaying the "this tooltip will now hide itself" message.
				//transitions back to state 1 when shift is pressed, or to state 3 after 5 seconds.
				case 2 -> {
					if(shifting) {
						//back to state 1
						tooltipState = 1;
						continue;
					} else if(System.currentTimeMillis() - byeMillis <= 5000) {
						addBye(tooltipComponents);
						return;
					} else {
						tooltipState = 3;
						continue;
					}
				}
				
				//state 3: no "hold shift" prompt. silently display message on shift only.
				case 3 -> {
					if(shifting) {
						addInfo(tooltipComponents);
					}
					return;
				}
				
				default -> {
					return;
				}
			}
		}
	}
}
