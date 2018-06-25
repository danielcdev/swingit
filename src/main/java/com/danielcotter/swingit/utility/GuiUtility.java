package com.danielcotter.swingit.utility;

import java.awt.Color;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.danielcotter.swingit.controller.RepositoryController;
import com.danielcotter.swingit.controller.RepositoryControllerFactory;
import com.danielcotter.swingit.view.MainView;

@Component
public class GuiUtility {

	@Autowired
	private ModalUtility modalUtility;

	@Autowired
	private RepositoryControllerFactory repositoryControllerFactory;

	@Autowired
	private MainView mainView;

	public void openRepository() {
		Git git = null;
		Status status = null;
		File initialPath = new File(System.getProperty("user.home") + File.separator + "git");
		initialPath = (!initialPath.exists()) ? new File(System.getProperty("user.home")) : initialPath;

		String path = null;
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(initialPath);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
			path = fileChooser.getSelectedFile().getAbsolutePath();
		else
			return;

		try {
			git = Git.open(new File(path));
			status = git.status().call();
		} catch (Exception e) {
			modalUtility.error(e.getMessage());
			return;
		}

		RepositoryController controller = repositoryControllerFactory.newInstance(path, git);

		if (controller == null)
			return;

		controller.init();

		try {
			String isModified = (status.hasUncommittedChanges()) ? "* " : "";

			mainView.getTabPane().add(isModified + git.getRepository().getWorkTree().getName() + " ["
					+ git.getRepository().getBranch() + "]", controller.getView().getPanelTop());
		} catch (Exception e) {
			modalUtility.error(e.getMessage());
			return;
		}
	}

	public void colorDiff(JTextArea diffArea) throws BadLocationException {
		Highlighter.HighlightPainter redPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.red);
		Highlighter.HighlightPainter greenPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.green);
		Highlighter.HighlightPainter grayPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.lightGray);

		String thisText = diffArea.getText();
		String newline[] = thisText.split("\\r?\\n");

		int i = 0;
		for (String line : newline) {
			if (i >= newline.length)
				break;

			if (line.startsWith("diff --git"))
				diffArea.getHighlighter().addHighlight(diffArea.getLineStartOffset(i), diffArea.getLineEndOffset(i),
						grayPainter);

			if (line.startsWith("-") && !line.startsWith("---"))
				diffArea.getHighlighter().addHighlight(diffArea.getLineStartOffset(i), diffArea.getLineEndOffset(i),
						redPainter);

			if (line.startsWith("+") && !line.startsWith("+++"))
				diffArea.getHighlighter().addHighlight(diffArea.getLineStartOffset(i), diffArea.getLineEndOffset(i),
						greenPainter);
			i++;
		}

	}
}
