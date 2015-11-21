package com.sft.vo.commonvo;

import cn.sft.sqlhelper.DBVO;

public class HeadPortrait extends DBVO {

	private static final long serialVersionUID = 1L;

	private String originalpic;
	private String thumbnailpic;
	private String width;
	private String height;

	@Override
	public String toString() {
		String str = "{\"originalpic\":\"" + originalpic + "\",\"thumbnailpic\":\"" + thumbnailpic + "\",\"width\":\""
				+ width + "\",\"height\":\"" + height + "\"}";
		return str;
	}

	public String getOriginalpic() {
		return originalpic;
	}

	public void setOriginalpic(String originalpic) {
		this.originalpic = originalpic;
	}

	public String getThumbnailpic() {
		return thumbnailpic;
	}

	public void setThumbnailpic(String thumbnailpic) {
		this.thumbnailpic = thumbnailpic;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

}
