package ac.grim.grimac.checks.impl.inventory;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;

@CheckData(name = "InventoryA")
public class InventoryA extends Check implements PostPredictionCheck {
    public InventoryA(final GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPredictionComplete(final PredictionComplete predictionComplete) {
        if (player.hasInventoryOpen && player.isSprinting && !player.isSwimming && predictionComplete.isChecked()
                && !player.packetStateData.lastPacketWasTeleport
                && !player.packetStateData.lastPacketWasOnePointSeventeenDuplicate
                && flagAndAlert()) {
            player.closeInventory();
        }
    }
}
