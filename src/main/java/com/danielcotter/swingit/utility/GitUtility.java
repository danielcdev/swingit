package com.danielcotter.swingit.utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.AbortedByHookException;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.UnmergedPathsException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.danielcotter.swingit.controller.RepositoryController;

@Component
public class GitUtility {

	@Autowired
	private ModalUtility modalUtility;

	public void openAllRepositories() {

	}

	public void commit(RepositoryController controller, String message)
			throws NoHeadException, NoMessageException, UnmergedPathsException, ConcurrentRefUpdateException,
			WrongRepositoryStateException, AbortedByHookException, GitAPIException {
		controller.getGit().commit().setMessage(message).call();
	}

	public void undoCommit(RepositoryController controller) throws CheckoutConflictException, GitAPIException {
		controller.getGit().reset().setMode(ResetType.SOFT).setRef("HEAD^").call();
	}

	@Async
	public void push(RepositoryController controller)
			throws InvalidRemoteException, TransportException, GitAPIException {
		if (controller.getCredentials() == null) {
			String username = modalUtility.getInput("Username");
			String password = modalUtility.getInput("Password");

			controller.setCredentials(new UsernamePasswordCredentialsProvider(username, password));
		}

		controller.getGit().push().setCredentialsProvider(controller.getCredentials()).call();
	}

	@Async
	public void push(RepositoryController controller, Boolean force)
			throws InvalidRemoteException, TransportException, GitAPIException {
		if (controller.getCredentials() == null) {
			String username = modalUtility.getInput("Username");
			String password = modalUtility.getInput("Password");

			controller.setCredentials(new UsernamePasswordCredentialsProvider(username, password));
		}

		controller.getGit().push().setCredentialsProvider(controller.getCredentials()).setForce(force).call();
	}

	public void merge(RepositoryController controller)
			throws IOException, InvalidRemoteException, TransportException, GitAPIException {
		if (controller.getCredentials() == null) {
			String username = modalUtility.getInput("Username");
			String password = modalUtility.getInput("Password");

			controller.setCredentials(new UsernamePasswordCredentialsProvider(username, password));
		}

		String currentBranch = controller.getGit().getRepository().getBranch();
		Ref currentRef = controller.getGit().getRepository().findRef(currentBranch);

		controller.getGit().pull().setRemoteBranchName("master").setCredentialsProvider(controller.getCredentials())
				.call();
		controller.getGit().checkout().setName("master").call();

		controller.getGit().merge().include(currentRef).setSquash(true).setStrategy(MergeStrategy.OURS).call();

		controller.getGit().commit().setMessage("Merge branch '" + currentBranch + "'").call();
		push(controller, true);
	}

	public void createBranch(RepositoryController controller)
			throws RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, GitAPIException {
		String branchName = modalUtility.getInput("Branch name?");

		if (branchName == null)
			return;

		controller.getGit().branchCreate().setName(branchName).call();
		controller.getGit().checkout().setName(branchName).call();
		push(controller);
	}

	public void stash(RepositoryController controller) throws GitAPIException, IOException {
		String currentBranch = controller.getGit().getRepository().getBranch();
		int i = 0;

		for (RevCommit thisRef : controller.getGit().stashList().call()) {
			if (thisRef.getShortMessage().startsWith(currentBranch))
				controller.getGit().stashDrop().setStashRef(i).call();

			i++;
		}

		controller.getGit().stashCreate().setIncludeUntracked(true)
				.setWorkingDirectoryMessage(currentBranch + " " + new Date().getTime())
				.setIndexMessage(currentBranch + " " + new Date().getTime()).call();
	}

	public void loadStash(RepositoryController controller)
			throws IOException, InvalidRefNameException, GitAPIException {
		String currentBranch = controller.getGit().getRepository().getBranch();

		for (RevCommit whoKnows : controller.getGit().stashList().call())
			if (whoKnows.getShortMessage().startsWith(currentBranch)) {
				controller.getGit().stashApply().setStashRef(whoKnows.getName()).call();
				break;
			}
	}

	public String getDiff(RepositoryController controller) throws GitAPIException {
		OutputStream out = new ByteArrayOutputStream();
		String diff = new String();

		controller.getGit().diff().setOutputStream(out).call();
		diff = out.toString();

		if (diff == null || diff.isEmpty())
			diff = new String();

		return diff;
	}

	public List<String> getUnstaged(Status status) {
		List<String> unstagedFiles = new ArrayList<>();
		unstagedFiles.addAll(status.getModified());
		unstagedFiles.addAll(status.getUntracked());
		unstagedFiles.addAll(status.getMissing());

		Collections.sort(unstagedFiles);

		return unstagedFiles;
	}

	public List<String> getStaged(Status status) {
		List<String> stagedFiles = new ArrayList<>();
		stagedFiles.addAll(status.getChanged());
		stagedFiles.addAll(status.getAdded());
		stagedFiles.addAll(status.getRemoved());

		Collections.sort(stagedFiles);

		return stagedFiles;
	}

	public void stageFile(Git git, String file) throws NoFilepatternException, GitAPIException {
		git.add().addFilepattern(file).call();
	}

	public void unstageFile(Git git, String file) throws NoFilepatternException, GitAPIException {
		git.reset().addPath(file).call();
	}
}
