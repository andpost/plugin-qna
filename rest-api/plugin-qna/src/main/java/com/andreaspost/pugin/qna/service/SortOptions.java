package com.andreaspost.pugin.qna.service;

import java.util.HashMap;
import java.util.Map;

public class SortOptions {

	public enum SortOrder {
		ASC, DESC;

		public static SortOrder of(String sign) {
			if ("+".equals(sign)) {
				return ASC;
			}
			if ("-".equals(sign)) {
				return DESC;
			}
			throw new IllegalArgumentException("Unsupported SortOrder: " + sign);
		}
	}

	private Map<String, SortOrder> sortOptionsMap;

	public SortOptions() {
		sortOptionsMap = new HashMap<>();
	}

	public void addSortOption(String field, SortOrder order) {
		sortOptionsMap.put(field, order);
	}

	public Map<String, SortOrder> getSortOptions() {
		return sortOptionsMap;
	}
}
