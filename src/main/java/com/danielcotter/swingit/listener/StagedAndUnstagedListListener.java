package com.danielcotter.swingit.listener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.danielcotter.swingit.controller.RepositoryController;
import com.danielcotter.swingit.utility.GitUtility;
import com.danielcotter.swingit.utility.ModalUtility;

@Component
@Scope("prototype")
public class StagedAndUnstagedListListener extends MouseAdapter {

	@Autowired
	private ModalUtility modalUtility;

	@Autowired
	private GitUtility gitUtility;

	RepositoryController myController;

	public void mouseClicked(MouseEvent mouseEvent) {
		JTree thisTree = (JTree) mouseEvent.getSource();
		Object[] test = thisTree.getSelectionPath().getPath();
		String filepath = "";
		boolean isFile = false;

		for (Object thisObject : test) {
			if (((DefaultMutableTreeNode) thisObject).getChildCount() == 0) {
				isFile = true;
				filepath += "/";
			}

			filepath += thisObject.toString();
		}

		if (!isFile)
			return;

		filepath = filepath.substring(filepath.indexOf("taged Files") + 11, filepath.length());

		try {
			String newline[] = myController.getView().getDiffArea().getText().split("\\r?\\n");
			int i = 0;

			if (thisTree == myController.getView().getUnstagedTree()) {
				for (String thisLine : newline) {
					i += thisLine.length();

					if (thisLine.contains(filepath)) {
						myController.getView().getDiffArea().setCaretPosition(i + thisLine.length());
						break;
					}
				}
			}
		} catch (Exception e) {
			modalUtility.error(e.getMessage());
		}

		if (mouseEvent.getClickCount() < 2)
			return;

		try {
			if (thisTree == myController.getView().getStagedTree())
				gitUtility.unstageFile(myController.getGit(), filepath);
			else
				gitUtility.stageFile(myController.getGit(), filepath);
		} catch (Exception e) {
			modalUtility.error(e.getMessage());
		}

		myController.monitorRepository();
	}

	/**
	 * @return the myController
	 */
	public RepositoryController getMyController() {
		return myController;
	}

	/**
	 * @param myController
	 *            the myController to set
	 */
	public void setMyController(RepositoryController myController) {
		this.myController = myController;
	}
}
