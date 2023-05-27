package com.example.miniapppointsofinterest.model.miniappCommand;

import java.util.Date;
import java.util.Map;


public class MiniAppCommandBoundary {

	private MiniAppCommandID commandId;
	private String command;
	private TargetObject targetObject;
	private Date invocationTimestamp;
	private InvocationUser invokedBy;
    private Map<String, Object> commandAttributes;
	
	public MiniAppCommandBoundary() {}

	public MiniAppCommandBoundary(MiniAppCommandID commandId, String command, TargetObject targetObject,
			Date invocationTimestamp, InvocationUser invokedBy, Map<String, Object> commandAttributes) {
		this.commandId = commandId;
		this.command = command;
		this.targetObject = targetObject;
		this.invocationTimestamp = invocationTimestamp;
		this.invokedBy = invokedBy;
		this.commandAttributes = commandAttributes;
	}


	public MiniAppCommandID getCommandId() {
		return commandId;
	}


	public void setCommandId(MiniAppCommandID commandId) {
		this.commandId = commandId;
	}


	public String getCommand() {
		return command;
	}


	public void setCommand(String command) {
		this.command = command;
	}


	public TargetObject getTargetObject() {
		return targetObject;
	}


	public void setTargetObject(TargetObject targetObject) {
		this.targetObject = targetObject;
	}


	public Date getInvocationTimestamp() {
		return invocationTimestamp;
	}


	public void setInvocationTimestamp(Date invocationTimestamp) {
		this.invocationTimestamp = invocationTimestamp;
	}


	public InvocationUser getInvokedBy() {
		return invokedBy;
	}


	public void setInvokedBy(InvocationUser invokedBy) {
		this.invokedBy = invokedBy;
	}


	public Map<String, Object> getCommandAttributes() {
		return commandAttributes;
	}


	public void setCommandAttributes(Map<String, Object> commandAttributes) {
		this.commandAttributes = commandAttributes;
	}


	@Override
	public String toString() {
		return "MiniAppCommandBoundary [commandID=" + commandId + ", command=" + command + ", targetObject="
				+ targetObject + ", invocationTimestamp=" + invocationTimestamp + ", invokedBy=" + invokedBy
				+ ", commandAttributes=" + commandAttributes + "]";
	}
	
}