package com.techm.eclipse.jgittest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;

import org.eclipse.jgit.api.errors.GitAPIException;

import com.techm.eclipse.jgitservice.JGitService;
import com.techm.eclipse.jgitservice.JGitServiceImpl;

/**
 * @author MS00600412
 *
 */
public class JgitAdapter {

	static JGitService jGitService;
	static UserDetails user;
	private static final String REMOTE_URL = "https://github.com/eclipse/jgit.git";
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException, IllegalStateException, GitAPIException {

		Scanner sc = new Scanner(System.in);
		user = new UserDetails();
		jGitService =  new JGitServiceImpl();
		
		System.out.println("-------------- Welcome to Enhance CPS ----------------\n");
		
		/*System.out.println("Enter Usernme : ");
		String username = sc.next();
		user.setUsername(username);

		System.out.println("Enter Password : ");
		String password = sc.next();
		user.setUsername(password);

		
		jGitService.validateUser(username, password);*/
		
		System.out.println("\n\t 1. CREATE REPOSITORY");
		System.out.println("\n\t 2. ACCESS REPOSITORY");
		System.out.println("\n\t 3. CLONE REPOSITORY");
		System.out.println("\n\t 4. CLONE REPOSITORY WITH BRANCH");
		System.out.println("\n\t 5. PUSH TO GIT HUB");
		System.out.println("\nEnter your choice : ");
		int input = sc.nextInt();

		switch (input) {
		case 1:
			System.out.println("Enter localPath (with '//') : \t");
			String path = sc.next();
			File localPath = new File(path);
			jGitService.createGitRepository(localPath);
			break;
		case 2:
			System.out.println("Enter repositoryPath (with '//') : \t");
			String repositoryPath = sc.next();
			jGitService.accessExistingRepository(repositoryPath);
			break;
		case 3:
			System.out.println("Enter clonePath (with '//') : \t");
			String clonePath = sc.next();
			jGitService.cloneGitRepository(clonePath);;
			break;
		case 4:
			System.out.println("Enter cloneFolderPath : \t");
			String cloneFolderPath = sc.next();
			System.out.println("Enter cloneBranchPath : \t");
			String cloneBranchPath = sc.next();
			jGitService.cloneGitRepositoryWithBranches(cloneFolderPath, cloneBranchPath);
			break;
		case 5:
			System.out.println("Enter REMOTE PATH : \t");
			String remotePath = sc.next();
			System.out.println("Enter local file path 1 : \t");
			String filePath1 = sc.next();
			 File localPath1 = new File(filePath1);
			 /*Files.delete(localPath1.toPath());
		        if(!localPath1.delete()) {
		            throw new IOException("Could not delete temporary file " + localPath1);
		        }*/
			System.out.println("Enter local file path 2 : \t");
			String filePath2 = sc.next();
			
			File localPath2 = new File(filePath2);
			/*Files.delete(localPath2.toPath());
            if(!localPath2.delete()) {
                throw new IOException("Could not delete temporary file " + localPath2);
            }*/
			//Files.delete(localPath2.toPath());
			jGitService.pushToGitRepositiry(remotePath, localPath1, localPath2);
			break;	
		case 6:
			System.out.println("Enter gitPath2 : \t");
			String gitPath1 = sc.next();
			File localPath001 = new File(gitPath1);
			System.out.println("Enter gitPath2 : \t");
			String gitPath2 = sc.next();
			File localPath002 = new File(gitPath2);
			System.out.println("Enter path : \t");
			String path1 = sc.next();
			jGitService.pushToGit(localPath001, localPath002, path1);
			break;	
			
		default:
			System.out.println("Wrong Entry");
		}

		sc.close();

	}
}
