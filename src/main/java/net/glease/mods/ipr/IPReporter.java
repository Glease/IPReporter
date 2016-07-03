package net.glease.mods.ipr;

import java.util.Map;
import java.util.WeakHashMap;

import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayerMP;

@Mod(modid = "IPReporter", version= "0.1A")
public class IPReporter {
	public static Logger LOGGER; 
	public static final String IP_HOST = "";
	
	@Instance
	public static IPReporter INSTANCE;
	
	@SidedProxy(clientSide= "net.glease.mods.ipr.ClientProxy", serverSide = "net.glease.mods.ipr.CommonProxy")
	public static CommonProxy proxy;
	
	public static Map<EntityPlayerMP, String> ips;
	public static SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel("IPReporter");
	
	@EventHandler
	public void init(FMLPreInitializationEvent e) {
		proxy.init();
		LOGGER = e.getModLog();
		// LAN server is possible. Still instantiate the ips map even on client side
		// WeakHashMap to prevent memory leaks.
		ips = new WeakHashMap<>();
		CHANNEL.registerMessage(IPMessage.IPMessageHandler.class, IPMessage.class, 0, Side.SERVER);
	}
}
