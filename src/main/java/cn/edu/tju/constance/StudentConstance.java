package cn.edu.tju.constance;

public class StudentConstance {
//	public static final String selectClassName = "数据库原理" ; //要选的课程名称
	public static final String username = "xxxxxxx" ; //用户名
	public static final String password = "xxxxxxxxxxxxx" ; //密码
	public static final String profileId = "421" ; //自己去网页上看，懒得获取了
	public static final String loginUrl = "https://sso.tju.edu.cn/cas/login?service=http%3A%2F%2Fee.tju.edu.cn%2FMain%2Finit.do" ; //登录页面的url
	public static final String getClassPageUrl = "http://classes.tju.edu.cn/eams/stdElectCourse!defaultPage.action" ; //post请求访问进入选课页面
	public static final String getLessonIdUrl = "http://classes.tju.edu.cn/eams/stdElectCourse!data.action?profileId="+ profileId; //访问获取lessonId
	public static final String selectUrl ="http://classes.tju.edu.cn/eams/stdElectCourse!batchOperator.action?profileId="+profileId ; //选课访问的url
	public static final int iteratorCount = 20 ; //最大迭代次数
	public static final String SUCCESS = "成功"   ;
}
