package agency.highlysuspect.crowmap.mixin.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(MapItem.class)
public class FilledMapItemHoverTextMixin {
	
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
	
	@Unique private void addHoldShiftPrompt(List<Component> tooltipComponents) {
		tooltipComponents.add(HOLD_SHIFT);
	}
	
	@Unique private void addInfo(List<Component> tooltipComponents) {
		tooltipComponents.add(INFO1);
		tooltipComponents.add(INFO2);
	}
	
	@Unique private void addBye(List<Component> tooltipComponents) {
		tooltipComponents.add(BYE);
	}
	
	@SuppressWarnings("UnnecessaryContinue")
	@Inject(
		method = "appendHoverText(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/Item$TooltipContext;Ljava/util/List;Lnet/minecraft/world/item/TooltipFlag;Lorg/spongepowered/asm/mixin/injection/callback/CallbackInfo;)V",
		at = @At("TAIL")
	)
	public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> tooltipComponents, TooltipFlag tooltipFlag, CallbackInfo ci) {
		if(tooltipContext == null || tooltipContext == Item.TooltipContext.EMPTY) return;
		
		boolean shifting = Screen.hasShiftDown();
		
		//it's a state machine! "continue" transitions states.
		
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
