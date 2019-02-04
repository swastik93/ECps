package com.techm.eclipse.jgitservice;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

/**
 * @author MS00600412
 *
 */
public class JGitServiceImpl implements JGitService {

	Git git;
	Status status;
	
	@Override
	public void createGitRepository(File localPath) {

		try {
			Files.delete(localPath.toPath());
			git = Git.init().setDirectory(localPath).call();

			System.out.println("Created repository: " + git.getRepository().getDirectory());
			File myFile = new File(git.getRepository().getDirectory().getParent(), "enhanceCPS.txt");
			if (!myFile.createNewFile()) {
				throw new IOException("Could not create file " + myFile);
			}

			// run the add-call
			git.add().addFilepattern("enhanceCPS.txt").call();

			git.commit().setMessage("Initial commit").call();
			System.out.println("Committed file " + myFile + " to repository at " + git.getRepository().getDirectory());

			// Create a few branches for testing
			git.checkout().setCreateBranch(true).setName("new-branch").call();

			// List all branches
			List<Ref> call = git.branchList().call();
			for (Ref ref : call) {
				System.out.println("Branch: " + ref + " " + ref.getName() + " " + ref.getObjectId().getName());
			}

			// Create a few new file
			File f = new File(git.getRepository().getDirectory().getParent(), "enhanceCPS.txt");
			f.createNewFile();
			git.add().addFilepattern("enhanceCPS.txt").call();

			// committed file
			Set<String> added = status.getAdded();
			for (String add : added) {
				System.out.println("Added: " + add);
			}
			// uncommitted files
			Set<String> uncommittedChanges = status.getUncommittedChanges();
			for (String uncommitted : uncommittedChanges) {
				System.out.println("Uncommitted: " + uncommitted);
			}

			// untracked file
			Set<String> untracked = status.getUntracked();
			for (String untrack : untracked) {
				System.out.println("Untracked: " + untrack);
			}

			// Find the head for the repository
			ObjectId lastCommitId = git.getRepository().resolve(Constants.HEAD);
			System.out.println("Head points to the following commit :" + lastCommitId.getName());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			git.close();
		}
	}

	/**
	 * This code would allow to access an existing repository
	 * 
	 * @param path
	 *            : complete path of the repository
	 */

	@Override
	public void accessExistingRepository(String path) {

		try {
			git = Git.open(new File(path));
			Repository repository = git.getRepository();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			git.close();
		}

	}

	/**
	 * Git Repository clonning
	 * 
	 */
	@Override
	public void cloneGitRepository(String path) {

		try {
			status = git.status().call();
			Git.cloneRepository().setURI("https://github.com/swastik93/Dump")
					.setDirectory(new File("d://SWASTIK_BIN//CPSGitPart2")).call();

			// Status status = git.status().call();
			System.out.println("Current status :" + status.getChanged());

		} catch (Exception ee) {
			System.out.println(ee.getMessage());
		}
	}

	@Override
	public void cloneGitRepositoryWithBranches(String path, String branchPath) {
		try {
			status = git.status().call();
			Git.cloneRepository()
			  .setURI("https://github.com/swastik93/Dump")
			  .setDirectory(new File("/path/to/repo"))
			  .setBranchesToClone(Arrays.asList("refs/heads/specific-branch"))
			  .setBranch("refs/heads/specific-branch")
			  .call();
			System.out.println("Current status :" + status.getChanged());

		} catch (Exception ee) {
			System.out.println(ee.getMessage());
		}

	}

	@Override
	public void pushToGitRepositiry(String REMOTE_URL, File localPath, File localPath2) {
		 // prepare a new folder for the cloned repository
		/*
        File localPath = File.createTempFile("TestGitRepository", "");
        if(!localPath.delete()) {
            throw new IOException("Could not delete temporary file " + localPath);
        }*/
        try {
        // then clone
        System.out.println("Cloning from " + REMOTE_URL + " to " + localPath);
        Git result = Git.cloneRepository()
                .setURI(REMOTE_URL)
                .setDirectory(localPath)
                .call();
  
            // prepare a second folder for the 2nd clone
    
            // then clone again
            System.out.println("Cloning from file://" + localPath + " to " + localPath2);
            Git result2 = Git.cloneRepository()
                    .setURI("file://" + localPath)
                    .setDirectory(localPath2)
                    .call();
            
   
                System.out.println("Result: " + result2);

                // now open the created repository
                FileRepositoryBuilder builder = new FileRepositoryBuilder();
                Repository repository = builder.setGitDir(localPath2)
                        .readEnvironment() // scan environment GIT_* variables
                        .findGitDir() // scan up the file system tree
                        .build();
       
                	Git git = new Git(repository);
              
                        git.push()
                                .call();
                    
                    System.out.println("Pushed from repository: " + repository.getDirectory() + " to remote repository at " + REMOTE_URL);
               
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}finally {
			git.close();
		}
		
	}
	
	

	@Override
	public boolean validateUser(String username, String password) {
		
		if(username != null && password != null) {
			if(!(username.equals("techm"))) {
				return false;
			}else {
				if((password.equals("techm123"))) {
					return true;
				}else {
					return false;
				}
			}
		}else {
			System.out.println("Please enter username");
		}
		
		return false;
	}

	@Override
	public void pushToGit(File localPath1,File localPath2, String path) {
		
		try {
		/*FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = builder.setGitDir(localPath1)
                .readEnvironment() // scan environment GIT_* variables
                .findGitDir() // scan up the file system tree
                .build();
        */
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
          if (listOfFiles[i].isFile()) {
            System.out.println("File " + listOfFiles[i].getName());
          } else if (listOfFiles[i].isDirectory()) {
            System.out.println("Directory " + listOfFiles[i].getName());
          }
        }
   /*     try{*/
        	//Git git = new Git(repository);
          /*  try {*/
                //git.push()
                //        .call();
            /*}finally {
            	git.close();
            }
*/
            System.out.println("Pushed from repository: " + /*repository.getDirectory() +*/ " to remote repository at " + localPath2);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
