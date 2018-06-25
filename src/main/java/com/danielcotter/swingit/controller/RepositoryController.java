package com.danielcotter.swingit.controller;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.danielcotter.swingit.listener.ListenerFactory;
import com.danielcotter.swingit.utility.GitUtility;
import com.danielcotter.swingit.utility.GuiUtility;
import com.danielcotter.swingit.utility.ModalUtility;
import com.danielcotter.swingit.view.MainView;
import com.danielcotter.swingit.view.RepositoryView;

@Component
@Scope("prototype")
public class RepositoryController {

	@Autowired
	private MainView mainView;

	@Autowired
	private ListenerFactory listenerFactory;

	@Autowired
	private GitUtility gitUtility;

	@Autowired
	private GuiUtility guiUtility;

	@Autowired
	private ModalUtility modalUtility;

	private CredentialsProvider credentials = null;
	private List<String> lastUnstaged = new ArrayList<>();
	private List<String> lastStaged = new ArrayList<>();
	private RepositoryView view = new RepositoryView();
	private String path;
	private String lastDiff;
	private Git git;

	public void init() {
		assignListListeners();
		assignKeyListeners();
		assignButtonListeners();
		view.render();
		monitorRepository();
	}

	private void assignListListeners() {
		MouseAdapter myListener = listenerFactory.getStagedAndUnstagedListListener(this);

		view.getStagedJlist().addMouseListener(myListener);
		view.getUnstagedJlist().addMouseListener(myListener);
	}

	private void assignKeyListeners() {
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new MyDispatcher());
	}

	private class MyDispatcher implements KeyEventDispatcher {
		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {
			if (e.getID() == KeyEvent.KEY_PRESSED) {
				if (e.isControlDown() && e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_V) {
					try {
						gitUtility.commit(myController(), modalUtility.getInput("Commit message?"));
						gitUtility.push(myController());
					} catch (Exception e1) {
						modalUtility.error(e1.getMessage());

						try {
							gitUtility.undoCommit(myController());
						} catch (Exception e2) {
							modalUtility.error(e2.getMessage());
						}
					}
				}
			}
			return false;
		}
	}

	private void assignButtonListeners() {
		view.getMergeFeatureButton().addActionListener(listenerFactory.getMergeFeatureButtonListener(this));
	}

	private RepositoryController myController() {
		return this;
	}

	@Async
	@Scheduled(fixedDelay = 5000)
	public void monitorRepository() {
		if (git == null)
			return;

		Status status = null;

		try {
			status = git.status().call();
		} catch (Exception e) {
			e.printStackTrace();
		}

		updateUnstagedFiles(status);
		updateStagedFiles(status);

		try {
			String diffText = gitUtility.getDiff(this);

			if (!diffText.equals(lastDiff)) {
				lastDiff = diffText;
				view.getDiffArea().setText(diffText);
				guiUtility.colorDiff(view.getDiffArea());
			}
		} catch (Exception e) {
			modalUtility.error(e.getMessage());
		}

		try {
			String isModified = (status.hasUncommittedChanges()) ? "* " : "";

			mainView.getTabPane().setTitleAt(0, isModified + git.getRepository().getWorkTree().getName() + " ["
					+ git.getRepository().getBranch() + "]");
		} catch (Exception e) {
			modalUtility.error(e.getMessage());
		}
	}

	private void updateUnstagedFiles(Status status) {
		List<String> unstagedFiles = gitUtility.getUnstaged(status);

		if (!unstagedFiles.equals(lastUnstaged)) {
			lastUnstaged = unstagedFiles;
			view.getUnstagedList().clear();

			for (String thisFile : unstagedFiles)
				view.getUnstagedList().addElement(thisFile);
		}
	}

	private void updateStagedFiles(Status status) {
		List<String> stagedFiles = gitUtility.getStaged(status);

		if (!stagedFiles.equals(lastStaged)) {
			lastStaged = stagedFiles;
			view.getStagedList().clear();

			for (String thisFile : stagedFiles)
				view.getStagedList().addElement(thisFile);
		}
	}

	/**
	 * @return the mainView
	 */
	public MainView getMainView() {
		return mainView;
	}

	/**
	 * @param mainView
	 *            the mainView to set
	 */
	public void setMainView(MainView mainView) {
		this.mainView = mainView;
	}

	/**
	 * @return the listenerFactory
	 */
	public ListenerFactory getListenerFactory() {
		return listenerFactory;
	}

	/**
	 * @param listenerFactory
	 *            the listenerFactory to set
	 */
	public void setListenerFactory(ListenerFactory listenerFactory) {
		this.listenerFactory = listenerFactory;
	}

	/**
	 * @return the gitFunctions
	 */
	public GitUtility getGitFunctions() {
		return gitUtility;
	}

	/**
	 * @param gitFunctions
	 *            the gitFunctions to set
	 */
	public void setGitFunctions(GitUtility gitFunctions) {
		this.gitUtility = gitFunctions;
	}

	/**
	 * @return the guiFunctions
	 */
	public GuiUtility getGuiFunctions() {
		return guiUtility;
	}

	/**
	 * @param guiFunctions
	 *            the guiFunctions to set
	 */
	public void setGuiFunctions(GuiUtility guiFunctions) {
		this.guiUtility = guiFunctions;
	}

	/**
	 * @return the modalUtility
	 */
	public ModalUtility getModalUtility() {
		return modalUtility;
	}

	/**
	 * @param modalUtility
	 *            the modalUtility to set
	 */
	public void setModalUtility(ModalUtility modalUtility) {
		this.modalUtility = modalUtility;
	}

	/**
	 * @return the credentials
	 */
	public CredentialsProvider getCredentials() {
		return credentials;
	}

	/**
	 * @param credentials
	 *            the credentials to set
	 */
	public void setCredentials(CredentialsProvider credentials) {
		this.credentials = credentials;
	}

	/**
	 * @return the lastUnstaged
	 */
	public List<String> getLastUnstaged() {
		return lastUnstaged;
	}

	/**
	 * @param lastUnstaged
	 *            the lastUnstaged to set
	 */
	public void setLastUnstaged(List<String> lastUnstaged) {
		this.lastUnstaged = lastUnstaged;
	}

	/**
	 * @return the lastStaged
	 */
	public List<String> getLastStaged() {
		return lastStaged;
	}

	/**
	 * @param lastStaged
	 *            the lastStaged to set
	 */
	public void setLastStaged(List<String> lastStaged) {
		this.lastStaged = lastStaged;
	}

	/**
	 * @return the view
	 */
	public RepositoryView getView() {
		return view;
	}

	/**
	 * @param view
	 *            the view to set
	 */
	public void setView(RepositoryView view) {
		this.view = view;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the lastDiff
	 */
	public String getLastDiff() {
		return lastDiff;
	}

	/**
	 * @param lastDiff
	 *            the lastDiff to set
	 */
	public void setLastDiff(String lastDiff) {
		this.lastDiff = lastDiff;
	}

	/**
	 * @return the git
	 */
	public Git getGit() {
		return git;
	}

	/**
	 * @param git
	 *            the git to set
	 */
	public void setGit(Git git) {
		this.git = git;
	}
}
