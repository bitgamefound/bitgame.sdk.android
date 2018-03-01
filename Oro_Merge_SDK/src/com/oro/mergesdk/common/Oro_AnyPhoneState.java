package com.oro.mergesdk.common;
/**
 * 手机状�?信息
 * @author Administrator
 *
 */
public class Oro_AnyPhoneState {
	public String imei;
	public String iesi;
	public String phoneType;
	public String mac;
	public int screenHight;
	public int screenWidth;
	public String cpu_max;
	public String cpu_avaliable;
	public String ram_max;
	public String ram_avaliable;
	public String system_version;
	public String Package;
	public String Version;
	
	public String getSystem_version() {
		return system_version;
	}
	public void setSystem_version(String sysVersion) {
		this.system_version = sysVersion;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getIesi() {
		return iesi;
	}
	public void setIesi(String iesi) {
		this.iesi = iesi;
	}
	public String getPhoneType() {
		return phoneType;
	}
	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}
	public int getScreenHight() {
		return screenHight;
	}
	public void setScreenHight(int screenHight) {
		this.screenHight = screenHight;
	}
	public int getScreenWidth() {
		return screenWidth;
	}
	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getCpu_max() {
		return cpu_max;
	}
	public void setCpu_max(String cpu_max) {
		this.cpu_max = cpu_max;
	}
	public String getCpu_avaliable() {
		return cpu_avaliable;
	}
	public void setCpu_avaliable(String cpu_avaliable) {
		this.cpu_avaliable = cpu_avaliable;
	}
	public String getRam_max() {
		return ram_max;
	}
	public void setRam_max(String ram_max) {
		this.ram_max = ram_max;
	}
	public String getRam_avaliable() {
		return ram_avaliable;
	}
	public void setRam_avaliable(String ram_avaliable) {
		this.ram_avaliable = ram_avaliable;
	}
	public String getPackage() {
		return Package;
	}
	public void setPackage(String spackage) {
		this.Package = spackage;
	}
	public String getVersion() {
		return Version;
	}
	public void setVersion(String version) {
		this.Version = version;
	}
}
