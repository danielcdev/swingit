package com.danielcotter.swingit.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class StagingModel {

	private Map<String, List<String>> unstagedFiles = new HashMap<>();
	private Map<String, List<String>> stagedFiles = new HashMap<>();

	public void addUnstagedFile(String filename) {
		addFile(unstagedFiles, filename);
	}

	public void addStagedFile(String filename) {
		addFile(stagedFiles, filename);
	}

	private void addFile(Map<String, List<String>> map, String filename) {
		String key = filename.substring(0, filename.lastIndexOf("/"));
		String value = filename.substring(filename.lastIndexOf("/") + 1, filename.length());

		if (!map.containsKey(key))
			map.put(key, new ArrayList<>());

		List<String> currentFiles = map.get(key);
		currentFiles.add(value);

		map.put(key, currentFiles);
	}

	/**
	 * @return the unstagedFiles
	 */
	public Map<String, List<String>> getUnstagedFiles() {
		return unstagedFiles;
	}

	/**
	 * @param unstagedFiles
	 *            the unstagedFiles to set
	 */
	public void setUnstagedFiles(Map<String, List<String>> unstagedFiles) {
		this.unstagedFiles = unstagedFiles;
	}

	/**
	 * @return the stagedFiles
	 */
	public Map<String, List<String>> getStagedFiles() {
		return stagedFiles;
	}

	/**
	 * @param stagedFiles
	 *            the stagedFiles to set
	 */
	public void setStagedFiles(Map<String, List<String>> stagedFiles) {
		this.stagedFiles = stagedFiles;
	}
}
