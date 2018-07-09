package com.danielcotter.swingit.controller;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.swing.tree.DefaultMutableTreeNode;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.danielcotter.swingit.listener.ListenerFactory;
import com.danielcotter.swingit.model.StagingModel;
import com.danielcotter.swingit.utility.GitProgressMonitor;
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

	@Autowired
	private StagingModel stagingModel;

	@Autowired
	private GitProgressMonitor progressMonitor;

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

		view.getStagedTree().addMouseListener(myListener);
		view.getUnstagedTree().addMouseListener(myListener);
	}

	private void assignKeyListeners() {
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new MyDispatcher());
	}

	private class MyDispatcher implements KeyEventDispatcher {
		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {
			if (e.getID() == KeyEvent.KEY_PRESSED) {
				if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V) {
					try {
						String commitMessage = modalUtility.getInput("Commit message?");

						if (commitMessage == null)
							return false;

						gitUtility.commit(myController(), commitMessage);
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
		view.getNewFeatureButton().addActionListener(listenerFactory.getNewFeatureButtonListener(this));
		view.getSwitchFeatureButton().addActionListener(listenerFactory.getSwitchFeatureButtonListener(this));
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
		}
	}

	@PostConstruct
	private void constructed() {
		progressMonitor.setController(this);
	}

	private void updateUnstagedFiles(Status status) {
		List<String> unstagedFiles = gitUtility.getUnstaged(status);

		if (unstagedFiles.equals(lastUnstaged))
			return;

		lastUnstaged = unstagedFiles;
		stagingModel.getUnstagedFiles().clear();

		for (String thisFile : unstagedFiles)
			stagingModel.addUnstagedFile(thisFile);

		view.getUnstagedRoot().removeAllChildren();

		for (Map.Entry<String, List<String>> entry : stagingModel.getUnstagedFiles().entrySet()) {
			DefaultMutableTreeNode rootOfFolder = new DefaultMutableTreeNode(entry.getKey());

			for (String thisFile : entry.getValue())
				rootOfFolder.add(new DefaultMutableTreeNode(thisFile));

			view.getUnstagedRoot().add(rootOfFolder);
		}

		view.getUnstagedModel().reload();

		for (int i = 0; i < view.getUnstagedTree().getRowCount(); i++)
			view.getUnstagedTree().expandRow(i);
	}

	private void updateStagedFiles(Status status) {
		List<String> stagedFiles = gitUtility.getStaged(status);

		if (stagedFiles.equals(lastStaged))
			return;

		lastStaged = stagedFiles;
		stagingModel.getStagedFiles().clear();

		for (String thisFile : stagedFiles)
			stagingModel.addStagedFile(thisFile);

		view.getStagedRoot().removeAllChildren();

		for (Map.Entry<String, List<String>> entry : stagingModel.getStagedFiles().entrySet()) {
			DefaultMutableTreeNode rootOfFolder = new DefaultMutableTreeNode(entry.getKey());

			for (String thisFile : entry.getValue())
				rootOfFolder.add(new DefaultMutableTreeNode(thisFile));

			view.getStagedRoot().add(rootOfFolder);
		}

		view.getStagedModel().reload();

		for (int i = 0; i < view.getStagedTree().getRowCount(); i++)
			view.getStagedTree().expandRow(i);
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
	 * @return the gitUtility
	 */
	public GitUtility getGitUtility() {
		return gitUtility;
	}

	/**
	 * @param gitUtility
	 *            the gitUtility to set
	 */
	public void setGitUtility(GitUtility gitUtility) {
		this.gitUtility = gitUtility;
	}

	/**
	 * @return the guiUtility
	 */
	public GuiUtility getGuiUtility() {
		return guiUtility;
	}

	/**
	 * @param guiUtility
	 *            the guiUtility to set
	 */
	public void setGuiUtility(GuiUtility guiUtility) {
		this.guiUtility = guiUtility;
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
	 * @return the stagingModel
	 */
	public StagingModel getStagingModel() {
		return stagingModel;
	}

	/**
	 * @param stagingModel
	 *            the stagingModel to set
	 */
	public void setStagingModel(StagingModel stagingModel) {
		this.stagingModel = stagingModel;
	}

	/**
	 * @return the progressMonitor
	 */
	public GitProgressMonitor getProgressMonitor() {
		return progressMonitor;
	}

	/**
	 * @param progressMonitor
	 *            the progressMonitor to set
	 */
	public void setProgressMonitor(GitProgressMonitor progressMonitor) {
		this.progressMonitor = progressMonitor;
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
