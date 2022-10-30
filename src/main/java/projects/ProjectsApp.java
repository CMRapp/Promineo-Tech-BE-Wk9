package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;

/**
 * This class contains the "main" method - the entry point to a Java
 * application.
 * 
 * @author Christian M Rapp
 *
 */
public class ProjectsApp {

	private Scanner scanner = new Scanner(System.in);
	private ProjectService projectService = new ProjectService(); 
	
	
	// @formatter:off
		private List<String> operations = List.of(
			"1) Add a project"
		);
		// @formatter:on
	
	public static void main(String[] args) {
		
		new ProjectsApp().processUserSelections();

	}// end main
		
	
	/**
	 * This method prints the menu of available operations, gets the user input and performs
	 * the requested operation. This process repeats until the user chooses to exit the program.
	 */
	private void processUserSelections() {
			boolean done = false;
			
			while (!done) {
				try {
					int selection = getUserSelection();
					
					switch(selection) {
					case -1:
						done = exitMenu();
						break;
						
					case 1:
						createProject();
						break;
						
					default:
						System.out.println("\n" + selection + " is not a valid selection. Try again.");
						break;
					}
					
				}
				catch(Exception e) {
					System.out.println("\nError: " + e + " Try again.");
				}
			}			
		}// end processUserSelections
		
		/**
		 * Get user input and call project service to create the row in the database.
		 */
		private void createProject() {
			String projectName = getStringInput("Enter the project name");
			BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
			BigDecimal actualHours = getDecimalInput("Enter the actual hours");
			Integer difficulty = getIntInput("Enter the project difficulty (1-5)");
			String notes = getStringInput("Enter the project notes");
			
			Project project = new Project();
			
			project.setProjectName(projectName);
			project.setEstimatedHours(estimatedHours);
			project.setActualHours(actualHours);
			project.setDifficulty(difficulty);
			project.setNotes(notes);
			
			Project dbProject = projectService.addProject(project);
			System.out.println("You have successfully created project: " + dbProject);
		
		}


		// This method get's the user's selection
		private int getUserSelection() {
			printOperations();
			Integer input = getIntInput("Enter a menu selection");
			return Objects.isNull(input) ? -1 : input;
		}// end getUserSelection
		
		
		// This method displays a prompt and gets a user's input which is converted to an Integer
		private Integer getIntInput(String prompt) {
			String input = getStringInput(prompt);
			
			if (Objects.isNull(input)) {
				return null;
			}
			
			try {
				return Integer.valueOf(input);
			}
			
			catch(NumberFormatException e) {
				throw new DbException(input + " is not a valid number.");
			}
			
		}
		
		
		// This method get's the user's selection and converts it to BigDecimal
		private BigDecimal getDecimalInput(String prompt) {
			String input = getStringInput(prompt);
			
			if (Objects.isNull(input)) {
				return null;
			}
			
			try {
				return new BigDecimal(input).setScale(2);
			}
			
			catch(NumberFormatException e) {
				throw new DbException(input + " is not a valid decimal number.");
			}
		}

		
		
		/**
		 *  This method prints the prompt and gets the user's input. If user enters nothing, null is returned. 
		 * Otherwise, user input is trimmed.
		 */
		
		private String getStringInput(String prompt) {
			System.out.print(prompt + ": ");
			String input = scanner.nextLine();
			
			return input.isBlank() ? null : input.trim();			
		}

		
		// This method prints to the screen the available operations, each on a separate line
		private void printOperations() {
			System.out.println("\nThese are the available selections. Press the ENTER key to quit:");
			operations.forEach(line -> System.out.println("   " + line));
			
		}
		
		// prints an exit message to the screen and returns TRUE
		private boolean exitMenu() {
		    System.out.println("\nExiting the menu.");
		    return true;
		  }

}
