package controller;

public class SendEvent {
    private String content;

    public SendEvent() {
    }
    public SendEvent(String content) {
        this.setContent(content);
    }
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}