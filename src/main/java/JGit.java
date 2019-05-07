
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class JGit {

	static String LOCAL_DIR = "";
	/*static String username = "swastik93";
	static String pass = "tilak@93";*/
	
	static String username = "sayan.kundu";
	static String pass = "Kolkata123#";
	//C://Users//UX015793//Downloads//GIT
	//static String GIT_URL = "https://github.com/swastik93/NewTestingGit.git";
	static String GIT_URL = "https://git.sami.int.thomsonreuters.com/Sayan.Kundu/TestGit.git";
	
	public static void main(String[] args) {
		// addToGitRepository(String fileName, String path, String fileContent, String userName, String password)
		//System.out.println(addToGitRepository("testfile.groovy", "C://Users//UX015454//Downloads//SAMIGIT", "groovy content 1", username, pass));
		System.out.println(addExistingFileToGitRepository("C://Users//UX015454//Downloads//SAMIGIT",username,pass,"testfile.groovy","new update added"));

	}

	
	public static boolean addExistingFileToGitRepository(String localPath, String USERNAME, String PASSWORD, String fileName, String fileContent) {
		try {

			//Files.delete(new File(localPath).toPath());
			Git git = Git.open(new File(localPath));

			
			//Git git = Git.init().setDirectory(new File(localPath)).call();
			System.out.println("Git initialised...");
			/*
			StoredConfig config = git.getRepository().getConfig();
			config.setString("remote", "origin", "url", "https://git.sami.int.thomsonreuters.com//Sayan.Kundu//TestGit.git");
			config.save();
			System.out.println("Git configured...");
*/			

			//System.out.println("Created repository: " + git.getRepository().getDirectory());
			File myFile = new File(git.getRepository().getDirectory().getParent(), fileName);
			if (!myFile.createNewFile()) {
				System.out.println("new file creation failed in local git repo...");
				throw new IOException("Could not create file " + myFile);
			}
			else {
				System.out.println("File is being written...");
				BufferedWriter writer = new BufferedWriter(new FileWriter(myFile));
			    writer.write(fileContent);
			    writer.close();
			}
			//git.checkout().setCreateBranch(true).setName("branch9").call();
			AddCommand add = git.add();
			
				add.addFilepattern(".").call();
				CommitCommand commit = git.commit();
				commit.setMessage("third commit").call();
				try {
					PushCommand pushCommand = git.push();
					pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(USERNAME, PASSWORD));
					pushCommand.call();

					Iterable<RevCommit> log = git.log().call();
					for (RevCommit revCommit : log) {
						System.out.println(revCommit.getCommitTime() + " " + revCommit.getFullMessage() + "");
					}
				} catch (GitAPIException e) {
					throw new RuntimeException(e);
				}
			} catch (NoFilepatternException e) {
				e.printStackTrace();
			} catch (GitAPIException e) {
				e.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			}
		
		return true;
	}

	
	public static boolean createGitRepository(String fileName, String path, String fileContent) {

		System.out.println("Inside createGitRepository() ...");
		System.out.println("fileName : "+fileName);
		System.out.println("path : "+path);

		if (prepareFolder()) {

			try {
				
				Git git = Git.init().setDirectory(new File(LOCAL_DIR)).call();
				System.out.println("Git initialised...");
				
				StoredConfig config = git.getRepository().getConfig();
				config.setString("remote", "origin", "url", GIT_URL);
				config.save();
				System.out.println("Git configured...");
				

				//System.out.println("Created repository: " + git.getRepository().getDirectory());
				File myFile = new File(git.getRepository().getDirectory().getParent(), fileName);
				if (!myFile.createNewFile()) {
					System.out.println("new file creation failed in local git repo...");
					throw new IOException("Could not create file " + myFile);
				}
				
				else {
					System.out.println("File is being written...");
					BufferedWriter writer = new BufferedWriter(new FileWriter(myFile));
				    writer.write(fileContent);
				    writer.close();
				}

				// run the add-call git.add().addFilepattern("enhanceCPS.txt").call();

				git.commit().setMessage("Initial commit").call();
				
				System.out.println("Committed file " + myFile + " to repository at " + git.getRepository().getDirectory());
				
				// Create a few branches for testing
				git.checkout().setCreateBranch(true).setName("branch10").call();
				System.out.println("git checked out...");
				
				// List all branches
				List<Ref> call = git.branchList().call();
				for (Ref ref : call) {
					System.out.println("Branch: " + ref + " " + ref.getName() + " " + ref.getObjectId().getName());
				}

				// Create a few new file
				
				/*File f = new File(git.getRepository().getDirectory().getParent(), "fileName");
				f.createNewFile();*/
				
				git.add().addFilepattern(fileName).call();

				// committed file
				Status status = git.status().call();
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
				ObjectId lastCommitId =	git.getRepository().resolve(Constants.HEAD);
				System.out.println("Head points to the following commit :" + lastCommitId.getName());
				
				
				git.close();
				return true;
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			
		}
		return false;
	}
	
	
	public static boolean addToGitRepository(String fileName, String path, String fileContent, String userName, String password) {

		LOCAL_DIR = path;
		//path is local path
		System.out.println("Inside addToGitRepository() ...");
		System.out.println("fileName : "+fileName);
		System.out.println("path : "+path);
		System.out.println("userName : "+userName);
		
		if (createGitRepository(fileName, path, fileContent)) {

			try {

				Git git = Git.open(new File(LOCAL_DIR));

				AddCommand add = git.add();
				try {
					add.addFilepattern(fileName).call();
					CommitCommand commit = git.commit();
					commit.setMessage("latest commit").call();
					System.out.println("committed");
					try {
						
						System.out.println("pushing to Git");
						PushCommand pushCommand = git.push();
						pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(userName, password));
						pushCommand.call();

						Iterable<RevCommit> log = git.log().call();
						for (RevCommit revCommit : log) {
							//System.out.println(revCommit.getCommitTime() + " " + revCommit.getFullMessage() + "");
						}
						
						System.out.println("Removing temporary local directory : " + LOCAL_DIR);
						//new File(LOCAL_DIR).delete();
						return true;
					} catch (GitAPIException e) {
						throw new RuntimeException(e);
					}
				} catch (NoFilepatternException e) {
					e.printStackTrace();
				} catch (GitAPIException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	
	
	}
	
	public static boolean prepareFolder() {
		System.out.println("Creating temp directory ..." + LOCAL_DIR);

		try {
			File localPath = new File(LOCAL_DIR);
			if (localPath.exists()) {
				System.out.println("Directory  exists..." + LOCAL_DIR);
				localPath.delete();
			}
			if (localPath.mkdirs()) {
				System.out.println("Temp directory creation success..." + LOCAL_DIR);
				return true;
			}

		} catch (Exception e) {
			System.out.println("Temp directory creation failed..." + e);
			e.printStackTrace();
		}
		System.out.println("Temp directory creation failed...");
		return false;

	}

}