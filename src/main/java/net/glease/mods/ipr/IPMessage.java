package net.glease.mods.ipr;

import com.google.common.base.Strings;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class IPMessage implements IMessage {
	private String ip;

	public IPMessage() {
	}

	public IPMessage(String ip) {
		super();
		this.ip = ip;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		ip = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, ip);
	}

	public static class IPMessageHandler implements IMessageHandler<IPMessage, IMessage> {

		@Override
		public IMessage onMessage(IPMessage message, MessageContext ctx) {
			IPReporter.ips.put(ctx.getServerHandler().playerEntity, Strings.nullToEmpty(message.ip));
			return null;
		}

	}
}
