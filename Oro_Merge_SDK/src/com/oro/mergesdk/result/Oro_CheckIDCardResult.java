package com.oro.mergesdk.result;
/**
 * 调用检查实名信息后，服务端返回的实体Data
 * @author Administrator
 *
 */
public class Oro_CheckIDCardResult
{
	private int status;
	private Oro_CheckIDCardResultData data;
	private int code;

	public int getStatus()
	{
		return this.status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public Oro_CheckIDCardResultData getData()
	{
		return this.data;
	}

	public void setData(Oro_CheckIDCardResultData data)
	{
		this.data = data;
	}

	public int getCode()
	{
		return this.code;
	}

	public void setCode(int code)
	{
		this.code = code;
	}
}
