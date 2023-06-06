package dk.dtu.compute.se.pisd.roborally.model.upgrade;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import org.jetbrains.annotations.NotNull;

public class ModularChassis extends Upgrade {
    public ModularChassis() {
        this.upgradeResponsibility = UpgradeResponsibility.MODULARCHASSIS;
    }

    @Override
    public void doAction(Player player, GameController gameController) {
    }

    public void doAction(@NotNull Player player, @NotNull Player pushedPlayer) {
        if (pushedPlayer != null && !pushedPlayer.getUpgrades().isEmpty()) {
            for (Upgrade upgrade : player.getUpgrades()) {
                if (upgrade.responsible(UpgradeResponsibility.MODULARCHASSIS) && !upgrade.isActivatedThisStep()) {
                    player.getUpgrades().remove(upgrade);

                    int randomUpgradeNumber = (int) (Math.random() * pushedPlayer.getUpgrades().size());

                    pushedPlayer.getUpgrades().add(upgrade);

                    player.getUpgrades().add(pushedPlayer.getUpgrades().get(randomUpgradeNumber));
                    pushedPlayer.getUpgrades().remove(randomUpgradeNumber);
                    setActivatedThisStep(true);
                }
            }
        }
    }
}
