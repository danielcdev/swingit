package com.danielcotter.swingit.model;

public class GitFile {

	private String name;
	private boolean modified = false;
	private boolean untracked = false;
	private boolean missing = false;
	private boolean changed = false;
	private boolean added = false;
	private boolean removed = false;

	/**
	 * @param name
	 */
	public GitFile(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the modified
	 */
	public boolean isModified() {
		return modified;
	}

	/**
	 * @param modified
	 *            the modified to set
	 */
	public void setModified(boolean modified) {
		this.modified = modified;
	}

	/**
	 * @return the untracked
	 */
	public boolean isUntracked() {
		return untracked;
	}

	/**
	 * @param untracked
	 *            the untracked to set
	 */
	public void setUntracked(boolean untracked) {
		this.untracked = untracked;
	}

	/**
	 * @return the missing
	 */
	public boolean isMissing() {
		return missing;
	}

	/**
	 * @param missing
	 *            the missing to set
	 */
	public void setMissing(boolean missing) {
		this.missing = missing;
	}

	/**
	 * @return the changed
	 */
	public boolean isChanged() {
		return changed;
	}

	/**
	 * @param changed
	 *            the changed to set
	 */
	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	/**
	 * @return the added
	 */
	public boolean isAdded() {
		return added;
	}

	/**
	 * @param added
	 *            the added to set
	 */
	public void setAdded(boolean added) {
		this.added = added;
	}

	/**
	 * @return the removed
	 */
	public boolean isRemoved() {
		return removed;
	}

	/**
	 * @param removed
	 *            the removed to set
	 */
	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (added ? 1231 : 1237);
		result = prime * result + (changed ? 1231 : 1237);
		result = prime * result + (missing ? 1231 : 1237);
		result = prime * result + (modified ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (removed ? 1231 : 1237);
		result = prime * result + (untracked ? 1231 : 1237);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof GitFile))
			return false;
		GitFile other = (GitFile) obj;
		if (added != other.added)
			return false;
		if (changed != other.changed)
			return false;
		if (missing != other.missing)
			return false;
		if (modified != other.modified)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (removed != other.removed)
			return false;
		if (untracked != other.untracked)
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}
}
