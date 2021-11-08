package me.litz.constants;

public enum DbInfo {

	EMS("info"),
	ESS("ess");

	private final String info;

	DbInfo(final String info) {
		this.info = info;
	}

	public final String getInfo() {
		return this.info;
	}
}
