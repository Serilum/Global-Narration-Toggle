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
	private static final String appDataPath = System.getenv("APPDATA");
	private static final File dir = new File(appDataPath);
	private static final String globalOptionsPath = appDataPath + File.separator + ".minecraft" + File.separator + "options.txt";
	private static final File globalOptionsFile = new File(globalOptionsPath);

	public static boolean isGloballyToggledOff() {
		try {
			if (dir.isDirectory() && globalOptionsFile.isFile()) {
				String optionsFileContent = new String(Files.readAllBytes(Paths.get(globalOptionsPath)));
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
			String currentOptionsFileContent = new String(Files.readAllBytes(Paths.get(globalOptionsPath)));
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

		FileWriter writer = new FileWriter(globalOptionsPath, StandardCharsets.UTF_8, false);
		writer.write(optionsFileContent.toString());
		writer.close();

		Variables.currentNarrationId = newNarratorId;
	}
}
