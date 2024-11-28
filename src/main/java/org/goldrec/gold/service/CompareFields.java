package org.goldrec.gold.service;

import org.apache.commons.text.similarity.LevenshteinDistance;

public class CompareFields {
	public static double similarity(String s1, String s2) {
		LevenshteinDistance distance = new LevenshteinDistance();
		return 1 - distance.apply(s1, s2);
	}
}
