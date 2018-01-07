package com.example.archit.meracut;

/**
 * @author dipenp
 *
 */
public class Items {

	private String itemName;


	private long iconId;

	public Items(String itemName, long iconId) {
		this.itemName = itemName;

		this.iconId = iconId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}





	public long getIconId() {
		return iconId;
	}

	public void setIconId(long iconId) {
		this.iconId = iconId;
	}
}
