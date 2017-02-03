package com.prototype.upload;

public class FileLinked {
	
	// Properties
	private String fileName;
	
	private String filePath;
	private String filePathDC;
	private String filePathTei;
	private String filePathMpeg7Vd;
	private String filePathLinkedMDR;
	
	private int fileId;
	private Boolean dc;
	private Boolean tei;
	private Boolean mpeg7_vd;
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
	public int getFileId() {
		return fileId;
	}
	public void setFileId(int uniqueFileId) {
		this.fileId = uniqueFileId;
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
	public Boolean getMpeg7Vd() {
		return mpeg7_vd;
	}
	public void setMpeg7Vd(Boolean mpeg7_vd) {
		this.mpeg7_vd = mpeg7_vd;
	}
	public Boolean getLinkedmdr() {
		return linkedmdr;
	}
	public void setLinkedmdr(Boolean linkedmdr) {
		this.linkedmdr = linkedmdr;
	}
	public String getFilePathDC() {
		return filePathDC;
	}
	public void setFilePathDC(String filePathDC) {
		this.filePathDC = filePathDC;
	}
	public String getFilePathTei() {
		return filePathTei;
	}
	public void setFilePathTei(String filePathTei) {
		this.filePathTei = filePathTei;
	}
	public String getFilePathMpeg7Vd() {
		return filePathMpeg7Vd;
	}
	public void setFilePathMpeg7Vd(String filePathMpeg7Vd) {
		this.filePathMpeg7Vd = filePathMpeg7Vd;
	}
	public String getFilePathLinkedMDR() {
		return filePathLinkedMDR;
	}
	public void setFilePathLinkedMDR(String filePathLinkedMDR) {
		this.filePathLinkedMDR = filePathLinkedMDR;
	}
}
