package com.dslm.firewood.command;


import com.dslm.firewood.Firewood;
import com.dslm.firewood.fireEffectHelper.flesh.FireEffectHelpers;
import com.dslm.firewood.fireEffectHelper.flesh.data.FireEffectNBTData;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class FirewoodCommand
{
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(
                Commands.literal("firewood_debug")
                        .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                        .then(Commands.literal("potion")
                                .executes(context -> {
                                    Firewood.LOGGER.info("=========================================");
                                    for(Potion potion : ForgeRegistries.POTIONS)
                                    {
                                        Firewood.LOGGER.info("-------------------------------------");
                                        Firewood.LOGGER.info(Objects.requireNonNull(potion.getRegistryName()).toString());
                                        Firewood.LOGGER.info("-------------------------------------");
                                        for (var effect : potion.getEffects()) {
                                            Firewood.LOGGER.info(effect.save(new CompoundTag()).toString());
                                        }
                                    }
                                    Firewood.LOGGER.info("=========================================");
                                    return 0;
                                }))
        );
    }
}
