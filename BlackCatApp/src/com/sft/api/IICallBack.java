package com.sft.api;
/**
 * 处理其他问题
 * @author pengdonghua
 *
 */
public interface IICallBack extends cn.sft.listener.ICallBack{

	public void doError(String type,String msg);
	
}
