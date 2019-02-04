package com.techm.eclipse.jgitservice;

import java.io.File;

public interface JGitService {
	
	public void createGitRepository(File localPath);
	
	public void accessExistingRepository(String path);
	
	public void cloneGitRepository(String path);
	
	public void cloneGitRepositoryWithBranches(String path, String branchPath);
	
	public void pushToGitRepositiry(String REMOTE_URL, File localPath, File localPath2);
	
	public void pushToGit(File localPath1,File localPath2, String path);
	
	//public boolean validateUser(String username, String password);

}
