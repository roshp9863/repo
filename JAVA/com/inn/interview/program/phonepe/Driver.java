package com.inn.interview.program.phonepe;

import java.util.*;

enum IssueType {
    PAYMENT_RELATED,
    MUTUAL_FUND_RELATED,
    GOLD_RELATED,
    INSURANCE_RELATED
}

enum Status {
    COMPLETED,
    PENDING,
    IN_PROGRESS,
    ASSIGNED,
    REJECTED
}

class Issue {
	private String transactionId;
    private IssueType issueType;
    private String subject;
    private String description;
    private String email;
    private String issueId;
    private Status status;
    private String resolution;
 
    public Issue(String transactionId, IssueType issueType, String subject, String description, String email) {
        this.transactionId = transactionId;
        this.issueType = issueType;
        this.subject = subject;
        this.description = description;
        this.email = email;
        this.issueId = "I" + transactionId; 
        this.status = Status.PENDING; 
        this.resolution = ""; 
    }

    public String getIssueId() {
        return issueId;
    }

    public Status getStatus() {
        return status;
    }

    public String getEmail() {
        return email;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }
    
    public IssueType getIssueType() {
    	return this.issueType;
    }
    
    @Override
	public String toString() {
		return "Issue [transactionId=" + transactionId + ", issueType=" + issueType + ", subject=" + subject
				+ ", description=" + description + ", email=" + email + ", issueId=" + issueId + ", status=" + status
				+ ", resolution=" + resolution + "]";
	}


}

class Agent {

	private String agentEmail;
    private String agentName;
    private List<IssueType> expertise;
    private Issue issue;

    public Agent(String agentEmail, String agentName, List<IssueType> expertise) {
        this.agentEmail = agentEmail;
        this.agentName = agentName;
        this.expertise = expertise;
    }
    
    public void setIssue(Issue issue) {
    	this.issue=issue;
    }
    
    public Issue getIssue() {
    	return this.issue;
    }

    
    public String getAgentEmail() {
        return agentEmail;
    }

    public String getAgentName() {
        return agentName;
    }

    public List<IssueType> getExpertise() {
        return this.expertise;
    }
    
    @Override
	public String toString() {
		return "Agent [agentEmail=" + agentEmail + ", agentName=" + agentName + ", expertise=" + expertise + ", issue="
				+ issue + "]";
	}

}

class IssueResolutionSystem {
    private List<Issue> issues = new ArrayList<>();
    private List<Agent> agents = new ArrayList<>();

    public String createIssue(String transactionId, IssueType issueType, String subject, String description, String email) {
        Issue issue = new Issue(transactionId, issueType, subject, description, email);
        issues.add(issue);
        return issue.getIssueId();
    }

    public void addAgent(String agentEmail, String agentName, List<IssueType> issueType) {
        Agent agent = new Agent(agentEmail, agentName, issueType);
        agents.add(agent);
    }

	public void assignIssue(String issueId) {
        Issue assignedIssue = getIssueById(issueId);

        for (Agent agent : agents) {
            if (isAgentFree(agent) && agent.getExpertise().contains(assignedIssue.getIssueType())) {
                agent.setIssue(assignedIssue);
                
                assignedIssue.setStatus(Status.ASSIGNED);
                
                System.out.println("Issue " + issueId + " assigned to agent " + agent.getAgentName());
                
                return;
            }
        }
        System.out.println("No free agents available to assign the issue " + issueId);
    }

    private boolean isAgentFree(Agent agent) {
    	if(agent.getIssue()!=null) {
    		return false;
    	}
       
        return true;
    }

    private Issue getIssueById(String issueId) {
        for (Issue issue : issues) {
            if (issue.getIssueId().equals(issueId)) {
                return issue;
            }
        }
        return null; 
    }
    
    public List<Issue> getIssues(Map<String, String> filter) {
    	
    	List<Issue> filteredIssues = new ArrayList<>();
    	List<Issue> issueList = getAllIssue();
    	
    	for (Issue issue : issueList) {
            boolean match = true;
            for (Map.Entry<String, String> entry : filter.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                switch (key) {
                    case "type":
                        if (!issue.getIssueType().toString().equals(value)) {
                            match = false;
                        }
                        break;
                    case "status":
                        if (!issue.getStatus().toString().equals(value)) {
                            match = false;
                        }
                        break;
                    case "email":
                        if (!issue.getEmail().equals(value)) {
                            match = false;
                        }
                        break;
                    default:
                        break;
                }

                if (!match) {
                    break;
                }
            }

            if (match) {
                filteredIssues.add(issue);
            }
        }
    	
    	return filteredIssues;
    	
    }


	private List<Issue> getAllIssue() {
		return issues;
	}

	public void updateIssue(String issueId, Status status, String resolution) {
        Issue issue = getIssueById(issueId);
        if (issue != null) {
            issue.setStatus(status);
            issue.setResolution(resolution);
            System.out.println("Issue " + issueId + " updated with status: " + status + " and resolution: " + resolution);
        } else {
            System.out.println("Issue " + issueId + " not found");
        }
    }

	public void resolveIssue(String issueId, String resolution) {
        Issue issue = getIssueById(issueId);
        if (issue != null) {
            issue.setStatus(Status.COMPLETED);
            issue.setResolution(resolution);
            System.out.println("Issue " + issueId + " updated with status: " + Status.COMPLETED + " and resolution: " + resolution);
        } else {
            System.out.println("Issue " + issueId + " not found");
        }
    }

	
}

public class Driver {
    public static void main(String[] args) {
        IssueResolutionSystem system = new IssueResolutionSystem();

        String issueId1 = system.createIssue("T1", IssueType.PAYMENT_RELATED, "Payment Failed", "My payment failed but money is debited", "testUser1@test.com");
        System.out.println("Issue " + issueId1 + " created");

        String issueId2 = system.createIssue("T2", IssueType.MUTUAL_FUND_RELATED, "Purchase Failed", "Unable to purchase Mutual Fund", "testUser2@test.com");
        System.out.println("Issue " + issueId2 + " created");

        system.addAgent("agent1@test.com", "Agent 1", Arrays.asList(IssueType.PAYMENT_RELATED, IssueType.GOLD_RELATED));
        System.out.println("Agent added");

        system.assignIssue(issueId1);
        
        Map<String, String> map = new HashMap<>();
        map.put("type", "PAYMENT_RELATED");
        
        system.updateIssue(issueId1, Status.PENDING, "Waiting");
        
        system.resolveIssue(issueId1, "RESOLVED");
        
        System.out.println(system.getIssues(map).get(0));

    }
}