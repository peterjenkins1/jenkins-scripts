// Adds legacy XML defined jobs to a folder
// This can be removed when all our jobs are 100% groovy
import jenkins.model.Jenkins
import com.cloudbees.hudson.plugins.folder.*

// Bring some values in from ansible using the jenkins_script modules wierd "args" approach (these are not gstrings)
def folderName = "$folderName"
def jobName = "$jobName"
def jobXml = '''$jobXml'''

Jenkins jenkins = Jenkins.instance // saves some typing

// Get the folder where this job should be
def folder = jenkins.getItem(folderName)
// Create the folder if it doesn't exist
if (folder == null) {
  folder = jenkins.createProject(Folder.class, folderName)
}

//def xmlStream = new ByteArrayInputStream( jobXml.getBytes() )
def xmlStream = new StringBufferInputStream(jobXml)

// Check if the job already exists
def job = folder.getItem(jobName)
// Remove it if it already exists
if (job != null) {
  folder.remove(job)
}
// Create job in the folder
job = folder.createProjectFromXML(jobName, xmlStream)
