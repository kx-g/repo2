package service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
@WebServlet("/wx")
public class WxServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
     
 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		signature	微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
//		timestamp	时间戳
//		nonce	随机数
//		echostr	随机字符串
		System.out.println("get");
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		System.out.println(signature);
		System.out.println(timestamp);
		System.out.println(nonce);
		System.out.println(echostr);
//       校验签名
		if (WxService.check(signature,timestamp,nonce)) {
			System.out.println("接入成功");
			PrintWriter out= response.getWriter();
			//原样返回echostr参数
			out.print(echostr);
			out.flush();
			out.close();
		}else {
			
			System.out.println("接入失败");
		}
		
		
		
	}
 
			/**
			 * 接收消息  和事件推送
			 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf8");
		response.setCharacterEncoding("utf8");
		System.out.println("post");
		//处理消息和事件推送
	 Map<String,String> requestMap=	WxService.parseRequest(request.getInputStream());
		//打印解析出来的Map
		System.out.println(requestMap);
//		String huifu="<xml><ToUserName><![CDATA["+requestMap.get("FromUserName")+"]]></ToUserName><FromUserName><![CDATA[fromUser]]></FromUserName><CreateTime>"+System.currentTimeMillis()/1000+"</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[我是呀]]></Content></xml>";
//		String huifu="<xml>\r\n" + 
//				"<ToUserName><![CDATA["+requestMap.get("FromUserName")+"]]></ToUserName>\r\n" + 
//				"<FromUserName><![CDATA["+requestMap.get("ToUserName")+"]]></FromUserName>\r\n" + 
//				"<CreateTime>"+System.currentTimeMillis()/1000+"</CreateTime>\r\n" + 
//				"<MsgType><![CDATA[text]]></MsgType>\r\n" + 
//				"<Content><![CDATA[是啊是啊]]></Content>\r\n" + 
//				"</xml>";
		//上面第一个回复是因为  未加一个参数  
		//第二个回复成功了  但是这么做太麻烦
		//下面是简单的
		String huifu= WxService.getResponse(requestMap);
		System.out.println(huifu);
		PrintWriter out = response.getWriter();
		out.print(huifu);
		out.flush();
		out.close();
		
		
		
		
		
		
		
	}

}
