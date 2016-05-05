package net.dkcraft.punishment.commands.jail;

public class JailInfo {

	private String jailName;
	private String jailWorld;
	private double jailX;
	private double jailY;
	private double jailZ;
	private double jailYaw;
	private double jailPitch;

	public JailInfo(String jailName, String jailWorld, double jailX, double jailY, double jailZ, double jailYaw, double jailPitch) {
		this.jailName = jailName;
		this.jailWorld = jailWorld;
		this.jailX = jailX;
		this.jailY = jailY;
		this.jailZ = jailZ;
		this.jailYaw = jailYaw;
		this.jailPitch = jailPitch;
	}

	public String getJailName() {
		return jailName;
	}

	public String getJailWorld() {
		return jailWorld;
	}

	public double getJailX() {
		return jailX;
	}

	public double getJailY() {
		return jailY;
	}

	public double getJailZ() {
		return jailZ;
	}

	public double getJailYaw() {
		return jailYaw;
	}

	public double getJailPitch() {
		return jailPitch;
	}
}
