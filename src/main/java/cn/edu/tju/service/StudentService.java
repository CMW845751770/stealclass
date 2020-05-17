package cn.edu.tju.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

public interface StudentService {

	/**
	 * 选课主函数
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	void selectClass(String lessonName) throws ClientProtocolException, IOException  ; 
}
