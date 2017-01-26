package com.prototype.upload;

public class FileLinked {
	
	// Properties
	private String fileName;
	private String filePath;
	private Boolean dc;
	private Boolean tei;
	private Boolean mpeg7_vd;
	private Boolean mpeg7_caliph;
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
	public Boolean getMpeg7Vd() {
		return mpeg7_vd;
	}
	public Boolean getMpeg7Caliph() {
		return mpeg7_caliph;
	}
	public void setMpeg7Vd(Boolean mpeg7_vd) {
		this.mpeg7_vd = mpeg7_vd;
	}
	public void setMpeg7Caliph(Boolean mpeg7_caliph) {
		this.mpeg7_caliph = mpeg7_caliph;
	}
	public Boolean getLinkedmdr() {
		return linkedmdr;
	}
	public void setLinkedmdr(Boolean linkedmdr) {
		this.linkedmdr = linkedmdr;
	}
}
