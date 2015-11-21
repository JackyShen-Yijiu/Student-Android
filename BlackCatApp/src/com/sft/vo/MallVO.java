package com.sft.vo;

import java.util.List;

import cn.sft.sqlhelper.DBVO;

public class MallVO extends DBVO {

	private static final long serialVersionUID = 1L;

	private List<ProductVO> mainlist;
	private List<ProductVO> toplist;

	public List<ProductVO> getMainlist() {
		return mainlist;
	}

	public void setMainlist(List<ProductVO> mainlist) {
		this.mainlist = mainlist;
	}

	public List<ProductVO> getToplist() {
		return toplist;
	}

	public void setToplist(List<ProductVO> toplist) {
		this.toplist = toplist;
	}

}
