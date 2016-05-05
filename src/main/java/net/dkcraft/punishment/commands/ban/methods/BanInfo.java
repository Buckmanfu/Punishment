package net.dkcraft.punishment.commands.ban.methods;

public class BanInfo {

	private String targetName;
	private String senderName;
	private long banDate;
	private long banLength;
	private String banReason;
	private boolean active;
	private boolean permanent;

	public BanInfo(String targetName, String senderName, long banDate, long banLength, String banReason, boolean active, boolean permanent) {
		this.targetName = targetName;
		this.senderName = senderName;
		this.banDate = banDate;
		this.banLength = banLength;
		this.banReason = banReason;
		this.active = active;
		this.permanent = permanent;
	}

	public String getTargetName() {
		return targetName;
	}

	public String getSenderName() {
		return senderName;
	}

	public long getBanDate() {
		return banDate;
	}

	public long getBanLength() {
		return banLength;
	}

	public String getBanReason() {
		return banReason;
	}

	public boolean getActive() {
		return active;
	}

	public boolean getPermanent() {
		return permanent;
	}
}
