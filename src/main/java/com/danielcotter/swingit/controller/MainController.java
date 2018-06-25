package com.danielcotter.swingit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.danielcotter.swingit.listener.ListenerFactory;
import com.danielcotter.swingit.utility.GitUtility;
import com.danielcotter.swingit.view.MainView;

@Component
public class MainController {

	@Autowired
	private MainView view;

	@Autowired
	private GitUtility gitUtility;

	@Autowired
	private ListenerFactory listenerFactory;

	public void init() {
		assignMenuListeners();
		view.buildMenu();
		view.buildFrame();

		gitUtility.openAllRepositories();
	}

	private void assignMenuListeners() {
		view.getRepositoryOpen().addActionListener(listenerFactory.getOpenLocalRepositoryListener());
	}
}
