/*
This adds a folder.

Because we have jobs with the same name as the environments, this code also renames
the folder. That extra code can be removed once this is deployed to jenkins1.

Another temp feature is moving the existing jobs into the folder.

*/
import com.cloudbees.hudson.plugins.folder.*
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import jenkins.model.Jenkins

Jenkins jenkins = Jenkins.instance // saves some typing

// Bring some values in from ansible using the jenkins_script modules wierd "args" approach (these are not gstrings)
String folderName = "$folderName"
String folderNameTemp = folderName + "-folder"

def folder = jenkins.getItem(folderName)
if (folder == null) {
  // Create the folder if it doesn't exist or if no existing job has the same name
  folder = jenkins.createProject(Folder.class, folderName)
} else {
  if (folder.getClass() != Folder.class) {
    // when folderName exists, but is not a folder we make the folder with a temp name
    folder = jenkins.createProject(Folder.class, folderNameTemp)
    // Move existing jobs from the same environment to folders (preseve history)
    Item[] items = jenkins.getItems(WorkflowJob.class)
    def job_regex = "^" + folderName

    items.grep { it.name =~ job_regex }.each { job ->
      Items.move(job, folder)
    }

    // Rename the temp folder now we've moved the jobs
    folder.renameTo(folderName)
  }
}

