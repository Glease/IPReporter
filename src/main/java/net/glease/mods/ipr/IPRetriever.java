package net.glease.mods.ipr;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Strings;
import com.google.common.base.Supplier;

import net.minecraft.util.HttpUtil;
import net.minecraft.util.StringUtils;

public enum IPRetriever implements Supplier<String> {
	CHINAZ {
		@Override
		public String get() {
			String json;
			try {
				json = HttpUtil.func_152755_a(URI.create(IPReporter.IP_HOST).toURL());
			} catch (IOException e) {
				IPReporter.LOGGER.warn("can't pull IP!", e);
				return "";
			}
			try {
				Matcher matcher = PATTERN.matcher(json);
				if (!matcher.find())
					return "";
				return Strings.nullToEmpty(matcher.group(1));
			} catch (Exception e) {
				IPReporter.LOGGER.warn("can't parse IP!", e);
				return "";
			}
		}
	},
	SOCKET{

		@Override
		public String get() {
			try {
				return InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				return "";
			}
		}
		
	};
	/**
	 * This matches an IP, no matter v4 or v6. Hacky. Breaks if input is invalid
	 * but keep silent.
	 */
	private static final Pattern PATTERN = Pattern.compile("'([a-fA-F:\\d\\.]+)'");
	public static String getHard() {
		String ip;
		for (IPRetriever ipr : values()) {
			ip = ipr.get();
			if(!StringUtils.isNullOrEmpty(ip))
				return ip;
		}
		return "";
	}
}
