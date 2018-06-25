package com.danielcotter.swingit.listener;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.danielcotter.swingit.controller.RepositoryController;

@Component
public class ListenerFactory {

	@Autowired
	private ApplicationContext context;

	@Autowired
	private OpenLocalRepositoryListener openLocalRepositoryListener;

	public ActionListener getOpenLocalRepositoryListener() {
		return openLocalRepositoryListener;
	}

	public MouseAdapter getStagedAndUnstagedListListener(RepositoryController controller) {
		StagedAndUnstagedListListener thisListener = (StagedAndUnstagedListListener) context
				.getBean("stagedAndUnstagedListListener");
		thisListener.setMyController(controller);

		return thisListener;
	}

	public ActionListener getMergeFeatureButtonListener(RepositoryController controller) {
		MergeFeatureButton thisListener = (MergeFeatureButton) context.getBean("mergeFeatureButton");
		thisListener.setMyController(controller);

		return thisListener;
	}
}
