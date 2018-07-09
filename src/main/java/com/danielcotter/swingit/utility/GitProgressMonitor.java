package com.danielcotter.swingit.utility;

import org.eclipse.jgit.lib.ProgressMonitor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.danielcotter.swingit.controller.RepositoryController;

@Component
@Scope("prototype")
public class GitProgressMonitor implements ProgressMonitor {

	private RepositoryController controller;
	private int total = 0;
	private int completed = 0;
	private String title;

	@Override
	public void start(int totalTasks) {
		total = totalTasks;
		completed = 0;

		controller.getView().getProgressBar().setString("0% - " + title);
	}

	@Override
	public void beginTask(String title, int totalWork) {
		total = totalWork;
		completed = 0;
		this.title = title;

		controller.getView().getProgressBar().setString("0% - " + title);
	}

	@Override
	public void update(int thisCompleted) {
		completed += thisCompleted;
		float percentage = 0f;

		try {
			percentage = (completed / total) * 100;
		} catch (Exception e) {
		}

		controller.getView().getProgressBar().setString(percentage + "% - " + title);
		controller.getView().getProgressBar().setValue((int) percentage);
	}

	@Override
	public void endTask() {
		controller.getView().getProgressBar().setValue(100);
		controller.getView().getProgressBar().setString("Done");
	}

	@Override
	public boolean isCancelled() {
		controller.getView().getProgressBar().setValue(100);
		controller.getView().getProgressBar().setString("Cancelled");

		return false;
	}

	/**
	 * @return the controller
	 */
	public RepositoryController getController() {
		return controller;
	}

	/**
	 * @param controller
	 *            the controller to set
	 */
	public void setController(RepositoryController controller) {
		this.controller = controller;
	}
}