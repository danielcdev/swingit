package com.danielcotter.swingit.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class MergeFeatureButton implements ActionListener {

	@Autowired
	private ModalUtility modalUtility;

	@Autowired
	private GitUtility gitUtility;

	RepositoryController myController;

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			gitUtility.merge(myController);
		} catch (Exception e1) {
			modalUtility.error(e1.getMessage());
		}
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
