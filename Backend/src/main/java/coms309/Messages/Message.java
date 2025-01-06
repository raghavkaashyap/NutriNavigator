package coms309.Messages;

public class Message {
    private int id;
    private int resolved;
    private String userSubmitted;
    private String subject;
    private String message;
    private long dateSubmitted;
    private long dateResolved;
    private String userResolved;

    public Message(int id, int resolved, String userSubmitted, String subject, String message, long dateSubmitted, long dateResolved, String userResolved) {
        this.id = id;
        this.resolved = resolved;
        this.userSubmitted = userSubmitted;
        this.subject = subject;
        this.message = message;
        this.dateSubmitted = dateSubmitted;
        this.dateResolved = dateResolved;
        this.userResolved = userResolved;
    }

    public Message(int resolved, String userSubmitted, String subject, String message, long dateSubmitted, long dateResolved, String userResolved) {
        this(-1, resolved, userSubmitted, subject, message, dateSubmitted, dateResolved, userResolved);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setResolved(int resolved) {
        this.resolved = resolved;
    }

    public void setUserSubmitted(String userSubmitted) {
        this.userSubmitted = userSubmitted;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDateSubmitted(long dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public void setDateResolved(long dateResolved) {
        this.dateResolved = dateResolved;
    }

    public void setUserResolved(String userResolved) {
        this.userResolved = userResolved;
    }

    public int getId() {
        return id;
    }

    public int getResolved() {
        return resolved;
    }

    public String getUserSubmitted() {
        return userSubmitted;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public long getDateSubmitted() {
        return dateSubmitted;
    }

    public long getDateResolved() {
        return dateResolved;
    }

    public String getUserResolved() {
        return userResolved;
    }
}
