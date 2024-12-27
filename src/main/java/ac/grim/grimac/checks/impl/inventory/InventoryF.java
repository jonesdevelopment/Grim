package ac.grim.grimac.checks.impl.inventory;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;

@CheckData(name = "InventoryF", experimental = true)
public class InventoryF extends Check implements PacketCheck {
    public InventoryF(final GrimPlayer player) {
        super(player);
    }

    private long lastTransaction = Long.MAX_VALUE;

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType().equals(PacketType.Play.Client.HELD_ITEM_CHANGE)) {
            if (player.hasInventoryOpen) {
                if (lastTransaction < player.lastTransactionReceived.get() && flagAndAlert()) {
                    if (shouldModifyPackets()) {
                        event.setCancelled(true);
                        player.onPacketCancel();
                        player.getInventory().needResend = true;
                    }
                    player.closeInventory();
                }
            } else {
                reward();
            }
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.HELD_ITEM_CHANGE) {
            lastTransaction = player.lastTransactionSent.get();
        }
    }
}
