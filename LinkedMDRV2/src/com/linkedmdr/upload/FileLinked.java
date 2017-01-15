package com.linkedmdr.upload;

public class FileLinked {
	
	// Properties
	private String fileName;
	private String filePath;
	private Boolean dc;
	private Boolean tei;
	private Boolean mpeg7;
	private Boolean linkedmdr;
	
	// Set&Get
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public Boolean getDc() {
		return dc;
	}
	public void setDc(Boolean dc) {
		this.dc = dc;
	}
	public Boolean getTei() {
		return tei;
	}
	public void setTei(Boolean tei) {
		this.tei = tei;
	}
	public Boolean getMpeg7() {
		return mpeg7;
	}
	public void setMpeg7(Boolean mpeg7) {
		this.mpeg7 = mpeg7;
	}
	public Boolean getLinkedmdr() {
		return linkedmdr;
	}
	public void setLinkedmdr(Boolean linkedmdr) {
		this.linkedmdr = linkedmdr;
	}
}
