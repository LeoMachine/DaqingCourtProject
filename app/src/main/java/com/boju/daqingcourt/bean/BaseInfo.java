package com.boju.daqingcourt.bean;


import java.io.Serializable;

public class BaseInfo implements Serializable
{
	protected boolean isChoosed;

	public boolean isChoosed()
	{
		return isChoosed;
	}

	public void setChoosed(boolean isChoosed)
	{
		this.isChoosed = isChoosed;
	}

}
