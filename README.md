# jenkins-scripts

Groovy scripts for configuring Jenkins via Ansible.

* Add a folder
* Add a basic Job (Jenkins calls these projects ... I don't)
* Add a multibranch pipeline job.
* Add a job from XML file (This lets you put it in a folder which the Ansible `jenkins_job` module does not)
* Harden a Jenkins server by disabling all the insecure stuff that a default instalation complains about.

Jenkins has various other ways to achive the above ([JobDSL plugin](https://github.com/jenkinsci/job-dsl-plugin), [Python Jenkins API bindings](https://pypi.python.org/pypi/jenkinsapi)), but they all have shortcommings so I prefer to use the Jenkins Java API directly. It's pitty there is no documentation for this approach because it's always possible to configure anything you find in the (horrible) Jenkins GUI because that GUI uses the same APIs.

These scripts were used with the `jenkins_script` Ansible module:

http://docs.ansible.com/ansible/latest/jenkins_script_module.html

Example usage with Ansible:

```
- name: Add Jenkins folder per environment
  jenkins_script:
    script: "{{ lookup('file', 'templates/add-folder.groovy') }}"
    args:
      folderName: "{{ job.key }}"
    url: "{{ jenkins_url }}"
    user: "{{ jenkins_admin_username }}"
    password: "{{ jenkins_admin_password }}"
  with_dict: "{{ example_environments }}"
  loop_control:
    loop_var: job
    label: "{{ job.key }}"

- name: Add deployment pipeline
  jenkins_script:
    script: "{{ lookup('file', 'templates/add-multibranch-pipeline-job.groovy') }}"
    args:
      folderName: "{{ job.key }}"
      jobName: example-deploy
      jobScript: jenkins/example-deploy.groovy
      gitRepo: "{{ example_environment_repos.example.repo }}"
      gitRepoName: example
      credentialsId: "{{ example_users.bot.id }}"
    url: "{{ jenkins_url }}"
    user: "{{ jenkins_admin_username }}"
    password: "{{ jenkins_admin_password }}"
  with_dict: "{{ example_environments }}"
  loop_control:
    loop_var: job
    label: "{{ job.key }}"
```
