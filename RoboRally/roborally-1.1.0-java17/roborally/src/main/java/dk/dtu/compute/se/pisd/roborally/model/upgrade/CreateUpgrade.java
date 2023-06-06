package dk.dtu.compute.se.pisd.roborally.model.upgrade;

public class CreateUpgrade {
    public static Upgrade getUpgrade(UpgradeResponsibility upgradeResponsibility){
        switch (upgradeResponsibility) {
            case MODULARCHASSIS -> { return new ModularChassis(); }
            case EXTRAHANDCARD -> { return new ExtraHandCard(); }
        }
        return null;
    }
}
