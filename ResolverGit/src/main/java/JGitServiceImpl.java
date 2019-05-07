package enhanceJgitService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.StatusCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class JGitServiceImpl implements JGitService {

	
	 * private static String REMOTE_URL = null; private static String USERNAME =
	 * null; private static String PASSWORD = null;
	 

	public JGitServiceImpl() {

	}

	public boolean addToGitRepository(String localPath, String USERNAME, String PASSWORD, String fileName) {
		try {

			Git git = Git.open(new File(localPath));

			
			//Git git = Git.init().setDirectory(new File(localPath)).call();
			System.out.println("Git initialised...");
			
			StoredConfig config = git.getRepository().getConfig();
			config.setString("remote", "origin", "url", "https://git.sami.int.thomsonreuters.com//Sayan.Kundu//TestGit.git");
			config.save();
			System.out.println("Git configured...");
			

			//System.out.println("Created repository: " + git.getRepository().getDirectory());
			File myFile = new File(git.getRepository().getDirectory().getParent(), fileName);
			if (!myFile.createNewFile()) {
				System.out.println("new file creation failed in local git repo...");
				throw new IOException("Could not create file " + myFile);
			}
			AddCommand add = git.add();
			
				add.addFilepattern(".").call();
				CommitCommand commit = git.commit();
				commit.setMessage("first commit").call();
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
		
		return false;
	}

	public void cloneGitRepository(String path, String REMOTE_PATH) {

		try {
			// status = git.status().call();
			StatusCommand status = Git.cloneRepository().setURI(REMOTE_PATH).setDirectory(new File(path)).call()
					.status();

			// Status status = git.status().call();
			System.out.println("Current status :" + status.toString());

		} catch (Exception ee) {
			System.out.println(ee.getMessage());
		}

	}

	public void createGitRepository(File localPath) {

		try {
			Files.delete(localPath.toPath());
			Git git = Git.init().setDirectory(localPath).call();

			System.out.println("Created repository: " + git.getRepository().getDirectory());
			File myFile = new File(git.getRepository().getDirectory().getParent(), "enhanceCPS.txt");
			if (!myFile.createNewFile()) {
				throw new IOException("Could not create file " + myFile);
			}

			// run the add-call git.add().addFilepattern("enhanceCPS.txt").call();

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
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	
	@SuppressWarnings("finally") public String myProperties() { Properties prop =
	  new Properties(); InputStream input = null;
	  
	  try { String filePath = "config.properties"; // String filename =
	  "config.properties"; input =
	 JGitServiceImpl.class.getClassLoader().getResourceAsStream(filePath); if
	  (input == null) { System.out.println("Sorry, unable to find " + filePath);
	  return null; }
	  
	  // load a properties file from class path, inside static method
	  prop.load(input); // get the property value and print it out REMOTE_URL =
	  prop.getProperty("REMOTE_URL"); System.out.println("REMOTE_URL : " +
	  REMOTE_URL); USERNAME = prop.getProperty("USERNAME");
	  System.out.println("USERNAME : " + USERNAME); PASSWORD =
	  prop.getProperty("PASSWORD"); System.out.println("PASSWORD : " + PASSWORD);
	  
	  } catch (IOException ex) { ex.printStackTrace(); } finally { if (input !=
	  null) { try { input.close(); } catch (IOException e) { e.printStackTrace(); }
	  } return REMOTE_URL; }
	  
	  }
	 
}