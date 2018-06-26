package com.danielcotter.swingit.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.danielcotter.swingit.controller.RepositoryController;
import com.danielcotter.swingit.utility.GitUtility;
import com.danielcotter.swingit.utility.ModalUtility;

@Component
@Scope("prototype")
public class NewFeatureButton implements ActionListener {

	@Autowired
	private ModalUtility modalUtility;

	@Autowired
	private GitUtility gitUtility;

	RepositoryController myController;

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			gitUtility.stash(myController);
			gitUtility.createBranch(myController);
		} catch (Exception e1) {
			modalUtility.error(e1.getMessage());
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
