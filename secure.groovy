// Harden Jenkins and remove all the nagging warnings in the web interface
import jenkins.model.Jenkins
import jenkins.security.s2m.*

Jenkins jenkins = Jenkins.getInstance()

// Disable remoting
jenkins.getDescriptor("jenkins.CLI").get().setEnabled(false)

// Enable Agent to master security subsystem
jenkins.injector.getInstance(AdminWhitelistRule.class).setMasterKillSwitch(false);

// Disable jnlp
jenkins.setSlaveAgentPort(-1);

// Disable old Non-Encrypted protocols
HashSet<String> newProtocols = new HashSet<>(jenkins.getAgentProtocols());
newProtocols.removeAll(Arrays.asList(
        "JNLP3-connect", "JNLP2-connect", "JNLP-connect", "CLI-connect"
));
jenkins.setAgentProtocols(newProtocols);
jenkins.save()
