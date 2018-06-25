package com.danielcotter.swingit.listener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;

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
		JList<?> thisList = (JList<?>) mouseEvent.getSource();
		int index = thisList.locationToIndex(mouseEvent.getPoint());

		if (index < 0)
			return;

		try {
			String newline[] = myController.getView().getDiffArea().getText().split("\\r?\\n");
			int i = 0;

			if (thisList == myController.getView().getUnstagedJlist()) {
				for (String thisLine : newline) {
					i += thisLine.length();

					if (thisLine.contains(thisList.getModel().getElementAt(index).toString())) {
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
			if (thisList == myController.getView().getStagedJlist())
				gitUtility.unstageFile(myController.getGit(), thisList.getModel().getElementAt(index).toString());
			else
				gitUtility.stageFile(myController.getGit(), thisList.getModel().getElementAt(index).toString());
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
