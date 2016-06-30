package net.glease.mods.ipr;

import java.io.IOException;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.HttpUtil;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	private String ip = "";
	private final Pattern PATTERN = Pattern.compile("'([a-fA-F:\\d\\.]+)'");

	@Override
	public void init() {
		String json;
		try {
			json = HttpUtil.func_152755_a(URI.create(IPReporter.IP_HOST).toURL());
		} catch (IOException e) {
			IPReporter.LOGGER.warn("can't pull IP!", e);
			return;
		}
		try {
		Matcher matcher = PATTERN.matcher(json);
		if (!matcher.find())
			return;
		ip = matcher.group(1);
		} catch (Exception e) {
			IPReporter.LOGGER.warn("can't parse IP!", e);
		}
	}

	@SubscribeEvent
	public void onConnect(ClientConnectedToServerEvent e) {
		if (e.isLocal || ip == null || ip.isEmpty())
			return;
		IPReporter.CHANNEL.sendToServer(new IPMessage(ip));
	}
}
