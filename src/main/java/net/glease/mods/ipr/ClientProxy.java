package net.glease.mods.ipr;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	@Override
	public void init() {
		FMLCommonHandler.instance().bus().register(this);
	}

	/**
	 * 
	 * @param e
	 */
	@SubscribeEvent
	public void onConnect(ClientConnectedToServerEvent e) {
		IPReporter.CHANNEL.sendToServer(new IPMessage(IPRetriever.getHard()));
	}
}
