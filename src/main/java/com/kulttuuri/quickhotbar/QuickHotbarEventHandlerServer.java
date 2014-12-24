package com.kulttuuri.quickhotbar;

import com.kulttuuri.quickhotbar.packets.PacketAnnounceServerAssist;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayerMP;

public class QuickHotbarEventHandlerServer
{
    @SubscribeEvent
    public void playerJoinedEvent(PlayerEvent.PlayerLoggedInEvent event)
    {
        //System.out.println(event.player.getDisplayName() + " !!! JOINED DAH SERVER!");
        QuickHotbarMod.instance.proxy.simpleNetworkWrapper.sendTo(new PacketAnnounceServerAssist(true, QuickHotbarModInfo.VERSION), (EntityPlayerMP) event.player);
    }
}