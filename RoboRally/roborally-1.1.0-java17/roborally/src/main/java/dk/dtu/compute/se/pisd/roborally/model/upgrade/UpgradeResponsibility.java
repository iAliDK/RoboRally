package dk.dtu.compute.se.pisd.roborally.model.upgrade;

public enum UpgradeResponsibility {
    MODULARCHASSIS, PUSHLEFTORRIGHT, BLUESCREENDEATH, PUSHPANELLODGER, EXTRAHANDCARD;

    public static UpgradeResponsibility getRandom() {
        return values()[((int) (Math.random() * values().length-1))];
    }
}
