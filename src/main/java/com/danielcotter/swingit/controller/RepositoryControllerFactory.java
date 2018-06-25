package com.danielcotter.swingit.controller;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jgit.api.Git;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class RepositoryControllerFactory {

	@Autowired
	private ApplicationContext context;

	private Map<String, RepositoryController> repositoryControllers = new HashMap<>();

	public RepositoryController getInstance(String path) {
		return repositoryControllers.get(path);
	}

	public RepositoryController newInstance(String path, Git git) {
		RepositoryController controller = repositoryControllers.get(path);

		if (controller != null)
			return null;

		controller = (RepositoryController) context.getBean("repositoryController");
		controller.setPath(path);
		controller.setGit(git);
		repositoryControllers.put(path, controller);

		return controller;
	}
}
