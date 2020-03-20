package service;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;
import com.thoughtworks.xstream.XStream;

import entity.Article;
import entity.BaseMessage;
import entity.ImageMessage;
import entity.MusicMessage;
import entity.NewsMessage;
import entity.TextMessage;
import entity.VideoMessage;
import entity.VoiceMessage;
import net.sf.json.JSONObject;
import util.Util;

public class WxService {
	private static final String  TOKEN = "zht";
//	private static final String TOKEN = "llzs";
	private static final String APPKEY="1fec136dbd19f44743803f89bd55ca62";
	/**
		 * 验证签名 
		 * @param signature
		 * @param timestamp
		 * @param nonce
		 * @return
		 */
	public static boolean check(String signature, String timestamp, String nonce) {
		
		 //  1）将token、timestamp、nonce三个参数进行字典序排序
			String[] strs=new String[] {TOKEN,timestamp,nonce};
			Arrays.sort(strs);
		 //  2）将三个参数字符串拼接成一个字符串进行sha1加密 
		String a=strs[0]+strs[1]+strs[2];
		System.out.println("a="+a);
		String mysig=sha1(a);
		System.out.println(mysig+"----");
		System.out.println("上下两个");
		System.out.println(signature+"----");
		//  3）开发者获得加密后的字符串可与signature对比， 
		  
		//equals验证返回过来的和传过来的signature是否对应
		return mysig.equalsIgnoreCase(signature);
	}
	/**
	 * 进行sha1加密
	 * @param a
	 * @return
	 */
	private static String sha1(String src) {
		try {
			//获取一个加密对象
			MessageDigest md = MessageDigest.getInstance("sha1");
			//加密
			byte[] digest = md.digest(src.getBytes());
			char[] chars= {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
			StringBuilder sb = new StringBuilder();
			//处理加密结果
			for(byte b:digest) {
				sb.append(chars[(b>>4)&15]);
				sb.append(chars[b&15]);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

//		private static String sha1(String src) {
//			try {
//				//获取加密对象
//				MessageDigest md=MessageDigest.getInstance("sha1");
//				//sha1加密
//				System.out.println(md);
//				byte[] digest = md.digest(src.getBytes());
//				char[] chars= {'0','1','2','3','4','5','6','7','8','a','b','c','d','e','f'};
//				StringBuilder sb=new StringBuilder();
//				System.out.println("sb");
//				//处理加密结果
//				 
//				for(byte b:digest) {
//					sb.append(chars[(b>>4)&15]);
//					sb.append(chars[b&15]); 
//				} 
//				return sb.toString();
//			} catch (NoSuchAlgorithmException e) { 
//				e.printStackTrace();
//			}
//			return null;
//		}
	
	/**
	 * 微信号发过来的是一个XML数据包  下面是解析   
	 * @param is
	 * @return
	 */
	public static Map<String, String> parseRequest(InputStream is) {
			Map<String ,String > map=new HashMap<>(); 
		SAXReader reader = new SAXReader();
		try {
			//读取输入流获取文档对象
			Document document = reader.read(is);
			//根据文档对象获取根节点
			Element root = document.getRootElement();
			//获取根节点对的所有子节点
			List<Element> elements = root.elements();
			for(Element e:elements) {
				map.put(e.getName(), e.getStringValue());
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//返回存入的键值对的map
		return map;

	}
	/**
	 * 用于处理所有时间的回复消息的信息
	 * @param requestMap
	 * @return返回的是XML数据包
	 * 张海涛qq 291301290
	 */
	public static String getResponse(Map<String, String> requestMap) {
		BaseMessage msg=null;
		String msgType=requestMap.get("MsgType");
		System.out.println("类型="+msgType);
		switch(msgType) {
		//处理文本消息
		case "text":
			msg=dealTextMessage(requestMap);
			break;
		case "image":
			
			break;
		case "voice":
			
			break;
		case "video":
			
			break;
		case "shortvideo":
			
			break;
		
		
		} 
		//把消息对象处理为XML数据包
	if (msg!=null) {
	return 	beanToXML(msg);
	}
	return null;
}
	//把消息对象处理为XML数据包
	private static String beanToXML(BaseMessage msg) {
		XStream stream=new XStream();
		// 设置需要处理XStreamAlias("xml")注释的类
		stream.processAnnotations(TextMessage.class);
		stream.processAnnotations(ImageMessage.class);
		stream.processAnnotations(MusicMessage.class);
		stream.processAnnotations(NewsMessage.class);
		stream.processAnnotations(VideoMessage.class);
		stream.processAnnotations(VoiceMessage.class);
		String xml=stream.toXML(msg);
		return xml;
	}
	/**
	 * 处理文本消息
	 * @param requestMap 
	 * @return
	 */
	private static BaseMessage dealTextMessage(Map<String, String> requestMap) {
		//用户发来的内容
		String msg=requestMap.get("Content");
		 if (msg.equals("罗小黑")) {
			 List<Article> article=new ArrayList<>();
			 article.add(new Article("罗小黑", "谛听与玄离不可告人的...", "http://mmbiz.qpic.cn/mmbiz_jpg/5feuRibwof7URZiaKP2XIOzdUCmP8t95ZL2wibZdfE6yUsPAJXf1Njojo7UTCdvy7SXH6ETSqa0rjoQyBa8nxJzibA/0", "www.baidu.com"));
			 NewsMessage nm=new NewsMessage(requestMap, article);
			 return nm;
					
		} 
		
		
		
		
		//调用方法返回聊天的内容
		String resp=chat(msg);
		TextMessage tm=new TextMessage(requestMap,resp);
		return tm;
	}
	/**
	 * 调用图灵机器人聊天
	 * @param msg
	 * @return
	 */
	private static String chat(String msg) {
		
		        String result =null;
		        String url ="http://op.juhe.cn/robot/index";//请求接口地址
		        Map params = new HashMap();//请求参数
		            params.put("key",APPKEY);//您申请到的本接口专用的APPKEY
		            params.put("info",msg);//要发送给机器人的内容，不要超过30个字符
		            params.put("dtype","");//返回的数据的格式，json或xml，默认为json
		            params.put("loc","");//地点，如北京中关村
		            params.put("lon","");//经度，东经116.234632（小数点后保留6位），需要写为116234632
		            params.put("lat","");//纬度，北纬40.234632（小数点后保留6位），需要写为40234632
		            params.put("userid","");//1~32位，此userid针对您自己的每一个用户，用于上下文的关联
		 
		        try {
		            result =Util.net(url, params, "GET");
		           //解析Json
		            JSONObject  jsonObject= JSONObject.fromObject(result);
		            //取出error_code
		            int code = jsonObject.getInt("error_code");
		            if (code!=0) {
						return null;
					}
		            //去除要返回的消息
		            String resp = jsonObject.getJSONObject("result").getString("text");
		            return resp;
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		
		        return null;
	}
	
	
	
	
	
	
	
	

}
