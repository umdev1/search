package com.example.search.data.types;

public enum Contenttype {

	ALBUM("ALBUM"), BOOK("BOOK");

	private String value;

	private Contenttype(String value) {

		this.value = value;

	}

	public String toString() {
		return this.value;
	}

	public static Contenttype fromValue(String value) {
		Contenttype[] var1 = values();
		int var2 = var1.length;

		for (int var3 = 0; var3 < var2; ++var3) {
			Contenttype element = var1[var3];
			if (element.toString().equals(value)) {
				return element;
			}
		}

		return null;
	}
}
