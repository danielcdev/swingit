package com.danielcotter.swingit.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.lib.Ref;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.danielcotter.swingit.controller.RepositoryController;
import com.danielcotter.swingit.utility.GitUtility;
import com.danielcotter.swingit.utility.ModalUtility;

@Component
@Scope("prototype")
public class SwitchFeatureButton implements ActionListener {

	@Autowired
	private ModalUtility modalUtility;

	@Autowired
	private GitUtility gitUtility;

	RepositoryController myController;

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			List<Ref> refs = myController.getGit().branchList().call();
			List<String> humanReadable = new ArrayList<>();

			for (Ref thisRef : refs) {
				String name = thisRef.getName().substring(thisRef.getName().lastIndexOf("/") + 1);

				if (!name.equals("master") && !name.equals(myController.getGit().getRepository().getBranch()))
					humanReadable.add(name);
			}

			if (humanReadable.size() <= 0) {
				modalUtility.error("No other branches exist");
				return;
			}

			String names[] = humanReadable.toArray(new String[0]);

			String newBranchName = modalUtility.dropDown(names, "Select feature branch");

			if (newBranchName == null || newBranchName.isEmpty())
				return;

			gitUtility.stash(myController);
			myController.getGit().checkout().setName(newBranchName).call();
			gitUtility.loadStash(myController);
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
