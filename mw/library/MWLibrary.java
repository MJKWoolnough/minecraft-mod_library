package mw.library;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;

@Mod(modid="MWLibrary", name="MWLibrary", version="1.0.0")
public class MWLibrary {
	@EventHandler
	public void postInit(FMLPostInitializationEvent preInitEvent) {
		Blocks.forgeMicroParts = Loader.isModLoaded("ForgeMultipart");
	}
}
