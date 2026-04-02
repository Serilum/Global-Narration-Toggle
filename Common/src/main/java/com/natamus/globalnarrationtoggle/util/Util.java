package com.natamus.globalnarrationtoggle.util;

import com.natamus.collective.functions.NumberFunctions;
import com.natamus.globalnarrationtoggle.data.Variables;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Util {
	private static final String OS = (System.getProperty("os.name")).toUpperCase();

	private static String getAppDataPath() {
		if (OS.contains("WIN")) {
			return System.getenv("AppData");
		}

		String appDataPath = System.getProperty("user.home");
		if (OS.contains("MAC")) {
			appDataPath += File.separator + "Library" + File.separator + "Application Support";
		}

		return appDataPath;
	}
	private static final File dir = new File(getAppDataPath());

	private static String getGlobalOptionsPath() {
		String dot = ".";
		if (OS.contains("MAC")) {
			dot = "";
		}

		return getAppDataPath() + File.separator + dot + "minecraft" + File.separator + "options.txt";
	}
	private static final File globalOptionsFile = new File(getGlobalOptionsPath());

	public static boolean isGloballyToggledOff() {
		try {
			if (dir.isDirectory() && globalOptionsFile.isFile()) {
				String optionsFileContent = new String(Files.readAllBytes(Paths.get(getGlobalOptionsPath())));
				for (String line : optionsFileContent.split("\n")) {
					if (line.startsWith("narrator:")) {
						String narratorSetting = line.split(":")[1].strip();
						if (NumberFunctions.isNumeric(narratorSetting)) {
							int narratorSettingId = Integer.parseInt(narratorSetting);
							Variables.currentNarrationId = narratorSettingId;

							return narratorSettingId == 0;
						}
					}
				}
			}
		}
		catch (IOException ignored) {}

		return false;
	}

	public static void initGlobalSync(int newNarratorId) throws IOException {
		if (newNarratorId == Variables.currentNarrationId) {
			return;
		}

		if (!dir.isDirectory()) {
			boolean ignored = dir.mkdirs();
		}

		StringBuilder optionsFileContent = new StringBuilder();
		if (globalOptionsFile.isFile()) {
			String currentOptionsFileContent = new String(Files.readAllBytes(Paths.get(getGlobalOptionsPath())));
			for (String line : currentOptionsFileContent.split("\n")) {
				if (line.startsWith("narrator:")) {
					optionsFileContent.append("narrator:").append(newNarratorId).append("\n");
					continue;
				}
				optionsFileContent.append(line).append("\n");
			}
		}
		else {
			optionsFileContent.append("narrator:").append(newNarratorId);
		}

		FileWriter writer = new FileWriter(getGlobalOptionsPath(), StandardCharsets.UTF_8, false);
		writer.write(optionsFileContent.toString());
		writer.close();

		Variables.currentNarrationId = newNarratorId;
	}
}
