package net.glease.mods.ipr.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import net.glease.mods.ipr.IPReporter;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.classloading.FMLForgePlugin;

public class EntityPlayerMPGetPlayerIPHacker implements IClassTransformer, Opcodes {

	public static String getPlayerIP(EntityPlayerMP e) {
		String s = IPReporter.ips.get(e);
		System.out.println("Injected code executed.");
		if(s==null) {
			s = e.playerNetServerHandler.netManager.getSocketAddress().toString();
	        s = s.substring(s.indexOf("/") + 1);
	        s = s.substring(0, s.indexOf(":"));
		}
		return s;
	}
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if ("mw".equals(name) || "net.minecraft.entity.player.EntityPlayerMP".equals(transformedName)) {
			ClassReader cr = new ClassReader(basicClass);
			ClassWriter cw = new ClassWriter(cr, 0);
			MethodVisitor mv;
			{
				mv = cw.visitMethod(ACC_PUBLIC, FMLForgePlugin.RUNTIME_DEOBF ? "getPlayerIP" : "s",
						"()Ljava/lang/String;", null, null);
				mv.visitCode();
				mv.visitVarInsn(ALOAD, 0);
				mv.visitMethodInsn(INVOKESTATIC, Type.getInternalName(getClass()), "getPlayerIP", "(L"+cr.getClassName()+";)Ljava/lang/String;", false);
				mv.visitInsn(ARETURN);
				mv.visitMaxs(4, 2);
				mv.visitEnd();
			}
			cw.visitEnd();

			return cw.toByteArray();
		}
		return null;
	}

}