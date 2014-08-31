package com.kulttuuri.quickhotbar;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.network.packet.Packet250CustomPayload;
import org.lwjgl.input.Keyboard;

import com.kulttuuri.quickhotbar.settings.SettingsClient;

import net.minecraft.client.Minecraft;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class NetworkConnectionHandler implements IConnectionHandler
{
	@Override
	public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager)
	{
        //System.out.println("PLAYER CONNECTED TO SERVER!");
        ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
        DataOutputStream outputStream = new DataOutputStream(bos);
        try
        {
            outputStream.writeBoolean(true);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = "invpserv";
        packet.data = bos.toByteArray();
        packet.length = bos.size();
        manager.addToSendQueue(packet);
	}

	@Override
	public String connectionReceived(NetLoginHandler netHandler, INetworkManager manager)
	{
		return null;
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler, String server, int port, INetworkManager manager)
	{
	}

	@Override
	public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager)
	{
	}

	@Override
	public void connectionClosed(INetworkManager manager)
	{
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
        {
            QuickHotbarMod.clientSettings.handleInventorySwitchInServer = false;
        }
	}

	@Override
	public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login)
	{
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
        {
            announceModWelcomeMessage();
        }
	}
	
	private void announceModWelcomeMessage()
	{
		SettingsClient settings = QuickHotbarMod.clientSettings;
		if (settings.ANNOUNCE_MOD_LOADED)
		{
            String keyNameScrolling = Keyboard.getKeyName(settings.SCROLLING_KEY).equals("LCONTROL") ? "left ctrl" : Keyboard.getKeyName(settings.SCROLLING_KEY).toLowerCase();
            String keyNameUp = Keyboard.getKeyName(settings.SCROLLING_KEY_UP);
            String keyNameDown = Keyboard.getKeyName(settings.SCROLLING_KEY_DOWN);
            String keyNameOpenmenu = Keyboard.getKeyName(settings.KEY_OPEN_MOD_SETTINGS_MENU);
            String keyNameSwitchMode = Keyboard.getKeyName(settings.SCROLLING_KEY_SWITCH_MODE);

            String orText = settings.ALLOW_SCROLLING_WITH_KEYBOARD ? " (or " + keyNameUp.toLowerCase() + " & " + keyNameDown.toLowerCase() + ")" : "";
            String switchModeText = settings.ALLOW_MODE_SWITCHING ? keyNameScrolling + " + " + keyNameSwitchMode + " to switch mode. " : "";
            String openMenuText = settings.ENABLE_SETTING_MENU ? keyNameScrolling + " + " + keyNameOpenmenu + " to view mod settings." : "";

            String msg = "Quick Hotbar " + QuickHotbarModInfo.VERSION + " loaded. ";
            msg = msg + keyNameScrolling + " + mouse wheel" + orText + " to scroll. ";
            msg = msg + switchModeText;
            msg = msg + openMenuText;
            Minecraft.getMinecraft().thePlayer.addChatMessage(msg);
		}
	}
}