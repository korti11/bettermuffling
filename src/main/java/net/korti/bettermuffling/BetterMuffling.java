package net.korti.bettermuffling;

import net.korti.bettermuffling.common.CommonProxy;
import net.korti.bettermuffling.common.constant.ModInfo;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, version = ModInfo.VERSION)
public class BetterMuffling {

    @Mod.Instance(ModInfo.MOD_ID)
    public static BetterMuffling instance;

    @SidedProxy(serverSide = ModInfo.COMMON_PROXY, clientSide = ModInfo.CLIENT_PROXY)
    public static CommonProxy proxy;


}
