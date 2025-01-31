package ac.grim.grimac.checks.impl.inventory;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PostPredictionCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.update.PredictionComplete;
import com.github.retrooper.packetevents.protocol.attribute.Attributes;

@CheckData(name = "InventoryB", decay = 0.01, experimental = true)
public class InventoryB extends Check implements PostPredictionCheck {
    public InventoryB(final GrimPlayer player) {
        super(player);
    }

    private double lastSquaredActualSpeed;

    @Override
    public void onPredictionComplete(final PredictionComplete predictionComplete) {
        if (player.hasInventoryOpen && predictionComplete.isChecked()
                && !player.isFlying && !player.isGliding && !player.isSwimming
                && !player.packetStateData.lastPacketWasTeleport
                && !player.packetStateData.lastPacketWasOnePointSeventeenDuplicate
                && !player.uncertaintyHandler.isOrWasNearGlitchyBlock
                && !player.uncertaintyHandler.isSteppingOnHoney
                && !player.uncertaintyHandler.isSteppingOnSlime
                && !player.uncertaintyHandler.isSteppingOnBouncyBlock
                && !player.uncertaintyHandler.lastMovementWasZeroPointZeroThree) {
            final double squaredActualSpeed = player.actualMovement.getX() * player.actualMovement.getX()
                    + player.actualMovement.getZ() * player.actualMovement.getZ();
            final double movementSpeed = player.compensatedEntities.self.getAttributeValue(Attributes.MOVEMENT_SPEED) * 2;
            final double squaredMovementSpeed = movementSpeed * movementSpeed;
            if (squaredActualSpeed > squaredMovementSpeed) {
                final double difference = Math.abs(squaredActualSpeed - lastSquaredActualSpeed);
                if (difference < 1e-7) {
                    if (flagAndAlert("difference=" + difference)) {
                        player.closeInventory();
                    }
                } else {
                    reward();
                }
            }
            lastSquaredActualSpeed = squaredActualSpeed;
        }
    }
}
