package com.danielcotter.swingit.view;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

import org.springframework.stereotype.Component;

@Component
public class MainView {

	private JFrame frame = new JFrame("SwinGit");
	private JMenuBar menuBar = new JMenuBar();
	private JTabbedPane tabPane = new JTabbedPane();
	private JMenuItem repositoryOpen = new JMenuItem("Open Local");
	private JMenuItem repositoryClone = new JMenuItem("Clone Remote");

	public void buildMenu() {
		JMenu repository = new JMenu("Repository");
		repository.add(repositoryOpen);
		repository.add(repositoryClone);
		menuBar.add(repository);
	}

	public void buildFrame() {
		frame.add(tabPane);
		frame.setJMenuBar(menuBar);
		frame.setSize(500, 500);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	/**
	 * @return the frame
	 */
	public JFrame getFrame() {
		return frame;
	}

	/**
	 * @param frame
	 *            the frame to set
	 */
	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	/**
	 * @return the menuBar
	 */
	public JMenuBar getMenuBar() {
		return menuBar;
	}

	/**
	 * @param menuBar
	 *            the menuBar to set
	 */
	public void setMenuBar(JMenuBar menuBar) {
		this.menuBar = menuBar;
	}

	/**
	 * @return the tabPane
	 */
	public JTabbedPane getTabPane() {
		return tabPane;
	}

	/**
	 * @param tabPane
	 *            the tabPane to set
	 */
	public void setTabPane(JTabbedPane tabPane) {
		this.tabPane = tabPane;
	}

	/**
	 * @return the repositoryOpen
	 */
	public JMenuItem getRepositoryOpen() {
		return repositoryOpen;
	}

	/**
	 * @param repositoryOpen
	 *            the repositoryOpen to set
	 */
	public void setRepositoryOpen(JMenuItem repositoryOpen) {
		this.repositoryOpen = repositoryOpen;
	}

	/**
	 * @return the repositoryClone
	 */
	public JMenuItem getRepositoryClone() {
		return repositoryClone;
	}

	/**
	 * @param repositoryClone
	 *            the repositoryClone to set
	 */
	public void setRepositoryClone(JMenuItem repositoryClone) {
		this.repositoryClone = repositoryClone;
	}
}
