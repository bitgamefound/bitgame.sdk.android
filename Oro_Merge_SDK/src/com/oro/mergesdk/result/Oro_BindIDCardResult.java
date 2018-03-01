package com.oro.mergesdk.result;
/**
 * 调用绑定实名信息后，服务端返回的实体Data
 * @author Administrator
 *
 */
public class Oro_BindIDCardResult
{
	private int status;
	private Oro_BindIDCardResultData data;

	public int getStatus()
	{
		return this.status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public Oro_BindIDCardResultData getData()
	{
		return this.data;
	}

	public void setData(Oro_BindIDCardResultData data)
	{
		this.data = data;
	}
}
