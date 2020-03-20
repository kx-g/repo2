package entity;

import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("xml")
public class ImageMessage extends BaseMessage {

	private String mediald;
	
	public String getMediald() {
		return mediald;
	}

	public void setMediald(String mediald) {
		this.mediald = mediald;
	}

	public ImageMessage(Map<String, String> requestMap,String mediald) {
		super(requestMap);
		this.setMsgType("image");
		this.setMediald(mediald);
	}

}
