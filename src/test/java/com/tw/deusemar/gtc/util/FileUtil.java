package com.tw.deusemar.gtc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class FileUtil {
	private FileUtil() {}

	public static boolean contentEquals(String sourceProcessed, String sourceExpected)
			throws IOException {
		BufferedReader fileExpected =  new BufferedReader(new StringReader(sourceExpected));
		BufferedReader fileProcessed = new BufferedReader(new StringReader(sourceProcessed));
		while (true) {
			String fileLine = getNextLine(fileExpected);
			String stringLine = getNextLine(fileProcessed);
			if (fileLine == null && stringLine == null) {
				break;
			}

			if (fileLine == null || stringLine == null) {
				return false;
			}

			if (fileLine.equals(stringLine) == false) {
				return false;
			}
		}
		return true;
	}

	private static String getNextLine(BufferedReader reader) throws IOException {
		while (true) {
			String line = reader.readLine();
			if (line == null) {
				return null;
			}

			if (line.trim().length() != 0) {
				return line;
			}
		}
	}
}
