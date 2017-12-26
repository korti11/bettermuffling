package net.korti.bettermuffling;

import net.korti.bettermuffling.common.CommonProxy;
import net.korti.bettermuffling.common.constant.ModInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, version = ModInfo.VERSION)
public class BetterMuffling {

    @Mod.Instance(ModInfo.MOD_ID)
    public static BetterMuffling instance;

    @SidedProxy(serverSide = ModInfo.COMMON_PROXY, clientSide = ModInfo.CLIENT_PROXY)
    public static CommonProxy proxy;

    public static CreativeTabs creativeTab = new CreativeTabs(ModInfo.MOD_ID) {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Blocks.WOOL);
        }
    };

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        proxy.preInit();
    }

}
