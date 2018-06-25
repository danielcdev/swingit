package com.danielcotter.swingit.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.danielcotter.swingit.utility.GuiUtility;

@Component
public class OpenLocalRepositoryListener implements ActionListener {

	@Autowired
	private GuiUtility guiUtility;

	@Override
	public void actionPerformed(ActionEvent e) {
		guiUtility.openRepository();
	}
}
