package cn.edu.tju.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import cn.edu.tju.constance.StudentConstance;
import cn.edu.tju.pojo.Lesson;
import cn.edu.tju.service.StudentService;
import cn.edu.tju.utils.JacksonUtil;

@Service
public class StudentServiceImpl implements StudentService {

	@Resource
	private CloseableHttpClient httpClient  ; 
	
	/**
	 * 获取execution
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String getExecutionStr() throws ClientProtocolException, IOException {
		HttpGet get = new HttpGet(StudentConstance.loginUrl);
		CloseableHttpResponse response = httpClient.execute(get);
		String html = EntityUtils.toString(response.getEntity());
		response.close();
		Document doc = Jsoup.parse(html);
		Elements elems = doc.getElementsByTag("input");
		String executionStr = elems.get(2).attr("value");
		System.out.println("【executionStr=】" + executionStr);
		return executionStr;
	}

	@Override
	public void selectClass(String lesonName) throws ClientProtocolException, IOException {
		String executionStr = getExecutionStr();
		String cookie  = login(executionStr);
		if(cookie == null)
		{
			System.out.println("【Login Failed...】");
			return ; 
		}
		getClassPage(cookie)  ; 
		String lessonId = getLessonId(lesonName) ; 
		if(lessonId ==null ) {
			System.out.println("找不到这门课");
			return ; 
		}
		String result = select(lessonId) ; 
		int count = 0 ; 
		while(!result.contains(StudentConstance.SUCCESS)) {
			if(count >= StudentConstance.iteratorCount) {
				break ; 
			}
			count ++ ; 
			System.out.println("尝试第"+count+"选课");
			result = select(lessonId) ; 
		}
		System.out.println(count >= StudentConstance.iteratorCount?"【选课Failed】":"【选课Success】");
	}
	
	/**
	 * 获取cookie
	 * @param execution
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String login(String execution) throws ClientProtocolException, IOException {
		HttpPost post = new HttpPost(StudentConstance.loginUrl);
		//这两个请求头必须设置
		post.setHeader("Content-Type" ,"application/x-www-form-urlencoded");
		post.setHeader("User-Agent" ,"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36");
		List<NameValuePair> list = new ArrayList<>();
		list.add(new BasicNameValuePair("username", StudentConstance.username));
		list.add(new BasicNameValuePair("password", StudentConstance.password));
		list.add(new BasicNameValuePair("execution", execution));
		list.add(new BasicNameValuePair("_eventId", "submit"));
		list.add(new BasicNameValuePair("geolocation", ""));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list);
		post.setEntity(entity);
		CloseableHttpResponse response = httpClient.execute(post);
		String cookie = response.getFirstHeader("Set-Cookie").getValue();
		response.close();
		System.out.println("【获取到的cookie=】 " + cookie);
		return cookie;
	}
	
	/**
	 * 携带cookie获取登陆页面
	 * @param cookie
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void getClassPage(String cookie)throws ClientProtocolException, IOException  {
		//���������getһ��
		HttpGet get = new HttpGet(StudentConstance.getClassPageUrl) ;
    	get.setHeader("User-Agent" , "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36");
		get.setHeader("Cookie" ,cookie);
		CloseableHttpResponse response = httpClient.execute(get);
		response.close();
		HttpPost post = new HttpPost(StudentConstance.getClassPageUrl)	 ; 
		List<NameValuePair> list = new ArrayList<>();
		list.add(new BasicNameValuePair("electionProfile.id", StudentConstance.profileId));
		post.setEntity(new UrlEncodedFormEntity(list));
		CloseableHttpResponse responsePost= httpClient.execute(post);
		System.out.println("【携带cookie后的post请求=】"+responsePost.getStatusLine());
		responsePost.close();
	}
	/**
	 * 获取lessonId
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String getLessonId(String lesonName) throws ClientProtocolException, IOException {
		HttpGet get = new HttpGet(StudentConstance.getLessonIdUrl) ;
		CloseableHttpResponse response = httpClient.execute(get);
		String responseStr = EntityUtils.toString(response.getEntity());
		System.out.println("【获取lessonId的get请求=】"+response.getStatusLine());
		String jsonStr = responseStr.substring(responseStr.indexOf('['),responseStr.length()-1) ;
		System.out.println("【获取到的json字符串=】"+jsonStr);
		List<Lesson> lessonList = JacksonUtil.json2BeanT(jsonStr.replaceAll("'", "\""), new TypeReference<List<Lesson>>() {
		}) ; 
		String lessonId = null ; 
		if(lessonList.size() != 0) {
			System.out.println("【Target Class:"+lesonName+"】");
			for(Lesson lesson : lessonList) {
				System.out.println("name:"+lesson.getName()+"  id"+lesson.getId());
				if(lesson.getName().equals(lesonName)) {
					lessonId=String.valueOf(lesson.getId())  ; 
					break ; 
				}
			}
		}
		System.out.println("【获取到的lessonId=】"+lessonId);
		response.close();
		return lessonId;
	}
	/**
	 * 根据lessonId选课
	 * @param lessonId
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String select(String lessonId)throws ClientProtocolException, IOException  {
		HttpPost post = new HttpPost(StudentConstance.selectUrl) ; 
		List<NameValuePair> list = new ArrayList<>();
    	list.add(new BasicNameValuePair("optype", "true"));
    	list.add(new BasicNameValuePair("operator0", lessonId+":true:0"));
    	post.setEntity(new UrlEncodedFormEntity(list));
		CloseableHttpResponse response = httpClient.execute(post);
		String html = EntityUtils.toString(response.getEntity());
		System.out.println("【选课状态=】"+response.getStatusLine());
		response.close();
		return html.trim() ; 
	}
	
}
