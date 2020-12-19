
// 참고 1:  https://shonm.tistory.com/126 
//		xml파싱해서 document객체로 저장

// 참고 2 : https://jeong-pro.tistory.com/144 출처
// xpath 라이브러리는 이용해서 element 찾아내기
package com.mycompany.myapp.json;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.mycompany.myapp.model.RouteS;
@Service
public class XmlParsing {
	//route_slave에 바로 저장될 경로 정보.
	
	public void getRouteInfo(RouteS rs,String transport, String publicDatas) {
		StringBuilder sb =null;
		try {
			//xml 읽어들일 객체 생성
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			// 앞에서 넘어온 xml 문자열을 document 객체로 생성.
			Document document = builder.parse(new InputSource(new StringReader(publicDatas)));
			
			//element를 찾아올 xpath 라이브러리 사용.
			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xpath = xPathFactory.newXPath();
			// 표현식 학습 필요!! // 참고 : http://tcpschool.com/xml/xml_xpath_pathExpression
			// <itemList> 
			//XPathExpression expr = xpath.compile("//itemList");
			XPathExpression expr = xpath.compile("//itemList");
			NodeList nodeList = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
			sb = new StringBuilder();
				
				// itemList의 자식 엘리먼트
				NodeList child = nodeList.item(0).getChildNodes();
				for(int j=0; j<child.getLength(); j++) {
					Node node =child.item(j);
					String nodeName = node.getNodeName();					
					if(nodeName.equals("pathList")) {
						NodeList nl = node.getChildNodes();
						for(int i =0; i<nl.getLength(); i++) {
							String nl_name = nl.item(i).getNodeName();
							if(!nl_name.equals("fname")) 
								if(!nl_name.equals("routeNm"))
									if(!nl_name.equals("tname"))
										continue;
							if(sb.length()!=0)sb.append(" => ");
							sb.append(nl.item(i).getTextContent());
						}
						if(transport.equals("Bus")) {
							rs.setBus_route(sb.toString());
						}else{
							rs.setComplex_route(sb.toString());
						};
					}else if(nodeName.equals("time")){
						if(transport.equals("Bus")) {
							rs.setBus_time(node.getTextContent());
						}else{
							rs.setComplex_time(node.getTextContent());
						};
					}
				}

		} catch (NullPointerException e) {
			System.out.println("public data service has no data");
			if(transport.equals("Bus")) {
				rs.setBus_time("NONE");
				rs.setBus_route("NONE");
			}else{
				rs.setComplex_time("NONE");
				rs.setComplex_route("NONE");
			};
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
}
