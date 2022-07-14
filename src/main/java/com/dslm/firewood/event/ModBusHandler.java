package com.dslm.firewood.event;

import com.dslm.firewood.Register;
import com.dslm.firewood.capprovider.PlayerSpiritualDataProvider;
import com.dslm.firewood.entity.RemnantSoulEntity;
import com.dslm.firewood.util.StaticValue;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = StaticValue.MOD_ID)
public class ModBusHandler
{
    
    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event)
    {
        event.register(PlayerSpiritualDataProvider.PlayerSpiritualData.class);
    }
    
    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event)
    {
        event.put(Register.REMNANT_SOUL_ENTITY.get(), RemnantSoulEntity.prepareAttributes().build());
    }

//    @SubscribeEvent
//    public void enqueue(final InterModEnqueueEvent event) {
//        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE,
//                () -> Arrays.stream(SlotTypePreset.values())
//                        .map(preset -> preset.getMessageBuilder().cosmetic().build()).collect(Collectors.toList()));
//    }
}
